package com.hexamples.hader.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.hexamples.hader.R;
import com.hexamples.hader.SessionManager;

public class MyAccount extends AppCompatActivity {
ImageButton Finger;
Button Logout;
SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account);
        sessionManager=new SessionManager(MyAccount.this);
        Finger=findViewById(R.id._4_btn);
        Finger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyAccount.this, FingerPrint.class);
                startActivity(intent);
            }
        });

        Logout=findViewById(R.id.logout);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.logout();
                sessionManager.ClearAllData();
                Intent intent = new Intent(MyAccount.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
