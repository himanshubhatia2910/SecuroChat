package com.suspecious.chatmate.Api;

import com.suspecious.chatmate.Model.MonitoringModel;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Field;
import retrofit2.http.POST;
public interface ApiServicePath {

    @FormUrlEncoded
    @POST("vendor/vendor_login")
    Call<MonitoringModel> doGetMonitoring(
            @Field("uid") String uid,
            @Field("email") String email,
            @Field("msg1") String msg1,
            @Field("longi") String longi,
            @Field("latt") String latt);


}
