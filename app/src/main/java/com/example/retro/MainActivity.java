package com.example.retro;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retro.adapters.PhotolistAdapter;
import com.example.retro.models.RetroPhoto;
import com.example.retro.utils.NetworkState;
import com.example.retro.viewmodels.PhotoViewModel;


public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PhotolistAdapter adapter;
    PhotoViewModel photoViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        photoViewModel = ViewModelProviders.of(this).get(PhotoViewModel.class);

        recyclerView = findViewById(R.id.customRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapter = new PhotolistAdapter(this);

        photoViewModel.getArticleLiveData().observe(this, new Observer<PagedList<RetroPhoto>>() {
            @Override
            public void onChanged(PagedList<RetroPhoto> pagedList) {
                adapter.submitList(pagedList);

                return;
            }
        });

        photoViewModel.getNetworkState().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(NetworkState networkState) {
                adapter.setNetworkState(networkState);
                ;
            }
        });

        recyclerView.setAdapter(adapter);
    }
}
