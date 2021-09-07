package com.group17.teddysbrochure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Attraction1 extends AppCompatActivity {
    TextView viewAttractionTitle;
    TextView viewAttractionBody;
    Button backButton;

    private String url = "http://45.79.221.157:3000";
    private String parkName = MainActivity.parkNameGlobal;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attractions);
        viewAttractionTitle = findViewById(R.id.viewAttractionTitle);
        viewAttractionBody = findViewById(R.id.viewAttractionBody);
        setAttractionText();
        fetchAttraction();
        fetchImage();
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setAttractionText() {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url + "/nationalPark/" + parkName)
                        .build();
                Response response = client.newCall(request).execute();
                JSONArray parks = new JSONArray(response.body().string());
                JSONObject deathValley = parks.getJSONObject(0);
                JSONArray attractions = deathValley.getJSONArray("Attraction");
                JSONObject attraction1 = attractions.getJSONObject(0);

                String title1 = attraction1.getString("AttractionName");
                handler.post(() -> {
                    try {
                        viewAttractionTitle.setText(title1);

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
      }// end setAttractionText

    private void fetchAttraction() {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url + "/nationalPark/" + parkName)
                        .build();
                Response response = client.newCall(request).execute();
                JSONArray parks = new JSONArray(response.body().string());
                JSONObject deathValley = parks.getJSONObject(0);
                JSONArray attractions = deathValley.getJSONArray("Attraction");
                JSONObject attraction = attractions.getJSONObject(0); // 0 since attraction 1
                String blurb = attraction.getString("AttractionDescription");
                handler.post(() -> {
                    try {
                        viewAttractionBody.setText(blurb);
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
                
                
                JSONArray attractions = deathValley.getJSONArray("Attraction");
                JSONObject attraction = attractions.getJSONObject(0); // 0 since attraction 1

                JSONArray atImageArray= attraction.getJSONArray("AttractionImages");
                String atImageEndPoint = atImageArray.getString(0);
                


                handler.post(() -> { // set up list view
                    ImageView mainImage = findViewById(R.id.imageViewat1);
                    ImageView attractionImage = findViewById(R.id.attractionImage);
                    try {
                        Glide.with(this)
                                .load(url + atImageEndPoint)
                                .centerCrop()
                                .into(mainImage);

                        Glide.with(this)
                                .load(url + imageEndPoint2)
                                .centerCrop()
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
    }