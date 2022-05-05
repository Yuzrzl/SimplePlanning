package com.example.prodiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prodiapp.Endpoint.Endpoint;
import com.example.prodiapp.Endpoint.RequestData;
import com.example.prodiapp.Model.ResponseModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UbahActivity extends AppCompatActivity {
    private int xId;
    private EditText et_ds,et_de,et_loc,et_titt,et_cap;
    private Button btn_simpan;
    private String dateStart, dateEnd, location, tittle, caption;
    Calendar calendar;
    DatePickerDialog.OnDateSetListener dateS,dateE;

    TextView dateSt,dateN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah);

        //calendarPicker
        dateSt = findViewById(R.id.ed_date_start);
        dateN = findViewById(R.id.ed_date_end);
        calendar = Calendar.getInstance();
        dateS = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                calendar.set(Calendar.DAY_OF_MONTH, d);
                calendar.set(Calendar.MONTH, m);
                calendar.set(Calendar.YEAR, y);

                TextView dateStart = findViewById(R.id.ed_date_start);


                String format = "dd MMMM yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
                dateStart.setText(simpleDateFormat.format(calendar.getTime()));

            }
        };
        dateE = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                calendar.set(Calendar.DAY_OF_MONTH, d);
                calendar.set(Calendar.MONTH, m);
                calendar.set(Calendar.YEAR, y);


                TextView dateEnd = findViewById(R.id.ed_date_end);

                String format = "dd MMMM yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);

                dateEnd.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };
        dateSt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(UbahActivity.this, dateS,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        dateN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(UbahActivity.this, dateE,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });//endCalendarPicker

// tangkap data
        Intent get = getIntent();
        xId = get.getIntExtra("xid",-1);
        dateStart = get.getStringExtra("dateStart");
        dateEnd = get.getStringExtra("dateEnd");
        location = get.getStringExtra("location");
        tittle = get.getStringExtra("tittle");
        caption = get.getStringExtra("caption");


        et_ds = findViewById(R.id.ed_date_start);
        et_de = findViewById(R.id.ed_date_end);
        et_loc = findViewById(R.id.ed_location);
        et_titt = findViewById(R.id.ed_tittle);
        et_cap = findViewById(R.id.ed_caption);
        btn_simpan = findViewById(R.id.btn_upload);
// tampilkan di activity_ubah
        et_ds.setText(dateStart);
        et_de.setText(dateEnd);
        et_loc.setText(location);
        et_titt.setText(tittle);
        et_cap.setText(caption);

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateStart = et_ds.getText().toString();
                dateEnd = et_de.getText().toString();
                location = et_loc.getText().toString();
                tittle = et_titt.getText().toString();
                caption = et_cap.getText().toString();


                updatedata();
            }
        });
    }
    private void updatedata(){
        RequestData connect = Endpoint.connecRetrofit().create(RequestData.class);
        Call<ResponseModel> updatedata = connect.updatedata(xId,dateStart, dateEnd, location, tittle, caption);

        updatedata.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();

                Toast.makeText(UbahActivity.this, "Kode : "+kode+"pesan : "+pesan, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(UbahActivity.this, "Gagal mengubungi server"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}