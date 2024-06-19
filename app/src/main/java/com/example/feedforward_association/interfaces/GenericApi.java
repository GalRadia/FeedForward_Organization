package com.example.feedforward_association.interfaces;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GenericApi {
    @GET
    Call<ResponseBody>fetch(@Url String url);
}
