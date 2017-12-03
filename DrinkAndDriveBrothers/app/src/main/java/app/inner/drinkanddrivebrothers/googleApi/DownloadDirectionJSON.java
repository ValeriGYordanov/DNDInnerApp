package app.inner.drinkanddrivebrothers.googleApi;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by plame_000 on 11-Nov-17.
 */


class DownloadDirectionJSON extends AsyncTask<Void, Void, String> {

    @Override
    protected String doInBackground(Void... params) {
        String apiKey = "AIzaSyCq4_gkZpwFT22TfkU4ei77qTzWCNRhpaM";
        String stringUrl = "https://maps.googleapis.com/maps/api/directions/json?origin=Sofia&destination=Berkovitsa&key=" + apiKey;
        StringBuilder response = new StringBuilder();
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection httpconn = null;
        try {
            httpconn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()), 8192);
                String strLine = null;
                try {
                    while ((strLine = input.readLine()) != null) {
                        response.append(strLine);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String jsonOutput = response.toString();
        return jsonOutput;
    }

}

