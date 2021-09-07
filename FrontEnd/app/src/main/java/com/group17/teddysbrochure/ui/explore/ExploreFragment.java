package com.group17.teddysbrochure.ui.explore;

import android.content.Intent;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.group17.teddysbrochure.Attraction1;
import com.group17.teddysbrochure.Attraction2;
import com.group17.teddysbrochure.MainActivity;
import com.group17.teddysbrochure.R;
import com.group17.teddysbrochure.ui.util.CustomUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ExploreFragment extends Fragment {

    /*
     * Global variables
     */
    private String url = "http://45.79.221.157:3000";
    private String attractionFile = "attraction.json";

    private String parkName = MainActivity.parkNameGlobal; // Stores current park name
    private Handler handler = new Handler(Looper.getMainLooper());

    // UI objects below
    private FrameLayout attractionButton1;
    private FrameLayout attractionButton2;
    private TextView attractionTitle1;
    private TextView attractionTitle2;
    private ImageView attractionImg1;
    private ImageView attractionImg2;

    private CustomUtil util;

    /*
     * Creates UI view with selected fragment
     */
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    /*
     * Run after the UI view is created
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        util = CustomUtil.getInstance(this.getContext());

        // Store network availability status
        boolean isOnline = util.isNetworkAvailable();

        // Gets each UI object from the view and stores it to be used later
        attractionButton1 = getView().findViewById(R.id.attractionButton1);
        attractionButton2 = getView().findViewById(R.id.attractionButton2);
        attractionTitle1 = getView().findViewById(R.id.attractionTitle1);
        attractionTitle2 = getView().findViewById(R.id.attractionTitle2);
        attractionImg1 = getView().findViewById(R.id.attractionImage1);
        attractionImg2 = getView().findViewById(R.id.attractionImage2);

        // Checks if the device is in online or offline mode to call the appropriate
        // method to fetch the data
        if (isOnline) {
            downloadAndSaveImages();
            fetchAttractionPageContent();
        } else {
            fetchAttractionPageContentOffline();
        }

        // Each attraction button opens an attraction detail page when clicked
        attractionButton1.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), Attraction1.class);
            startActivity(intent);
        });

        attractionButton2.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), Attraction2.class);
            startActivity(intent);
        });

    }

    /*
     * Fetches and displays texts and images for the Explore page from internal storage
     */
    private void fetchAttractionPageContentOffline() {
        String parkContent = util.readFromInternalStorage(attractionFile);
        if (parkContent == null) return;

        try {
            // Gets Explore page data from whatever is stored in the device's internal storage
            JSONArray parks = new JSONArray(parkContent);
            JSONObject parkObj = parks.getJSONObject(0);
            JSONArray attractions = parkObj.getJSONArray("Attraction");

            // Gets all data for each attraction
            JSONObject attraction1 = attractions.getJSONObject(0);
            JSONObject attraction2 = attractions.getJSONObject(1);

            // Gets attractions' titles for each attraction bubble
            attractionTitle1.setText(attraction1.getString("AttractionName"));
            attractionTitle2.setText(attraction2.getString("AttractionName"));
            // Gets attractions' images for each attraction bubble and decodes them
            String imageEndPoint = attraction1.getJSONArray("AttractionImages").getString(0);
            String imageEndPoint2 = attraction2.getJSONArray("AttractionImages").getString(0);
            byte[] imageEndPointByte = Base64.decode(imageEndPoint, Base64.DEFAULT);
            byte[] imageEndPoint2Byte = Base64.decode(imageEndPoint2, Base64.DEFAULT);
            // Gets park overview text blurb
            String overview = parkObj.getString("blurb");

            // Uses Glide to have the images display on the pages
            Glide.with(this)
                    .load(imageEndPointByte)
                    .centerCrop()
                    .transform(new CenterCrop(),new RoundedCorners(20))
                    .into(attractionImg1);

            Glide.with(this)
                    .load(imageEndPoint2Byte)
                    .centerCrop()
                    .transform(new CenterCrop(),new RoundedCorners(20))
                    .into(attractionImg2);

            // Sets park overview text with the fetched text
            TextView parkOverviewBody = getView().findViewById(R.id.parkOverviewBody);
            parkOverviewBody.setText(overview);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
     * Fetches images from the database, converts them to base64,
     * and stores them in the internal storage for offline use
     */
    private void downloadAndSaveImages() {
        new Thread(() -> {
            try {
                // Request to get park data from database
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url + "/nationalPark/" + parkName)
                        .build();
                Response response = client.newCall(request).execute();
                JSONArray parks = new JSONArray(response.body().string());

                JSONObject parkObj = parks.getJSONObject(0);

                // Download and convert images to base64 for all images of each attraction
                JSONArray attractions = parkObj.getJSONArray("Attraction");
                for (int i = 0; i < attractions.length(); i++) { // go through each attraction in the attractions list
                    JSONObject attraction = attractions.getJSONObject(i);
                    JSONArray attractionImages = attraction.getJSONArray("AttractionImages");
                    for (int j = 0; j < attractionImages.length(); j++) { // go through and convert each image in the image list
                        String image = url + attractionImages.getString(j);
                        String base64 = util.urlToBase64(image);
                        attractionImages.put(j, base64);
                    }
                }

                // Stores these changes on the device locally for offline mode use
                util.writeToInternalStorage(attractionFile, parks.toString());

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /*
     * Fetches and displays texts and images for the Explore page from database
     */
    public void fetchAttractionPageContent(){
        new Thread(() -> {
            try {
                // Request to get park data from database
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url + "/nationalPark/" + parkName)
                        .build();
                Response response = client.newCall(request).execute();
                JSONArray parks = new JSONArray(response.body().string());

                JSONObject parkObj = parks.getJSONObject(0);

                // Gets all data for each attraction
                JSONArray attractions = parkObj.getJSONArray("Attraction");
                JSONObject attraction1 = attractions.getJSONObject(0);
                JSONObject attraction2 = attractions.getJSONObject(1);

                // Gets attractions' titles for each attraction bubble
                String title1 = attraction1.getString("AttractionName");
                String title2 = attraction2.getString("AttractionName");
                // Gets attractions' images for each attraction bubble
                String imageEndPoint = attraction1.getJSONArray("AttractionImages").getString(0);
                String imageEndPoint2 = attraction2.getJSONArray("AttractionImages").getString(0);
                // Gets park overview text blurb
                String overview = parkObj.getString("blurb");

                // Applies Explore page texts and images to have them displayed
                handler.post(() -> {
                    try {
                        // Sets attraction bubble titles with the fetched text
                        attractionTitle1.setText(title1);
                        attractionTitle2.setText(title2);

                        // Uses Glide to have the fetched images display on the pages
                        Glide.with(this)
                                .load(url + imageEndPoint)
                                .centerCrop()
                                .transform(new CenterCrop(),new RoundedCorners(20))
                                .into(attractionImg1);

                        Glide.with(this)
                                .load(url + imageEndPoint2)
                                .centerCrop()
                                .transform(new CenterCrop(),new RoundedCorners(20))
                                .into(attractionImg2);

                        // Sets park overview text with the fetched text
                        TextView parkOverviewBody = getView().findViewById(R.id.parkOverviewBody);
                        parkOverviewBody.setText(overview);

                    } catch (Exception e) {
                        System.out.println(e);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
