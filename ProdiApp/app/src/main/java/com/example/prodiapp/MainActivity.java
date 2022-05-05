package com.example.prodiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.prodiapp.Adapter.AdapterApi;
import com.example.prodiapp.Endpoint.Endpoint;
import com.example.prodiapp.Endpoint.RequestData;
import com.example.prodiapp.Model.DataModel;
import com.example.prodiapp.Model.ResponseModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<DataModel> list;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private Button btnTambah;

    TextView namaAkun, namaEmail, namaAlamat, tglUltah;
    ImageView uimage;

    DatabaseReference dbreference;
    StorageReference storageReference;

    Uri filepath;
    Bitmap bitmap;
    String UserID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        namaAkun =findViewById(R.id.nama_akun);
        namaEmail =findViewById(R.id.nama_email);
        namaAlamat =findViewById(R.id.nama_alamat);
        tglUltah =findViewById(R.id.tgl_ultah);
        uimage =findViewById(R.id.img_profile);


        dbreference= FirebaseDatabase.getInstance().getReference().child("userprofile");
        storageReference= FirebaseStorage.getInstance().getReference();

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        UserID=user.getUid();
        dbreference.child(UserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    namaAkun.setText(snapshot.child("uname").getValue().toString());
                    namaEmail.setText(snapshot.child("email").getValue().toString());
                    namaAlamat.setText(snapshot.child("alamat").getValue().toString());
                    tglUltah.setText(snapshot.child("ultah").getValue().toString());
                    Glide.with(getApplicationContext()).load(snapshot.child("uimage").getValue().toString()).into(uimage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //ke EditAccount
        FloatingActionButton edit = findViewById(R.id.idBtnEdit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,EditAccountActivity.class);
                startActivity(i);
            }
        });
//recyclerview
        recyclerView = findViewById(R.id.rv_list);
        swipeRefreshLayout = findViewById(R.id.swiperfsh);
        progressBar = findViewById(R.id.progrebr);
        btnTambah = findViewById(R.id.btnTambah);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        getData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
              swipeRefreshLayout.setRefreshing(true);
              getData();
              swipeRefreshLayout.setRefreshing(false);
            }
        });

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TambahActivity.class));
            }
        });

    }


    public void getData() {
        progressBar.setVisibility(View.VISIBLE);

        RequestData connect = Endpoint.connecRetrofit().create(RequestData.class);
        Call<ResponseModel> view = connect.koneksidata();

        view.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();

                //Toast.makeText(MainActivity.this, "Berhasil : " + kode + "Pesan : " + pesan, Toast.LENGTH_LONG).show();

                list = response.body().getData();

                adapter = new AdapterApi(MainActivity.this, list);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Gagal terhubung", Toast.LENGTH_LONG).show();

                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
