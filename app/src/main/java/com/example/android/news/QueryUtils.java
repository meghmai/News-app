package com.example.android.news;


import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
//An empty private constructor makes sure that the class is not going to be initialised.
    }

    public static List<News> fetchNewsData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<News> news = extractFeatureFromJson(jsonResponse);

        return news;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10001 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<News> extractFeatureFromJson(String newsJSON) {

        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        List<News> news = new ArrayList<>();

        try {

            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONObject newsResponse = baseJsonResponse.getJSONObject("response");

            JSONArray newsArray = newsResponse.getJSONArray("results");

            for (int i = 0; i < newsArray.length(); i++) {
                String url, date, section, title, img;
                JSONObject currentnews = newsArray.getJSONObject(i);
                if (currentnews.has("webUrl"))
                    url = currentnews.getString("webUrl");
                else
                    url = "";
                if (currentnews.has("webPublicationDate"))
                    date = currentnews.getString("webPublicationDate");
                else
                    date = "";
                if (currentnews.has("sectionName"))
                    section = currentnews.getString("sectionName");
                else
                    section = "";
                JSONObject fields;
                if (currentnews.has("fields")) {
                    fields = currentnews.getJSONObject("fields");
                    if (fields.has("headline"))
                        title = fields.getString("headline");
                    else
                        title = "";
                    if (fields.has("thumbnail"))
                        img = fields.getString("thumbnail");
                    else
                        img = "";
                } else {
                    title = "";
                    img = "";
                }
                JSONArray tag;
                String author = "";
                if (currentnews.has("tags")) {
                    tag = currentnews.getJSONArray("tags");
                    if (tag.length() != 0)
                        for (int k = 0; k < tag.length(); k++) {
                            String firstName, secondName;
                            JSONObject name = tag.getJSONObject(k);
                            if (name.has("firstName"))
                                firstName = name.getString("firstName");
                            else
                                firstName = "";
                            if (name.has("secondName"))
                                secondName = name.getString("secondName");
                            else
                                secondName = "";
                            author = firstName + " " + secondName;
                        }
                } else
                    author = "";

                News currentNews = new News(img, title, author, section, date, url);
                news.add(currentNews);

            }

        } catch (JSONException e) {

            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }

        return news;
    }
}
