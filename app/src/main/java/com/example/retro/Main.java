package com.example.retro;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.retro.adapters.CustomAdapter;
import com.example.retro.interfaces.GetDataService;
import com.example.retro.models.RetroPhoto;
import com.example.retro.network.RetrofitClientInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Main extends AppCompatActivity {

    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    CustomAdapter adapter;
    List<RetroPhoto> photoList;

    boolean isLoading = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(Main.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        //Create handle for RetrofitInstance interface
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<RetroPhoto>> call = service.getAllPhotos();
        call.enqueue(new Callback<List<RetroPhoto>>() {
            @Override
            public void onResponse(Call<List<RetroPhoto>> call, Response<List<RetroPhoto>> response) {
                progressDialog.dismiss();
                generateDataList(response.body());
                Log.i("BODY", response.toString());
            }

            @Override
            public void onFailure(Call<List<RetroPhoto>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(Main.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void generateDataList(final List<RetroPhoto> photoList){
        int formerVisibleItems, visibleItemCount, totalItemCount;

        recyclerView = findViewById(R.id.customRecyclerView);
        adapter = new CustomAdapter(Main.this, photoList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Main.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == photoList.size() - 1) {
                        //bottom of list!
                        photoList.add(null);
                        adapter.notifyItemInserted(photoList.size() - 1);


                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                photoList.remove(photoList.size() - 1);
                                int scrollPosition = photoList.size();
                                adapter.notifyItemRemoved(scrollPosition);
                                int currentSize = scrollPosition;
                                int nextLimit = currentSize + 10;

                                while (currentSize - 1 < nextLimit) {
                                    //photoList.add(currentSize);
                                    currentSize++;
                                }

                                adapter.notifyDataSetChanged();
                                isLoading = false;
                            }
                        }, 2000);
                        isLoading = true;
                    }
                }
            }
        });
    }
}
