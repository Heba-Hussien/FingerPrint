package com.hexamples.hader.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.hexamples.hader.Networking.GlobalFunctions;
import com.hexamples.hader.Modules.Records;
import com.hexamples.hader.Networking.MyAPI;
import com.hexamples.hader.R;
import com.hexamples.hader.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Archive extends AppCompatActivity {
     Toolbar toolbar;
     Button button1,button2,button3,button4;
     Button btn1,btn2;
     TextView textView1,textView2,textView3,textView4;
     ImageButton Account,Notification,Finger;
     Intent intent;
     SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.archive);
        intent=new Intent(Archive.this,Details.class);
        sessionManager=new SessionManager(Archive.this);
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        textView1=findViewById(R.id.txt1);
        textView2=findViewById(R.id.txt2);
        textView3=findViewById(R.id.txt3);
        textView4=findViewById(R.id.txt4);
        GetData();
        Account=findViewById(R.id._1_btn);
        Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Archive.this, MyAccount.class);
                startActivity(intent);
            }
        });

        Finger=findViewById(R.id._2_btn);
        Finger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Archive.this, Home.class);
                startActivity(intent);
            }
        });

        Notification=findViewById(R.id._3_btn);
        Notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Archive.this, Notification.class);
                startActivity(intent);
            }
        });


        button1=findViewById(R.id.btn1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("Title","Attendance");
                intent.putExtra("kind","1");
                startActivity(intent);
            }
        });

        button2=findViewById(R.id.btn2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("Title","Departure");
                intent.putExtra("kind","2");
                startActivity(intent);

            }
        });

        button3=findViewById(R.id.btn3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("Title","Late");
                intent.putExtra("kind","3");
                startActivity(intent);


            }
        });

        button4=findViewById(R.id.btn4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("Title","Left Early");
                intent.putExtra("kind","4");
                startActivity(intent);


            }
        });




        btn1=findViewById(R.id.btn1_1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Archive.this,VacationRequest.class);
                startActivity(i);

            }
        });

        btn2=findViewById(R.id.btn1_2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Archive.this,SubmitAbsentExcuse.class);
                startActivity(i);

            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            btn2.setEnabled(false);
            btn1.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        } else {
            btn1.setEnabled(true);
            btn2.setEnabled(true);
        }

    }

    public void  GetData()
    {

        MyAPI myAPI = GlobalFunctions.getAppRetrofit(Archive.this).create(MyAPI.class);

        Call<Records> call = myAPI.ReurnDetails(
           sessionManager.getUserId()
        );
        call.enqueue(new Callback<Records>() {
            @Override
            public void onResponse(Call<Records> call, Response<Records> response) {
                if (response != null) {
                    if (response.body() != null) {

                        if(!response.body().getStatus().equals("0")) {
                            Log.e("", "Response >> " + new Gson().toJson(response.body()));
                            //////////////////////////////////من هنا ///////////////////////////
                            textView1.setText(response.body().getAttendance());
                            textView2.setText(response.body().getDeparture());
                            textView3.setText(response.body().getLate());
                            textView4.setText(response.body().getEarly());
                        }
                        else{  Toast.makeText(Archive.this,"Sorry There are a problem ,please try again later", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("", "body === null");

                    }
                } else {
                    Log.e("", "response === null");


                }
            }
            @Override
            public void onFailure(Call<Records> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                btn1.setEnabled(true);
                btn2.setEnabled(true);

            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
