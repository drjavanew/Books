package com.example.books;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

public class BookListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        TextView tvResponse = (TextView) findViewById(R.id.tvResponse);

        try {
            URL bookUrl = ApiUtil.buildUrl("cooking");
            String jsonResult = ApiUtil.getJson(bookUrl);
            tvResponse.setText(jsonResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}