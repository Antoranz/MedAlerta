package com.example.myapplication.utils.async;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;

public class PostDataAsync {

    public static void postDataAsync(String url, Executor executor, OnTaskCompleted callback, String method, String postData) {
        executor.execute(() -> {
            try {
                URL urlObj = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
                connection.setRequestMethod(method);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);


                if (postData != null) {
                    try (OutputStream os = connection.getOutputStream()) {
                        byte[] input = postData.getBytes(StandardCharsets.UTF_8);
                        os.write(input, 0, input.length);
                    }
                }

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {

                    try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = in.readLine()) != null) {
                            response.append(line);
                        }
                        if (response.length() > 0) {

                            callback.onTaskCompleted(response.toString());
                        } else {

                            callback.onTaskCompleted(null);
                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    callback.onTaskCompleted(null);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                try {
                    callback.onTaskCompleted(null);
                } catch (JSONException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public interface OnTaskCompleted {
        void onTaskCompleted(String result) throws JSONException;
    }

}
