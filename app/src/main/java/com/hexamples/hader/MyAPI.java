package com.hexamples.hader;



import com.hexamples.hader.Modules.BasicResponse;
import com.hexamples.hader.Modules.Employee;
import com.hexamples.hader.Modules.EmployeeLocation;
import com.hexamples.hader.Modules.Item;
import com.hexamples.hader.Modules.Records;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by ElShafie on 3/24/2017.
 */

public interface MyAPI {


    @Headers("Content-Type: application/json")
    @GET("webservice/login.php")
    Call<BasicResponse> login(
            @Query("username") String username,
            @Query("password") String password,
            @Query("Android_ID") String Android_ID
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

    @Headers("Content-Type: application/json")
    @GET("webservice/return-staff.php")
    Call<Employee> ReurnStuff(
            @Query("staff_ID") String staff_ID
    );

    @Headers("Content-Type: application/json")
    @GET("webservice/update_staff.php")
    Call<BasicResponse> Update(
            @Query("Staff_ID") String Staff_ID,
            @Query("UserName") String UserName,
            @Query("Password") String Password,
            @Query("Staff_Name") String Staff_Name,
            @Query("Staff_Phone") String Staff_Phone,
            @Query("Staff_Email") String Staff_Email,
            @Query("Staff_Address") String Staff_Address
    );

    @Headers("Content-Type: application/json")
    @GET("webservice/returndetails.php")
    Call<Records> ReurnDetails(
            @Query("staff_ID") String staff_ID
    );

    @Headers("Content-Type: application/json")
    @GET("webservice/late-attendance.php")
    Call<List<Item>> LateAttendance(
            @Query("staff_ID") String staff_ID
    );

    @Headers("Content-Type: application/json")
    @GET("webservice/departure.php")
    Call<ArrayList<Item>> Departure(
            @Query("staff_ID") String staff_ID
    );

    @Headers("Content-Type: application/json")
    @GET("webservice/early-departure.php")
    Call<ArrayList<Item>> EarlyDeparture(
            @Query("staff_ID") String staff_ID
    );

    @Headers("Content-Type: application/json")
    @GET("webservice/attendance.php")
    Call<ArrayList<Item>> Attendance(
            @Query("staff_ID") String staff_ID
    );

}
