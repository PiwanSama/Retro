package com.example.retro;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.retro.adapters.UserAdapter;
import com.example.retro.databinding.ActivityMainBinding;
import com.example.retro.models.User;
import com.example.retro.viewmodels.MainActivityViewModel;


public class MainActivity extends AppCompatActivity {
    private PagedList<User> users;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MainActivityViewModel mainActivityViewModel;
    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        getUsers();

        swipeRefreshLayout = activityMainBinding.swipeLayout;
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUsers();
            }
        });
    }

    public void getUsers() {
        mainActivityViewModel.getUserPagedList().observe(this, new Observer<PagedList<User>>() {
            @Override
            public void onChanged(@Nullable PagedList<User> usersFromLiveData) {
                users = usersFromLiveData;
                showOnRecyclerView();
            }
        });
    }

    private void showOnRecyclerView() {
        RecyclerView recyclerView = activityMainBinding.rvMovies;
        UserAdapter userAdapter = new UserAdapter(this);
        userAdapter.submitList(users);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        } else {

            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(userAdapter);
        userAdapter.notifyDataSetChanged();
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}