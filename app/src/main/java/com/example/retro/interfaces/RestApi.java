package com.example.retro.interfaces;

import com.example.retro.models.RetroPhoto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestApi {
    @GET("/photos")
    //Call<List<Photo>> getAllPhotos();
    Call<List<RetroPhoto>> fetchPhotos
                            (@Query("") String q,
                             @Query("page") long page,
                             @Query("pageSize") int pageSize);
}
