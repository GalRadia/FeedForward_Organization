package com.example.feedforward_association.controllers;
import com.example.feedforward_association.interfaces.Callback_Generic;
import com.example.feedforward_association.interfaces.GenericApi;

import okhttp3.ResponseBody;
import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository implements  Callback<ResponseBody> {
    private static final String TAG = "Repository";
    private static final String BASE_URL = "https://api.github.com/";
    private static final String API_KEY="SECRET";
    Callback_Generic callback_generic;
    public Repository(Callback_Generic callback_generic) {
        this.callback_generic = callback_generic;
    }
    public void fetchData(String url){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GenericApi apiInterface = retrofit.create(GenericApi.class);
        url = url.charAt(url.length()-1)=='/'?url.substring(0,url.length()-1):url;
        Call<ResponseBody> call = apiInterface.fetch(url);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if(response.isSuccessful()){
            String data = response.body().toString();
            callback_generic.onSuccess(response.body());
        }else{
            callback_generic.onError(response.message());
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
        callback_generic.onError(throwable.getMessage());
    }
}
