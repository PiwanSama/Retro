package com.example.retro.factory;

import com.example.retro.MyApplication;
import com.example.retro.interfaces.UserDataService;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance{

    private static Retrofit retrofit = null;

    private static final String BASE_URL = "https://reqres.in/api/";

    private static RetrofitInstance instance;

    public static RetrofitInstance getInstance(){
        if (instance==null){
            instance = new RetrofitInstance();
        }
        return instance;
    }

    public static UserDataService getService(){
        int cacheSize = 10*1024*1024; //10MB
        Cache cache = new Cache(new File(MyApplication.getInstance().getCacheDir(), "Retro Cache"), cacheSize);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(offlineIntercepter)
                .addInterceptor(onlineInterceptor)
                .cache(cache)
                .build();

        if (retrofit == null){
             retrofit = new Retrofit
                     .Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();

        }
        return retrofit.create(UserDataService.class);
    }

    static Interceptor onlineInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response response = chain.proceed(chain.request());
            int maxAge = 60; // read from cache for 60 seconds even if there is internet connection
            return response.newBuilder()
                    .header("Cache-Control", "public, max-age=" +maxAge)
                    .removeHeader("Pragma")
                    .build();
        }
    };

    static Interceptor offlineIntercepter = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
           if (!MyApplication.hasNetwork()){
                int maxStale = 60 * 60 * 24 * 30; // Offline cache available for 30 days
                request = request.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale="+maxStale)
                        .removeHeader("Pragma")
                        .build();
           }
            return chain.proceed(request);
        }
    };
}