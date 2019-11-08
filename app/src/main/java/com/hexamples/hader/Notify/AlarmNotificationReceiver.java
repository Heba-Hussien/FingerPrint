package com.hexamples.hader.Notify;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.hexamples.hader.Activities.Home;
import com.hexamples.hader.Activities.MainActivity;
import com.hexamples.hader.Modules.BasicResponse;
import com.hexamples.hader.Networking.GlobalFunctions;
import com.hexamples.hader.Networking.MyAPI;
import com.hexamples.hader.R;
import com.hexamples.hader.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.PendingIntent.FLAG_ONE_SHOT;


public class AlarmNotificationReceiver extends BroadcastReceiver {
    Context contextt;
    String formattedDate;
    int M ,H;
    SessionManager sessionManager;
    TextView textView;
    @Override
    public void onReceive(Context context, Intent intent) {
        contextt=context;
        sessionManager=new SessionManager(contextt);
        H= Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        M= Calendar.getInstance().get(Calendar.MINUTE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Intent myIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                myIntent,
                FLAG_ONE_SHOT );
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Hader")
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentInfo("Info");
        if (intent.getAction().equalsIgnoreCase("ACTION_ONE")) {
            if(!sessionManager.IsAttendance()&&!sessionManager.IsAttendanceNotify()) {
                builder.setContentText(" Attendance time is Coming Soon");
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1, builder.build());
                sessionManager.AttendanceNotify();
                sessionManager.setNOneTime(H+":"+M);
                sessionManager.setNOneTxt("Attendance time is Coming Soon");

            }
            else{}
        } else if(intent.getAction().equalsIgnoreCase("ACTION_TWO")){
            if(!sessionManager.IsDeparture()&&!sessionManager.IsDepartureNotify()&&sessionManager.IsAttendance()){
            builder.setContentText(" Departure time is Coming Soon");
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1,builder.build());
            sessionManager.DepartureNotify();
                sessionManager.setNTwoTime(H+":"+M);
                sessionManager.setNTwoTxt(" Departure time is Coming Soon");

            } else{}
        }else if(intent.getAction().equalsIgnoreCase("ACTION_THREE"))
            {
            if(!sessionManager.IsAttendance()){
                SendData();
                sessionManager.Attendance();
                sessionManager.Departure();
                builder.setContentText("You Recorded as Absent Today");
                NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1,builder.build());
                sessionManager.setNTwoTime(H+":"+M);
                sessionManager.setNTwoTxt("You Recorded as Absent Today");

            }else{

            }
        }
        else if(intent.getAction().equalsIgnoreCase("ACTION_FOUR")){
             sessionManager.NotAttendanceNotify();
             sessionManager.NotDepartureNotify();
             sessionManager.Departure();
        }

        Log.e("Show2",""+sessionManager.IsAttendance()+sessionManager.IsAttendanceNotify()+sessionManager.IsDeparture()+sessionManager.IsDepartureNotify());


    }

    public void SendData(){
        final Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(c);
        Log.e("Date",formattedDate);
        MyAPI myAPI = GlobalFunctions.getAppRetrofit(contextt).create(MyAPI.class);
        Call<BasicResponse> call = myAPI.AddFingerPrintِ(
                "Absent",
                formattedDate,
                "0",
                sessionManager.getUserId(),
                "1",
                "0",
                "0"
        );
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (response != null) {
                    if (response.body() != null) {
                        if(!response.body().getStatus().equals("0")) {
                         //   Toast.makeText(contextt,"Success!", Toast.LENGTH_SHORT).show();
                              sessionManager.Attendance();
                              sessionManager.Departure();
                            Log.e("", "Response >> " + new Gson().toJson(response.body())+sessionManager.IsAttendance());
                        } else{ Toast.makeText(contextt,response.body().getMsg(), Toast.LENGTH_SHORT).show(); }
                    } else { Log.e("", "body === null"); }
                } else { Log.e("", "response === null"); }
            }
            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}