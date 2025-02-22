package com.hexamples.hader.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.gson.Gson;
import com.hexamples.hader.Networking.GlobalFunctions;
import com.hexamples.hader.Networking.MyAPI;
import com.hexamples.hader.R;
import com.hexamples.hader.Modules.BasicResponse;
import com.hexamples.hader.SessionManager;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    EditText Username,Password;
    TextView CreateAccount;
    SessionManager sessionManager;
    Button Login;
    String deviceId;
    String subscriberId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        sessionManager=new SessionManager(Login.this);
        Username=findViewById(R.id.Username);
        Password=findViewById(R.id._Password);
        GetAndroidId();

        // Toast.makeText(this, deviceId+subscriberId, Toast.LENGTH_SHORT).show();
        Login =findViewById(R.id._login_btn);
        Login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(validate()==false){ }
                else
                {
                    MyAPI myAPI = GlobalFunctions.getAppRetrofit(Login.this).create(MyAPI.class);
                    Call<BasicResponse> call = myAPI.login(
                            Username.getText().toString(),
                            Password.getText().toString(),
                            deviceId + subscriberId
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

    public void GetAndroidId() {
        final String PREFS_NAME = "GetAndroidId_MyPrefsFile";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getBoolean("my_first_time", true)) {
            Log.e("Comments", "First time");
            String uuid = UUID.randomUUID().toString();
            settings.edit().putString("uuid", uuid).commit();
            settings.edit().putBoolean("my_first_time", false).commit();
        }
        String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        String uuid = settings.getString("uuid", UUID.randomUUID().toString());
        deviceId = android_id + "_" + uuid;

        Log.e("deviceId", "" + deviceId);

    }


}


