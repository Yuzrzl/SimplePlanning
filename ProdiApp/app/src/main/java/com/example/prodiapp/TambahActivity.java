package com.example.prodiapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prodiapp.Endpoint.Endpoint;
import com.example.prodiapp.Endpoint.RequestData;
import com.example.prodiapp.Model.ResponseModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahActivity extends AppCompatActivity {
    private EditText et_ds,et_de,et_loc,et_titt,et_cap;
    private Button btn_simpan;
    private String dateStart, dateEnd, location, tittle, caption;
    Calendar calendar;
    DatePickerDialog.OnDateSetListener dateS,dateE;

    TextView dateSt,dateN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);

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
                new DatePickerDialog(TambahActivity.this, dateS,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        dateN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(TambahActivity.this, dateE,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });//endCalendarPicker

        et_ds = findViewById(R.id.ed_date_start);
        et_de = findViewById(R.id.ed_date_end);
        et_loc = findViewById(R.id.ed_location);
        et_titt = findViewById(R.id.ed_tittle);
        et_cap = findViewById(R.id.ed_caption);
        btn_simpan = findViewById(R.id.btn_upload);

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateStart = et_ds.getText().toString();
                dateEnd = et_de.getText().toString();
                location = et_loc.getText().toString();
                tittle = et_titt.getText().toString();
                caption = et_cap.getText().toString();
                tambahdata();
                Intent i  = new Intent(TambahActivity.this,MainActivity.class);
                startActivity(i);
            }
        });

    }
    private void tambahdata(){
        RequestData connect = Endpoint.connecRetrofit().create(RequestData.class);
        Call<ResponseModel> simpandata = connect.tambahdata(dateStart, dateEnd, location, tittle, caption);

        simpandata.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();

                Toast.makeText(TambahActivity.this, "Kode : "+kode+"pesan : "+pesan, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                //Toast.makeText(TambahActivity.this, "Gagal mengubungi server"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}