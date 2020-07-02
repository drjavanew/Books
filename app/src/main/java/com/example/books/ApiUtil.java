package com.example.books;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ApiUtil {
    private static final String QUERY_PARAMENTER_KEY = "q";
    private static final String KEY = "key";
    private static final String API_KEY = "AIzaSyBPlGPQio3dTiL3E_WxlU-h1MQ8nprDXMs";

    private ApiUtil(){}

    public static final String BASE_API_URL =
            "https://www.googleapis.com/books/v1/volumes";

    public static URL buildUrl(String title){
        URL url = null;
        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAMENTER_KEY, title)
                .appendQueryParameter(KEY, API_KEY)
                .build();

        try{
            url = new URL(uri.toString());

        }catch(Exception e) {
            e.printStackTrace();
        }
        return url;

    }

    public static  String getJson(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            InputStream stream = connection.getInputStream();
            Scanner scanner = new Scanner(stream);
            scanner.useDelimiter("\\A");
            boolean hasData = scanner.hasNext();
            if(hasData){
                return scanner.next();
            } else {
                return null;
            }

        } catch (Exception e){
            Log.d("Error", e.toString());
            return null;
        } finally {
            connection.disconnect();
        }
    }
}
