package com.group17.teddysbrochure.ui.history;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
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

public class HistoryFragment extends Fragment {

    /*
     * Global variables
     */
    private String url = "http://45.79.221.157:3000";
    private String parkName = MainActivity.parkNameGlobal;
    private String parkHistoryFile = "parkHistory.json";
    private Handler handler = new Handler(Looper.getMainLooper());
    private TextView parkHistoryBody;

    private CustomUtil util;

    /*
     * Creates UI view with selected fragment
     */
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    /*
     * Run after the UI view is created
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        util = CustomUtil.getInstance(this.getContext());
        boolean isOnline = util.isNetworkAvailable();

        // Checks if the device is in online or offline mode to call the appropriate
        // method to fetch the data
        if (isOnline) {
            fetchParkHistoryPageContent();
        } else {
            fetchParkHistoryPageContentOffline();
        }
    }

    /*
     * Fetches stored Park History page text and images if available when offline
     */
    private void fetchParkHistoryPageContentOffline() {
        // Reads what has been stored from the previous database fetch if in offline mode
        String reviewContent = util.readFromInternalStorage(parkHistoryFile);
        if (reviewContent == null) return; // Nothing stored, so return

        try {
            // Gets park history paragraph and image for the page
            JSONArray parks = new JSONArray(reviewContent);
            JSONObject parkObj = parks.getJSONObject(0);
            String history = parkObj.getString("parkHistory");
            String historyImage =  parkObj.getJSONArray("images").getString(1);

            // Applies park history paragraph text and image
            // First, sets TextView for the page with the fetched text
            parkHistoryBody = getView().findViewById(R.id.parkHistoryBody);
            parkHistoryBody.setText(history);
            ImageView historyImageView = getView().findViewById(R.id.parkHistoryImage);

            byte[] historyImageViewByte = Base64.decode(historyImage, Base64.DEFAULT);

            // Uses Glide to have the images display on the pages
            Glide.with(this)
                    .load(historyImageViewByte)
                    .centerCrop()
                    .into(historyImageView);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
     * Fetches Park History page text and images from the server and stores the data in
     * internal storage for offline use
     */
    private void fetchParkHistoryPageContent() {
        new Thread(() -> { // The following actions are performed on a new thread
            try {
                // Request to get park data from database
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url + "/nationalPark/" + parkName)
                        .build();
                Response response = client.newCall(request).execute();
                JSONArray parks = new JSONArray(response.body().string());

                JSONObject parkObj = parks.getJSONObject(0);

                // Gets park history paragraph and image for the page
                String history = parkObj.getString("parkHistory");
                String historyImage =  parkObj.getJSONArray("images").getString(1);

                // Converts each image URL for the park to base64 to be used with Glide
                JSONArray images = parkObj.getJSONArray("images");
                for (int i = 0; i < images.length(); i++) {
                    String image = url + images.getString(i);
                    String base64 = util.urlToBase64(image);
                    images.put(i, base64);
                }

                // Stores fetched Park History data on the device locally for offline mode use
                util.writeToInternalStorage(parkHistoryFile, parks.toString());

                // Applies park history paragraph text and image
                handler.post(() -> {
                    try {
                        // Sets TextView for the page with the fetched text
                        parkHistoryBody = getView().findViewById(R.id.parkHistoryBody);
                        parkHistoryBody.setText(history);

                        ImageView historyImageView = getView().findViewById(R.id.parkHistoryImage);

                        // Uses Glide to have the fetched images display on the pages
                        Glide.with(this)
                                .load(url + historyImage)
                                .centerCrop()
                                .into(historyImageView);
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
