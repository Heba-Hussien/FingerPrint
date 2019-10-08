package com.hexamples.hader.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import com.google.gson.Gson;
import com.hexamples.hader.GlobalFunctions;
import com.hexamples.hader.MyAPI;
import com.hexamples.hader.R;
import com.hexamples.hader.Modules.BasicResponse;
import com.hexamples.hader.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    EditText Username,Password;
    TextView CreateAccount;
    SessionManager sessionManager;
    Button Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        sessionManager=new SessionManager(Login.this);
        Username=findViewById(R.id.Username);
        Password=findViewById(R.id._Password);
//        CreateAccount= findViewById(R.id.sign_txt);
//        CreateAccount.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent i = new Intent(Login.this, SignUp.class);
//                startActivity(i);
//                finish();
//            }
//        });
        Login =findViewById(R.id._login_btn);
        Login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(validate()==false){ }
                else
                {
                    MyAPI myAPI = GlobalFunctions.getAppRetrofit(Login.this).create(MyAPI.class);

                    Call<BasicResponse> call = myAPI.login(
                            Username.getText().toString(),
                            Password.getText().toString()
                    );
                    call.enqueue(new Callback<BasicResponse>() {
                        @Override
                        public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                            if (response != null) {
                                if (response.body() != null) {

                                    if(!response.body().getStatus().equals("0")) {
                                        Log.e("", "Response >> " + new Gson().toJson(response.body()));
                                        //////////////////////////////////من هنا ///////////////////////////
                                        sessionManager.setUserId(response.body().getStatus());
                                        sessionManager.setUser_name(Username.getText().toString());
                                        sessionManager.setPassword( Password.getText().toString());
                                        sessionManager.LoginSession();


                                        Intent intent = new Intent(Login.this, Home.class);
                                        intent.putExtra("user_ID", response.body().getStatus());
                                        startActivity(intent);

                                    }
                                    else{  Toast.makeText(Login.this,response.body().getMsg(), Toast.LENGTH_SHORT).show();
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
            }

        });
    }
    public boolean validate() {
        boolean valid = true;
        String name = Username.getText().toString();
        String password = Password.getText().toString();
        if (name.isEmpty()) {
            Username.setError("Required field");
            valid = false;
        } else {
            Username.setError(null);
        }

        if (password.isEmpty()) {
            Password.setError("Required field");
            valid = false;
        } else {
            Password.setError(null);
        }

        return valid;
    }

}


