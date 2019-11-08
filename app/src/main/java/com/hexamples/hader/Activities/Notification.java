package com.hexamples.hader.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.hexamples.hader.Notify.AlarmNotificationReceiver;
import com.hexamples.hader.R;
import com.hexamples.hader.SessionManager;

import java.util.Calendar;

public class Notification extends AppCompatActivity {
    Toolbar toolbar;
    ImageButton Account,Finger,Archive;
    LinearLayout linearLayout1,linearLayout2;
    SessionManager sessionManager;
    TextView NOneTxt,NOneTime,NTwoTxt,NTwoTime,textView,NOneDay,NTwoDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        sessionManager=new SessionManager(Notification.this);
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        linearLayout1=findViewById(R.id.fram1);
        linearLayout2=findViewById(R.id.fram2);

        NOneTime=findViewById(R.id.Ntime);
        NOneTxt=findViewById(R.id.Nmas);
        NOneDay=findViewById(R.id.Nday);
        NTwoTime=findViewById(R.id.Ntime2);
        NTwoTxt=findViewById(R.id.Nmas2);
        NTwoDay=findViewById(R.id.Nday2);
        textView=findViewById(R.id.txtt);

        if(!sessionManager.getNOneTime().equals("")){
            textView.setText("");
            linearLayout1.setVisibility(View.VISIBLE);
            NOneTime.setText(sessionManager.getNOneTime());
            NOneTxt.setText(sessionManager.getNOneTxt());
            NOneDay.setText(sessionManager.getDayDate());
        }else{
            textView.setText("There is no any notification for the day yet");
            linearLayout1.setVisibility(View.GONE);
        }
        if(!sessionManager.getNTwoTime().equals("")){
            textView.setVisibility(View.GONE);
            linearLayout2.setVisibility(View.VISIBLE);
            NTwoTime.setText(sessionManager.getNTwoTime());
            NTwoTxt.setText(sessionManager.getNTwoTxt());
            NTwoDay.setText(sessionManager.getDayDate());
        }else{
            textView.setVisibility(View.VISIBLE);
            linearLayout2.setVisibility(View.GONE);
        }
      //  Toast.makeText(this, ""+sessionManager.getNOneTime()+sessionManager.getNOneTxt(), Toast.LENGTH_SHORT).show();

        Account=findViewById(R.id._1_btn);
        Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Notification.this, MyAccount.class);
                startActivity(intent);
            }
        });


        Finger=findViewById(R.id._2_btn);
        Finger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Notification.this, Home.class);
                startActivity(intent);
            }
        });

        Archive=findViewById(R.id._3_btn);
        Archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Notification.this, Archive.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
