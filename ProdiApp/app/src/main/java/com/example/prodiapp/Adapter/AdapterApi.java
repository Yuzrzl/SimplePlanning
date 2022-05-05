package com.example.prodiapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prodiapp.DetailActivity;
import com.example.prodiapp.Endpoint.Endpoint;
import com.example.prodiapp.Endpoint.RequestData;
import com.example.prodiapp.MainActivity;
import com.example.prodiapp.Model.DataModel;
import com.example.prodiapp.Model.ResponseModel;
import com.example.prodiapp.R;
import com.example.prodiapp.UbahActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterApi extends RecyclerView.Adapter<AdapterApi.Holderdata>{
    private Context context;
    private List<DataModel> list;
    private List<DataModel> listPlanning;
    private int id_p;

    public AdapterApi(Context context, List<DataModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Holderdata onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_planning,parent,false);
        Holderdata holderdata = new Holderdata(view);
        return holderdata;
    }

    @Override
    public void onBindViewHolder(@NonNull Holderdata holder, int position) {
        DataModel dataModel = list.get(position);

        holder.id.setText(String.valueOf(dataModel.getId()));
        holder.dateStart.setText(dataModel.getDate_start());
        holder.dateEnd.setText(dataModel.getDate_end());
        holder.location.setText(dataModel.getLocation());
        holder.tittle.setText(dataModel.getTittle());
        holder.caption.setText(dataModel.getCaption());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holderdata extends RecyclerView.ViewHolder{
        TextView id, dateStart,dateEnd,location,tittle,caption;


        public Holderdata(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.id);
            dateStart = itemView.findViewById(R.id.ed_date_start);
            dateEnd = itemView.findViewById(R.id.ed_date_end);
            location = itemView.findViewById(R.id.ed_location);
            tittle = itemView.findViewById(R.id.ed_tittle);
            caption = itemView.findViewById(R.id.ed_caption);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("tittle", tittle.getText().toString());
                    i.putExtra("start", dateStart.getText().toString());
                    i.putExtra("end", dateEnd.getText().toString());
                    i.putExtra("loc", location.getText().toString());
                    i.putExtra("cap", caption.getText().toString());
                    context.startActivity(i);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder poppesan = new AlertDialog.Builder(context);
                    poppesan.setMessage("Apakah data akan dirubah?");
                    poppesan.setTitle("Perhatian");
                    poppesan.setIcon(R.drawable.ic_warning);
                    poppesan.setCancelable(true);

                    id_p = Integer.parseInt(id.getText().toString());

                    poppesan.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            hapusdata();
                            dialog.dismiss();
                            ((MainActivity)context).getData();
                        }
                    });

                    poppesan.setNegativeButton("Ubah", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ambildata();
                            dialog.dismiss();
                        }
                    });

                    poppesan.show();

                    return false;
                }
            });
        }
        private  void hapusdata(){
            RequestData connect = Endpoint.connecRetrofit().create(RequestData.class);
            Call<ResponseModel> hapusdata = connect.hapusdataa(id_p);

            hapusdata.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();
                    Toast.makeText(context, "Kode : "+kode+"Pesan : "+pesan, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(context, "Gagal menghubungi server"+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void ambildata(){
            RequestData connect = Endpoint.connecRetrofit().create(RequestData.class);
            Call<ResponseModel> ambildata = connect.ambil1data(id_p);

            ambildata.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();

                    listPlanning = response.body().getData();
                    int idP = listPlanning.get(0).getId();
                    String dateStart = listPlanning.get(0).getDate_start();
                    String dateEnd = listPlanning.get(0).getDate_end();
                    String  location = listPlanning.get(0).getLocation();
                    String tittle = listPlanning.get(0).getTittle();
                    String caption = listPlanning.get(0).getCaption();


                    //Toast.makeText(context, "Kode : "+kode+" Pesan : "+pesan+" Data : "+idP, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent( context, UbahActivity.class);
                    intent.putExtra("xid",idP);
                    intent.putExtra("dateStart",dateStart);
                    intent.putExtra("dateEnd",dateEnd);
                    intent.putExtra("location",location);
                    intent.putExtra("tittle",tittle);
                    intent.putExtra("caption",caption);
                    context.startActivity(intent);
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(context, "Gagal menghubungi server"+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
