package com.example.complaintclose.javafiles;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("closecomplaintthird.php")
    Call<Void> sendDataArray(@Body datapostmodule arrayData);
}
