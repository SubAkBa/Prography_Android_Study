package com.example.prography_android_study;

import retrofit2.http.GET;

public interface RetrofitInterface {
    @GET("something/")
    Call<Get_My_Data> get_data();

}
