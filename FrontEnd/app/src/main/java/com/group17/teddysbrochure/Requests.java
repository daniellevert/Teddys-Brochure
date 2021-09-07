package com.group17.teddysbrochure;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.solver.widgets.WidgetContainer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
/*
 * Requests is a class intended to process all of the html requests for the database.
 *
 *
 * Unfortunately, after figuring out how to run the requests on a different thread,
 * I realized that there is no way (that I know of) to wait on the main thread
 * for the response from the separate thread, and so I could not do any
 * parsing or processing of the data on the main thread because the json string
 * I was trying to grab was being returned as null and crashing the program. This is
 * my less-than-elegant fix, but I hope that it will work for this release, though I
 * know this might limit what we are able to do to some degree. Unfortunately this is
 * the best I can do at the moment.
 *
 * */

public class Requests {
    // lets messages be added to job queue of the main thread
    Handler uiThreadHandler =  new Handler(Looper.getMainLooper());
    // post jobs to the main thread when uiThreadHandler is invoked
    private void uiThreadHandler(Runnable r) {
        uiThreadHandler.post(r);
    }

    private OkHttpClient httpc; // make a connection with the server
    private String parkdbUrl;   // url for the nationalPark database
    private String reviewdbUrl; // url for the review database ***UNTESTED***

    // constructor
    //
    //
    public Requests(){
        this.httpc = new OkHttpClient();
        this.parkdbUrl = "http://45.79.221.157:3000/nationalPark/" + MainActivity.parkNameGlobal; // url for the national park db
        this.reviewdbUrl = "http://45.79.221.157:3000/review/60263b0b42d1ffd2ec84a6ac"; // url for the requests db
    }

    // get
    //
    // helper function for getParkdb and getReviewdb.
    //
    // Builds a Request object with a url and fetched the data from that request with a key
    // for either the National Park database or the reviews database based on the boolean
    // parkdb. If parkdb == true we build the request for the National Park database,
    // otherwise if parkdb == false we build the request for the reviews database.
    // This function is synchronoized, since it runs the get request on another thread.
    private synchronized void get(TextView tv, String key, boolean parkdb){

        Request curRequest;
        //parse parkdb and build the correct Request object
        if(parkdb){
            curRequest = new Request.Builder()
                    .url(parkdbUrl)
                    .build();
        }
        else{
            curRequest = new Request.Builder()
                    .url(reviewdbUrl)
                    .build();
        }

        // perform the get request on a seperate thread
        httpc.newCall(curRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // get the json data as a string
                    String jString = response.body().string();
                    uiThreadHandler(new Runnable(){
                        @Override
                        public void run() {

                            try {
                                // parse the json data into a json object
                                JSONArray jsonArray = new JSONArray(jString);
                                JSONObject serverResponse = jsonArray.getJSONObject(0);
                                // grab the data stored at 'key' in the server
                                String blurb = (String) serverResponse.get(key);
                                // set the text of the textview object
                                tv.setText(blurb);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } // end run
                    });
                } // uiThreadHandler
            } // end onResponse
        });
    } // end get

    // getParkdb
    //
    // gets the data from the National Park database at a specified key
    public void getParkdb(TextView tv, String key){
        get(tv, key, true);
    }

    // getReviewdb
    //
    // gets the data from the review database at at specified key
    public void getReviewdb(TextView tv, String key){
        get(tv, key, false);
    }

}
