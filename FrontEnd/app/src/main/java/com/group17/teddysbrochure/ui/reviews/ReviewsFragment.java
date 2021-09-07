package com.group17.teddysbrochure.ui.reviews;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.group17.teddysbrochure.MainActivity;
import com.group17.teddysbrochure.R;
import com.group17.teddysbrochure.ui.util.CustomUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReviewsFragment extends Fragment implements CreateReviewDialog.ReviewDialogListener {

    private String url = "http://45.79.221.157:3000";
    private String fileName = "review.json";
    private String parkName = MainActivity.parkNameGlobal;
    private Handler handler = new Handler(Looper.getMainLooper());
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private ListView reviewList;
    private List<String> titles = new ArrayList<>();
    private List<String> reviewsers = new ArrayList<>();
    private List<String> stars = new ArrayList<>();
    private List<String> reviewsText = new ArrayList<>();

    private Button createReviewBtn;
    private CustomUtil util;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reviews, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Check If Phone is connected or not
        util = CustomUtil.getInstance(this.getContext());
        boolean isOnline = util.isNetworkAvailable();

        // fetch for data if online and save data, else load if from assets folders
        if (isOnline) {
            fetchReviews();
            createReview();
        } else {
            fetchReviewsOffline();
            createReviewOffline();
        }
    }

    private void createReview() {
         createReviewBtn = getView().findViewById(R.id.createReviewButton);
         createReviewBtn.setOnClickListener(e -> {
            CreateReviewDialog reviewDialog = new CreateReviewDialog(this);
            reviewDialog.show(getChildFragmentManager(), "Alert");
         });
    }

    private void createReviewOffline() {
        createReviewBtn = getView().findViewById(R.id.createReviewButton);
        createReviewBtn.setOnClickListener(e -> {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Can't Review When In Offline Mode");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        });
    }

    private void fetchReviewsOffline() {
        String reviews = util.readFromInternalStorage(fileName);
        if (reviews == null) return;

        JSONArray reviewJsonArr = null;
        try {
            reviewJsonArr = new JSONArray(reviews);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (reviewJsonArr == null) return;

        for (int i = 0; i < reviewJsonArr.length(); i++) {
            JSONObject review = reviewJsonArr.optJSONObject(i);
            titles.add(review.optString("title"));
            stars.add(review.optString("stars"));
            reviewsText.add(review.optString("review"));
            reviewsers.add(review.optString("reviewer"));
        }

        reviewList = (ListView) getView().findViewById(R.id.ReviewList);
        ReviewAdapter reviewAdapter = new ReviewAdapter(this.getContext(), titles, reviewsers, stars, reviewsText);
        reviewList.setAdapter(reviewAdapter);
    }

    private void fetchReviews() {
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

                util.writeToInternalStorage(fileName, reviews.toString()); // save to files.

                for (int i = 0; i < reviews.length(); i++) {
                    JSONObject review = reviews.getJSONObject(i);
                    titles.add(review.getString("title"));
                    stars.add(review.getString("stars"));
                    reviewsText.add(review.getString("review"));
                    reviewsers.add(review.getString("reviewer"));
                }

                handler.post(() -> { // set up list view
                    try {
                        reviewList = (ListView) getView().findViewById(R.id.ReviewList);
                        ReviewAdapter reviewAdapter = new ReviewAdapter(this.getContext(), titles, reviewsers, stars, reviewsText);
                        reviewList.setAdapter(reviewAdapter);
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

    @Override
    public void applyTexts(String reviewTitle, String reviewer, String review, String star) {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url + "/nationalPark/" + parkName)
                        .build();
                Response response = client.newCall(request).execute();
                JSONArray parks = new JSONArray(response.body().string());
                JSONObject deathValley = parks.getJSONObject(0);
                String deathValleyID = deathValley.getString("_id");

                JSONObject reviewObj = new JSONObject();
                reviewObj.put("parkID", deathValleyID);
                reviewObj.put("title", reviewTitle);
                reviewObj.put("reviewer", reviewer);
                reviewObj.put("review", review);
                reviewObj.put("stars", Double.parseDouble(star));

                RequestBody body = RequestBody.create(JSON, reviewObj.toString());
                request = new Request.Builder()
                        .url(url + "/review/" + deathValleyID)
                        .post(body)
                        .build();
                response = client.newCall(request).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
