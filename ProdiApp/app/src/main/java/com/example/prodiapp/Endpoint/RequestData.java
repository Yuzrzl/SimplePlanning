package com.example.prodiapp.Endpoint;

import com.example.prodiapp.Model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RequestData {
    @GET("koneksi.php")
    Call<ResponseModel> koneksidata();

    @FormUrlEncoded
    @POST("tambah_data.php")
    Call<ResponseModel> tambahdata(
            @Field("date_start") String date_start,
            @Field("date_end") String date_end,
            @Field("location") String location,
            @Field("tittle") String tittle,
            @Field("caption") String caption

    );

    @FormUrlEncoded
    @POST("hapus_data.php")
    Call<ResponseModel> hapusdataa(
      @Field("id") int id
    );

    @FormUrlEncoded
    @POST("proses_ubah.php")
    Call<ResponseModel> ambil1data(
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("update.php")
    Call<ResponseModel> updatedata(
            @Field("id") int id,
            @Field("date_start") String date_start,
            @Field("date_end") String date_end,
            @Field("location") String location,
            @Field("tittle") String tittle,
            @Field("caption") String caption

    );
}
