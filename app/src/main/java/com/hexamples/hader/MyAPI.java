package com.hexamples.hader;



import com.hexamples.hader.Modules.BasicResponse;
import com.hexamples.hader.Modules.Category;
import com.hexamples.hader.Modules.EmployeeLocation;
import com.hexamples.hader.Modules.Group;
import com.hexamples.hader.Modules.Item;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by ElShafie on 3/24/2017.
 */

public interface MyAPI {


    @Headers("Content-Type: application/json")
    @GET("webservice/login.php")
    Call<BasicResponse> login(
            @Query("username") String username,
            @Query("password") String password
    );


    @Headers("Content-Type: application/json")
    @GET("webservice/return-building.php")
    Call<EmployeeLocation> ReurnLocation(
            @Query("staff_ID") String staff_ID
    );



    @Headers("Content-Type: application/json")
    @GET("webservice/addpresent.php")
    Call<BasicResponse> AddFingerPrintِ(
            @Query("Status") String Status,
            @Query("Day") String Day,
            @Query("Time") String Time,
            @Query("Staff_ID") String Staff_ID
    );



}
