package com.example.books;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class ApiUtil {
    private static final String QUERY_PARAMENTER_KEY = "q";
    private static final String KEY = "key";
    private static final String API_KEY = Apikey.getApiKey();
    private static final String TITLE = "intitle:";
    private static final String AUTHOR = "inauthor:";
    private static final String PUBLISHER = "inpublisher:";
    private static final String ISBN = "isbn:";

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

    public static ArrayList<Book> getBooksFromJson(String json){
        final String ID = "id";
        final String TITLE = "title";
        final String SUBTITLE = "subtitle";
        final String AUTHORS = "authors";
        final String PUBLISHER = "publisher";
        final String PUBLISHED_DATE = "publishedDate";
        final String ITEMS = "items";
        final String VOLUMEINFO = "volumeInfo";
        final String DESCRIPTION = "description";
        final String IMAGELINKS = "imageLinks";
        final String THUMBNAIL = "thumbnail";



        ArrayList<Book> books = new ArrayList<Book>();

        try {
            JSONObject jsonBooks = new JSONObject(json);
            JSONArray arrayBooks = jsonBooks.getJSONArray(ITEMS);
            int numberOfBooks = arrayBooks.length();

            for (int i = 0; i < numberOfBooks; i++){
                JSONObject bookJSON = arrayBooks.getJSONObject(i);
                JSONObject volumeInfoJSON =
                        bookJSON.getJSONObject(VOLUMEINFO);
                JSONObject imageLinksJSON = null;
                if (volumeInfoJSON.has(IMAGELINKS)) {
                    imageLinksJSON = volumeInfoJSON.getJSONObject(IMAGELINKS);
                }
                int authorNum;
                try {
                    authorNum = volumeInfoJSON.getJSONArray(AUTHORS).length();
                }catch (Exception e){
                    authorNum = 0;
                }
                String[] authors = new String[authorNum];
                for(int j = 0; j < authorNum; j++){
                    authors[j] = volumeInfoJSON.getJSONArray(AUTHORS).get(j).toString();
                }
                Book book = new Book(
                        bookJSON.getString(ID),
                        volumeInfoJSON.getString(TITLE),
                        (volumeInfoJSON.isNull(SUBTITLE)?"":volumeInfoJSON.getString(SUBTITLE)),
                        authors,
                        (volumeInfoJSON.isNull(PUBLISHER)?"":volumeInfoJSON.getString(PUBLISHER)),
                        (volumeInfoJSON.isNull(PUBLISHED_DATE)?"":volumeInfoJSON.getString(PUBLISHED_DATE)),
                        (volumeInfoJSON.isNull(DESCRIPTION)?"":volumeInfoJSON.getString(DESCRIPTION)),
                        (imageLinksJSON == null)?"":imageLinksJSON.getString(THUMBNAIL));
                books.add(book);

            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return books;

    }

    public static URL buildUrl(String title, String author, String publisher, String isbn){
        URL url = null;
        StringBuilder sb = new StringBuilder();
        if(!title.isEmpty()) sb.append(TITLE + title + "+");
        if(!author.isEmpty()) sb.append(AUTHOR + author + "+");
        if(!publisher.isEmpty()) sb.append(PUBLISHER + publisher + "+");
        if(!isbn.isEmpty()) sb.append(ISBN + isbn + "+");
        sb.setLength(sb.length() - 1);

        String query = sb.toString();
        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAMENTER_KEY, query)
                .appendQueryParameter(KEY, API_KEY)
                .build();

        try {
            url = new URL(uri.toString());

        }catch (Exception e){
            e.printStackTrace();
        }
        return url;
    }
}
