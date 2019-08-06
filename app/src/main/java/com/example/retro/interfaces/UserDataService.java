package com.example.retro.interfaces;

import com.example.retro.models.UserDBResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserDataService {
    @GET("users")
    Call<UserDBResponse> getUsers();

    @GET("users")
    Call<UserDBResponse> getUsersWithPaging(@Query("page") long page);
}