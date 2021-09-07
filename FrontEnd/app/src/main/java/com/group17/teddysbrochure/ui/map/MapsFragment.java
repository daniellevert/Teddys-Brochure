package com.group17.teddysbrochure.ui.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.group17.teddysbrochure.MainActivity;
import com.group17.teddysbrochure.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapsFragment extends Fragment {

    private String url = "http://45.79.221.157:3000";
    private Handler handler = new Handler(Looper.getMainLooper());

    private String parkName = MainActivity.parkNameGlobal;
    private Double lat = 0.0;
    private Double lng = 0.0;
    LatLng position;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            fetchLatLng(googleMap);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fullMapsComponent);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

    }

    // get the latitude and longitude of the national park from the database
    private void fetchLatLng(GoogleMap googleMap) {
        new Thread(() -> {
            try {
                //request to get park ID
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url + "/nationalPark/" + parkName)
                        .build();
                Response response = client.newCall(request).execute();
                JSONArray parks = new JSONArray(response.body().string());
                JSONObject currPark = parks.getJSONObject(0);
                Double latitude = currPark.getDouble("latdec");
                Double longitude = currPark.getDouble("longdec");

                handler.post(() -> {
                    try {
                        lat = latitude;
                        lng = longitude;

                        position = new LatLng(lat, lng);
                        googleMap.addMarker(new MarkerOptions().position(position).title("Marker in " + parkName));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 8f));
                    } catch (Exception e){
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

    //Getters added for testing:
    public Double getLat(){
        Double res = this.lat;
        return res;
    }

    public Double getLong(){
        return this.lng;
    }
}