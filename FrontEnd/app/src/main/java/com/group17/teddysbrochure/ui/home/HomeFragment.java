package com.group17.teddysbrochure.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.group17.teddysbrochure.MainActivity;
import com.group17.teddysbrochure.R;
import com.group17.teddysbrochure.ui.explore.ExploreFragment;
import com.group17.teddysbrochure.ui.history.HistoryFragment;
import com.group17.teddysbrochure.ui.reviews.ReviewAdapter;
import com.group17.teddysbrochure.ui.reviews.ReviewsFragment;
import com.group17.teddysbrochure.ui.util.CustomUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    private String parkNameFile = "nationalParks.json";
    private String weatherFile = "weather.json";
    private String reviewFile = "review.json";

    private String url = "http://45.79.221.157:3000";
    private String parkName = MainActivity.parkNameGlobal;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Button visitPark;
    private Button callPark;
    private TextView parkHistoryPreview;
    private LinearLayout parkHistoryBubble;
    private FrameLayout exploreBubble;
    private TextView recentReviewTitle;
    private TextView recentReviewer;
    private TextView recentReviewStar;
    private TextView recentReviewText;
    private LinearLayout reviewBubble;

    //RecyclerView For Weather
    RecyclerView recyclerView;
    private List<String> Day = new ArrayList<>();
    private List<String> High = new ArrayList<>();
    private List<String> Low = new ArrayList<>();
    List<WeatherModel> weatherModelList;
    WeatherAdapter weatherAdapter;

    private String title = new String();
    private String stars = new String();
    private String reviewText = new String();
    private String reviewer = new String();

    private CustomUtil util;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        util = CustomUtil.getInstance(this.getContext());
        boolean isOnline = util.isNetworkAvailable();

        // if there is an internet connection we download and save
        // the json data so it can be used offline
        if (isOnline) {
            downloadAndSaveContent();
            fetchWeather();
            fetchImage();
            fetchRecentReview();
            fetchParkHistoryPreview();
        // if no internet connection use the saved json data from last
        // database query
        } else {
            fetchWeatherOffline();
            fetchImageOffline();
            fetchRecentReviewOffline();
            fetchParkHistoryPreviewOffline();
        }

        // visitPark button opens the park website in a browser window
        visitPark = getView().findViewById(R.id.visitButton);
        visitPark.setOnClickListener(new View.OnClickListener() { // set to visit site when clicked
            @Override
            public void onClick(View v) {
                visitSite();
            }
        });
        callPark = getView().findViewById(R.id.callButton);
        callPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPark();
            }
        });

        // parkHistoryBubble takes user to history page when clicked, displays
        // a preview of the park history text
        parkHistoryBubble = getView().findViewById(R.id.parkHistoryBubble);
        parkHistoryBubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HistoryFragment()).commit(); }
        });

        // exploreBubble takes user to the explore page, displays a preview
        // of the explore text
        exploreBubble = getView().findViewById(R.id.exploreBubble);
        exploreBubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ExploreFragment()).commit(); }
        });

        // reviewBubble takes user to the review page, displays a preview
        // of the reviews
        reviewBubble = getView().findViewById(R.id.reviewBubble);
        reviewBubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ReviewsFragment()).commit(); }
        });
    }

    // fetch the weather data from the last database query
    private void fetchWeatherOffline() {
        String weatherContent = util.readFromInternalStorage(weatherFile);
        if (weatherContent == null) return;

        try {
            JSONArray daily = new JSONArray(weatherContent);

            Calendar calendar = Calendar.getInstance();
            for (int i = 0; i < daily.length(); i++) {
                JSONObject tempObj = daily.getJSONObject(i);
                double max = tempObj.getJSONObject("temp").getDouble("max");
                double min = tempObj.getJSONObject("temp").getDouble("min");
                Date date = calendar.getTime();
                String day = new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime());

                Day.add(day);
                High.add(Math.round(max) + "°F");
                Low.add(Math.round(min) + "°F");
                calendar.add(Calendar.DATE, 1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        createHorizontalRecyclerView();
    }

    // fetch the image data from the last database query
    private void fetchImageOffline() {
        String weatherContent = util.readFromInternalStorage(parkNameFile);
        if (weatherContent == null) return;

        try {
            JSONArray parks = new JSONArray(weatherContent);
            JSONObject park = parks.getJSONObject(0);
            String imageEndPoint = park.getJSONArray("images").getString(0);
            String imageEndPoint2 = park.getJSONArray("images").getString(2);

            byte[] imageEndPointByte = Base64.decode(imageEndPoint, Base64.DEFAULT);
            byte[] imageEndPoint2Byte = Base64.decode(imageEndPoint2, Base64.DEFAULT);

            ImageView mainImage = getView().findViewById(R.id.mainImage);
            ImageView attractionImage = getView().findViewById(R.id.attractionImage);
            Glide.with(this)
                    .load(imageEndPointByte)
                    .centerCrop()
                    .transform(new CenterCrop(),new RoundedCorners(20))
                    .into(mainImage);

            Glide.with(this)
                    .load(imageEndPoint2Byte)
                    .centerCrop()
                    .transform(new CenterCrop(),new RoundedCorners(20))
                    .into(attractionImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // fetch the reviews from the last database query
    private void fetchRecentReviewOffline() {
        String reviewContent = util.readFromInternalStorage(reviewFile);
        if (reviewContent == null) return;

        try {
            JSONArray reviews = new JSONArray(reviewContent);
            JSONObject review = reviews.getJSONObject(reviews.length()-1);
            stars = review.getString("stars");
            reviewText = review.getString("review");
            reviewer = review.getString("reviewer");

            recentReviewer = getView().findViewById(R.id.reviewer);
            recentReviewStar = getView().findViewById(R.id.reviewStars);
            recentReviewText = getView().findViewById(R.id.reviewText);

            recentReviewStar.setText(stars);
            recentReviewText.setText(reviewText);
            recentReviewer.setText(reviewer);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // fetch the park history text from the last database query
    private void fetchParkHistoryPreviewOffline() {
        String reviewContent = util.readFromInternalStorage(parkNameFile);
        if (reviewContent == null) return;

        try {
            JSONArray parks = new JSONArray(reviewContent);
            JSONObject park = parks.getJSONObject(0);
            String history = park.getString("parkHistory");

            int i = 0;
            while (history.charAt(i) != '.' && i != history.length()) {
                i++;
            }

            String shortenedHistory = history.substring(0, i+1);

            parkHistoryPreview = getView().findViewById(R.id.parkHistoryPreview);
            parkHistoryPreview.setText(shortenedHistory);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // download the content from the database query so we can use it in offline mode
    private void downloadAndSaveContent() {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url + "/nationalPark/" + parkName)
                        .build();
                Response response = client.newCall(request).execute();
                JSONArray parks = new JSONArray(response.body().string());

                JSONObject parkObj = parks.getJSONObject(0);
                String parkId = parkObj.getString("_id");

                // Download and convert images to base64
                JSONArray images = parkObj.getJSONArray("images");
                for (int i = 0; i < images.length(); i++) {
                    String image = url + images.getString(i);
                    String base64 = util.urlToBase64(image);
                    images.put(i, base64);
                }

                util.writeToInternalStorage(parkNameFile, parks.toString());

                // Weather
                double lat = parkObj.getDouble("latdec");
                double lon = parkObj.getDouble("longdec");

                client = new OkHttpClient();
                request = new Request.Builder()
                        .url(url + "/weather/?lat=" + lat + "&long=" + lon)
                        .build();
                response = client.newCall(request).execute();
                JSONObject weatherResponse = new JSONObject(response.body().string());
                JSONArray daily = weatherResponse.getJSONArray("daily");

                util.writeToInternalStorage(weatherFile, daily.toString());

                // Reviews
                request = new Request.Builder()
                        .url(url + "/review/" + parkId)
                        .build();
                response = client.newCall(request).execute();
                JSONArray reviews = new JSONArray(response.body().string());

                util.writeToInternalStorage(reviewFile, reviews.toString());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // when the call park button is pressed pull up the dial pad
    // with the park number filled in
    private void callPark(){
        new Thread(() -> {
            try {
                //request to get park ID
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url + "/nationalPark/" + parkName)
                        .build();
                Response response = client.newCall(request).execute();
                JSONArray parks = new JSONArray(response.body().string());
                JSONObject deathValley = parks.getJSONObject(0);

                // create parsable uri string
                // tel:<phonenumber>
                // List of uri schemes can be found at:
                // https://en.wikipedia.org/wiki/List_of_URI_schemes#Generic_syntax
                String sitePhone = "tel:" + deathValley.getString("phone");

                /* From the docs https://developer.android.com/reference/android/content/Intent:
                 * Intent object is abstract description of an operation to be performed.
                 * It can be used with startActivity to launch an activity
                 * It is used in the launching of activities, where it can be thought of as the
                 * glue between activities.
                 */
                Intent callPark = new Intent(Intent.ACTION_DIAL, Uri.parse(sitePhone));
                startActivity(callPark); // ask android os to go to site

            } catch (IOException e) {
                System.out.println("unable to fetch request");
            } catch (JSONException e) {
                System.out.println("unable to parse request");
            }
        }).start();

    }

    // when the visitSite buttin is clicked open a window in the browser
    // with the official park website.
    private void visitSite() {
        new Thread(() -> {
            try {
                //request to get park ID
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url + "/nationalPark/" + parkName)
                        .build();
                Response response = client.newCall(request).execute();
                JSONArray parks = new JSONArray(response.body().string());
                JSONObject deathValley = parks.getJSONObject(0);
                String siteUrl = deathValley.getString("parkurl"); // get the part url from the database

                /* From the docs https://developer.android.com/reference/android/content/Intent:
                 * Intent object is abstract description of an operation to be performed.
                 * It can be used with startActivity to launch an activity
                 * It is used in the launching of activities, where it can be thought of as the
                 * glue between activities.
                 */
                Intent goToSite = new Intent(Intent.ACTION_VIEW, Uri.parse(siteUrl));
                startActivity(goToSite); // ask android os to go to site

            } catch (IOException e) {
                System.out.println("unable to fetch request");
            } catch (JSONException e) {
                System.out.println("unable to parse request");
            }
        }).start();
    }

    // fetch the images from the database
    private void fetchImage() {
        new Thread(() -> {
            try {
                //request to get park ID
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url + "/nationalPark/" + parkName)
                        .build();
                Response response = client.newCall(request).execute();
                JSONArray parks = new JSONArray(response.body().string());
                JSONObject deathValley = parks.getJSONObject(0);
                String imageEndPoint = deathValley.getJSONArray("images").getString(0);
                String imageEndPoint2 = deathValley.getJSONArray("images").getString(2);


                handler.post(() -> { // set up list view
                    ImageView mainImage = getView().findViewById(R.id.mainImage);
                    ImageView attractionImage = getView().findViewById(R.id.attractionImage);
                    try {
                        Glide.with(this)
                                .load(url + imageEndPoint)
                                .centerCrop()
                                .transform(new CenterCrop())
                                .into(mainImage);

                        Glide.with(this)
                                .load(url + imageEndPoint2)
                                .centerCrop()
                                .transform(new CenterCrop(),new RoundedCorners(20))
                                .into(attractionImage);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                });

            } catch (IOException e) {
                System.out.println("unable to fetch request");
            } catch (JSONException e) {
                System.out.println("unable to parse request");
            }
        }).start();
    }

    // create the view that embeds the weather information
    private void createHorizontalRecyclerView() {
        //RecyclerView
        recyclerView = getView().findViewById(R.id.weatherRecyclerView);
        weatherModelList = new ArrayList<>();
        for (int i = 0; i < Day.size(); i++) {
            WeatherModel temp = new WeatherModel(Day.get(i), High.get(i), Low.get(i));
            weatherModelList.add(temp);
        }
        //Design Layout
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //initialize weather adapter
        weatherAdapter = new WeatherAdapter(getContext(), weatherModelList);
        recyclerView.setAdapter(weatherAdapter);
    }

    // fetch the weather information from the database
    private void fetchWeather() {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url + "/nationalPark/" + parkName)
                        .build();
                Response response = client.newCall(request).execute();
                JSONArray parks = new JSONArray(response.body().string());
                JSONObject deathValley = parks.getJSONObject(0);
                double lat = deathValley.getDouble("latdec");
                double lon = deathValley.getDouble("longdec");

                //request to get park ID
                client = new OkHttpClient();
                request = new Request.Builder()
                        .url(url + "/weather/?lat=" + lat + "&long=" + lon)
                        .build();
                response = client.newCall(request).execute();
                JSONObject weatherResponse = new JSONObject(response.body().string());
                JSONArray daily = weatherResponse.getJSONArray("daily");

                handler.post(() -> { // set up list view
                    try {
                        Calendar calendar = Calendar.getInstance();
                        for (int i = 0; i < daily.length(); i++) {
                            JSONObject tempObj = daily.getJSONObject(i);
                            double max = tempObj.getJSONObject("temp").getDouble("max");
                            double min = tempObj.getJSONObject("temp").getDouble("min");
                            Date date = calendar.getTime();
                            String day = new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime());

                            Day.add(day);
                            High.add(Math.round(max) + "°F");
                            Low.add(Math.round(min) + "°F");
                            calendar.add(Calendar.DATE, 1);
                        }

                        createHorizontalRecyclerView();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                });

            } catch (IOException e) {
                System.out.println("unable to fetch request");
            } catch (JSONException e) {
                System.out.println("unable to parse request");
            }
        }).start();
    }

    // fetch the park history from the database
    private void fetchParkHistoryPreview() {
        new Thread(() -> {
            try {
                //request to get park ID
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url + "/nationalPark/" + parkName)
                        .build();
                Response response = client.newCall(request).execute();
                JSONArray parks = new JSONArray(response.body().string());
                JSONObject deathValley = parks.getJSONObject(0);
                String history = deathValley.getString("parkHistory");

                // get the index of the end of the first sentence then create a substring using it
                int i = 0;
                while (history.charAt(i) != '.' && i != history.length()) {
                    i++;
                }

                String shortenedHistory = history.substring(0, i+1);
                handler.post(() -> {
                    try {
                        parkHistoryPreview = getView().findViewById(R.id.parkHistoryPreview);
                        parkHistoryPreview.setText(shortenedHistory);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                });

            } catch (IOException e) {
                System.out.println("unable to fetch request");
            } catch (JSONException e) {
                System.out.println("unable to parse request");
            }
        }).start();
    }

    // Get the most recent review from the database
    private void fetchRecentReview() {
        new Thread(() -> {
            try {
                //request to get park ID
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url + "/nationalPark/" + parkName)
                        .build();
                Response response = client.newCall(request).execute();
                JSONArray parks = new JSONArray(response.body().string());
                JSONObject deathValley = parks.getJSONObject(0);
                String deathValleyID = deathValley.getString("_id");

                //request to get reviews
                request = new Request.Builder()
                        .url(url + "/review/" + deathValleyID)
                        .build();

                response = client.newCall(request).execute();
                JSONArray reviews = new JSONArray(response.body().string());

                JSONObject review = reviews.getJSONObject(reviews.length()-1);
                stars = review.getString("stars");
                reviewText = review.getString("review");
                reviewer = review.getString("reviewer");


                handler.post(() -> { // set up list view
                    try {
                        recentReviewer = getView().findViewById(R.id.reviewer);
                        recentReviewStar = getView().findViewById(R.id.reviewStars);
                        recentReviewText = getView().findViewById(R.id.reviewText);
                        recentReviewStar.setText(stars);
                        recentReviewText.setText(reviewText);
                        recentReviewer.setText(reviewer);

                    } catch (Exception e) {
                        System.out.println(e);
                    }
                });

            } catch (IOException e) {
                System.out.println("unable to fetch request");
            } catch (JSONException e) {
                System.out.println("unable to parse request");
            }
        }).start();
    }

}
