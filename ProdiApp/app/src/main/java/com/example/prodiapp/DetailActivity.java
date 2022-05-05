package com.example.prodiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    TextView title,start,end,location,caption;
    String tl, st, ed, loc, cap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();

        tl = intent.getStringExtra("tittle");
        st = intent.getStringExtra("start");
        ed = intent.getStringExtra("end");
        loc = intent.getStringExtra("loc");
        cap = intent.getStringExtra("cap");

        title = findViewById(R.id.ed_tittle);
        start = findViewById(R.id.ed_date_start);
        end = findViewById(R.id.ed_date_end);
        location = findViewById(R.id.ed_location);
        caption = findViewById(R.id.ed_caption);

        title.setText(tl);
        start.setText(st);
        end.setText(ed);
        location.setText(loc);
        caption.setText(cap);

    }
}