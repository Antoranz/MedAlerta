package com.example.myapplication;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;

public class GetDataAsync {


    public static void getDataAsync(String url, Executor executor, OnTaskCompleted listener) {
        executor.execute(() -> {
            String result = doInBackground(url);
            if (listener != null) {
                listener.onTaskCompleted(result);
            }
        });
    }

    private static String doInBackground(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                InputStream in = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                StringBuilder stringBuilder = new StringBuilder();

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }

                bufferedReader.close();
                return stringBuilder.toString();
            } finally {
                urlConnection.disconnect();
            }
        } catch (IOException e) {
            Log.e("ObtenerDataTask", "Error al obtener data", e);
            return null;
        }
    }

    public interface OnTaskCompleted {
        void onTaskCompleted(String result);
    }

}
