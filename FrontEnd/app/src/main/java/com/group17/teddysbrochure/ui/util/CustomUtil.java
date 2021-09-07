package com.group17.teddysbrochure.ui.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CustomUtil {
    private static CustomUtil networkUtil = null;
    static Context context;

    private  CustomUtil (Context ctx) {
        context = ctx.getApplicationContext();
    }

    public static synchronized CustomUtil getInstance(Context ctx) {
        CustomUtil network = null;
        if (networkUtil == null) {
            network = new CustomUtil(ctx);
        }
        return network;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return  isConnected;
    }

    public String convertInputStreamToString(InputStream stream) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        StringBuilder total = new StringBuilder();
        for (String line; (line = r.readLine()) != null; ) {
            total.append(line).append('\n');
        }
        r.close();
        return total.toString();
    }

    public void writeToInternalStorage(String fileName, String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readFromInternalStorage(String fileName) {
        String content = null;
        try {
            InputStream inputStream = context.openFileInput(fileName);
            content = convertInputStreamToString(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public String urlToBase64(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        byte[] imageStream = response.body().bytes();
        return Base64.encodeToString(imageStream, Base64.NO_WRAP);
    }
}
