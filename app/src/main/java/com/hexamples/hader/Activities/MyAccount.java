package com.hexamples.hader.Activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hexamples.hader.Networking.GlobalFunctions;
import com.hexamples.hader.Modules.BasicResponse;
import com.hexamples.hader.Modules.Employee;
import com.hexamples.hader.Networking.MyAPI;
import com.hexamples.hader.R;
import com.hexamples.hader.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAccount extends AppCompatActivity {
ImageButton Finger,Archive,Notification;
Button Logout,Save;
EditText Name,Username,Password,Address,Phone,Email;
SessionManager sessionManager;
Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account);
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        sessionManager=new SessionManager(MyAccount.this);
        Name=findViewById(R.id.name);
        Password=findViewById(R.id.password);
        Username=findViewById(R.id.username);
        Address=findViewById(R.id.address);
        Phone=findViewById(R.id.phone);
        Email=findViewById(R.id.email);
        GetData();
        Finger=findViewById(R.id._1_btn);
        Finger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyAccount.this, Home.class);
                startActivity(intent);
            }
        });

        Archive=findViewById(R.id._2_btn);
        Archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyAccount.this, Archive.class);
                startActivity(intent);
            }
        });

        Notification=findViewById(R.id._3_btn);
        Notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyAccount.this, Notification.class);
                startActivity(intent);
            }
        });

        Logout=findViewById(R.id.logout);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.logout();
              //  sessionManager.ClearAllData();
                sessionManager.Remove();
                Intent intent = new Intent(MyAccount.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
        Save=findViewById(R.id.Save);
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveData();
            }
        });
    }

    public  void  GetData(){

        MyAPI myAPI = GlobalFunctions.getAppRetrofit(MyAccount.this).create(MyAPI.class);

        Call<Employee> call = myAPI.ReurnStuff(
              sessionManager.getUserId()
        );
        call.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                if (response != null) {
                    if (response.body() != null) {

                        if(!response.body().getStatus().equals("0")) {
                            Log.e("", "Response >> " + new Gson().toJson(response.body()));
                            //////////////////////////////////من هنا ///////////////////////////
                            Name.setText(response.body().getStaff_Name());
                            Username.setText(response.body().getUserName());
                            Password.setText(response.body().getPassword());
                            Phone.setText(response.body().getStaff_Phone());
                            Address.setText(response.body().getStaff_Address());
                            Email.setText(response.body().getStaff_Email());
                        }
                        else{
                            Toast.makeText(MyAccount.this,response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("", "body === null");

                    }
                } else {
                    Log.e("", "response === null");
                }
            }
            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }


 public  void SaveData(){



     MyAPI myAPI = GlobalFunctions.getAppRetrofit(MyAccount.this).create(MyAPI.class);

     Call<BasicResponse> call = myAPI.Update(
             sessionManager.getUserId(),
             Username.getText().toString(),
             Password.getText().toString(),
             Name.getText().toString(),
             Phone.getText().toString(),
             Email.getText().toString(),
            Address.getText().toString()

     );
     call.enqueue(new Callback<BasicResponse>() {
         @Override
         public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
             if (response != null) {
                 if (response.body() != null) {

                     if(!response.body().getStatus().equals("0")) {
                         Log.e("", "Response >> " + new Gson().toJson(response.body()));
                         //////////////////////////////////من هنا ///////////////////////////
                         Toast.makeText(MyAccount.this,response.body().getMsg(), Toast.LENGTH_SHORT).show();
                     }
                     else{
                         Toast.makeText(MyAccount.this,response.body().getMsg(), Toast.LENGTH_SHORT).show();
                     }
                 } else {
                     Log.e("", "body === null");

                 }
             } else {
                 Log.e("", "response === null");
             }
         }
         @Override
         public void onFailure(Call<BasicResponse> call, Throwable t) {
             t.printStackTrace();
         }
     });


 }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
