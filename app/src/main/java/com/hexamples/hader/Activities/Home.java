package com.hexamples.hader.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.akhgupta.easylocation.EasyLocationAppCompatActivity;
import com.akhgupta.easylocation.EasyLocationRequest;
import com.akhgupta.easylocation.EasyLocationRequestBuilder;
import com.google.android.gms.location.LocationRequest;
import com.google.gson.Gson;
import com.hexamples.hader.GlobalFunctions;
import com.hexamples.hader.Modules.EmployeeLocation;
import com.hexamples.hader.MyAPI;
import com.hexamples.hader.R;
import com.hexamples.hader.SessionManager;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends EasyLocationAppCompatActivity {
Button Attendance,Departure;
ImageButton Account,Archive,Notification;
float distanceInMeters;
ProgressDialog dialog;
SessionManager sessionManager;
private Location targetLocation;
Intent intent;
    int H,M;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        sessionManager=new SessionManager(Home.this);
        targetLocation = new Location("");
        intent = new Intent(Home.this, FingerPrint.class);

        H= Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        M= Calendar.getInstance().get(Calendar.MINUTE);
     //  H=8;M=2;
      // H=9;M=0;
      // H=8;M=0;
       //H=14;M=30;
       //H=14;M=11;
      // H=12;M=55;
       Attendance=findViewById(R.id.attendance_btn);
        Attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(H<8||(H==14&&M>30)||(H>=15)){
                    Toast.makeText(Home.this, "this is not work time", Toast.LENGTH_SHORT).show();
                }else{
               requestUserLocation();
                intent.putExtra("FingrtPrintKind","Attendance");
                //startActivity(intent);
                }

            }
        });


        Departure=findViewById(R.id.deperture_btn);
        Departure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(H<8||(H==14&&M>30)||(H>=15)){
                    Toast.makeText(Home.this, "this is not work time", Toast.LENGTH_SHORT).show();
                }else{
                requestUserLocation();
                intent.putExtra("FingrtPrintKind","Departure");
                   // startActivity(intent);
                }


            }
        });
        if (!sessionManager.IsAttendance()){

        Departure.setVisibility(View.INVISIBLE);
        }


        Account=findViewById(R.id._1_btn);
        Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Home.this, MyAccount.class);
                startActivity(intent);
            }
        });


        Archive=findViewById(R.id._2_btn);
        Archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Home.this, Archive.class);
                startActivity(intent);
            }
        });

        Notification=findViewById(R.id._3_btn);
        Notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Home.this, Notification.class);
                startActivity(intent);
            }
        });


    }

    void requestUserLocation() {
        LocationRequest locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(5000)
                .setFastestInterval(5000);
        EasyLocationRequest easyLocationRequest = new EasyLocationRequestBuilder()
                .setLocationRequest(locationRequest)
                .setFallBackToLastLocationTime(3000)
                .build();
        requestSingleLocationFix(easyLocationRequest);

    }


    @Override
    public void onLocationPermissionGranted() {

    }

    @Override
    public void onLocationPermissionDenied() {

    }

    @Override
    public void onLocationReceived(final Location location) {
        dialog = new ProgressDialog(Home.this);
   if (location != null) {

            // Toast.makeText(this, location.getLatitude() + " , " + location.getLongitude(), Toast.LENGTH_SHORT).show();
       dialog.setMessage("detect your location ,please wait...");
       dialog.show();

       MyAPI myAPI = GlobalFunctions.getAppRetrofit(Home.this).create(MyAPI.class);

       Call<EmployeeLocation> call = myAPI.ReurnLocation(
              sessionManager.getUserId()

       );
       call.enqueue(new Callback<EmployeeLocation>() {
           @Override
           public void onResponse(Call<EmployeeLocation> call, Response<EmployeeLocation> response) {
               if (response != null) {
                   if (response.body() != null) {

                       if(!response.body().getStatus().equals("0")) {
                           Log.e("", "Response >> " + new Gson().toJson(response.body()));
                           targetLocation.setLatitude(Double.parseDouble(response.body().getBuilding_lat()));//your coords of course
                           targetLocation.setLongitude(Double.parseDouble(response.body().getBuilding_lan()));
                           distanceInMeters =  targetLocation.distanceTo(location);
                           dialog.dismiss();
                           if(distanceInMeters<=200){
                               startActivity(intent);
                           }
                           else
                           {  Toast.makeText(Home.this, "You are so far from work now please try after some time", Toast.LENGTH_SHORT).show();}
                       }
                       else{
                           dialog.dismiss();
                           Toast.makeText(Home.this,response.body().getMsg(), Toast.LENGTH_SHORT).show(); }
                   } else {
                       dialog.dismiss();
                       Log.e("", "body === null");

                   }
               } else {
                   dialog.dismiss();
                   Log.e("", "response === null");
               }
           }

           @Override
           public void onFailure(Call<EmployeeLocation> call, Throwable t) {
               t.printStackTrace();
               dialog.dismiss();

           }
       });
    }
    }

    @Override
    public void onLocationProviderEnabled() {

    }

    @Override
    public void onLocationProviderDisabled() {

    }

}
