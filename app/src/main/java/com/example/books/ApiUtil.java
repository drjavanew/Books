package com.example.books;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiUtil {
    private ApiUtil(){}

    public static final String BASE_API_URL =
            "https://www.googleapis.com/books/v1/volumes/";

    public static URL buildUrl(String title){
        String fullUrl = BASE_API_URL + "?q=" + title;
        URL url = null;

        try{
            url = new URL(fullUrl);

        }catch(Exception e) {
            e.printStackTrace();
        }
        return url;

    }

    public static  String getJson(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();


    }
}
