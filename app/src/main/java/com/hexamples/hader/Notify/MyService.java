package com.hexamples.hader.Notify;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.hexamples.hader.Activities.MainActivity;
import com.hexamples.hader.SessionManager;

import java.util.Calendar;

public class MyService extends Service {
    SessionManager sessionManager;
    int H,M;
    public MyService() {
        sessionManager=new SessionManager(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            startAlarm(true, true);
        }catch (Exception e){
            Log.e("Exep",e.getMessage());
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }
    private void startAlarm(boolean isNotification, boolean isRepeat ) {
        H= Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        M= Calendar.getInstance().get(Calendar.MINUTE);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        //////////////////////////////////////////
        if (!sessionManager.IsAttendance()&&!sessionManager.IsAttendanceNotify()&&H==7) {
            Log.e("Show3",""+sessionManager.IsAttendance()+sessionManager.IsAttendanceNotify()+sessionManager.IsDeparture()+sessionManager.IsDepartureNotify());
            Intent myIntent;
            PendingIntent pendingIntent;
            calendar.set(Calendar.HOUR_OF_DAY, 7);
            calendar.set(Calendar.MINUTE,45);
            calendar.set(Calendar.SECOND, 0);
            myIntent = new Intent(this, AlarmNotificationReceiver.class);
            // Toast.makeText(this, "1" +sessionManager.IsAttendance()+sessionManager.IsAttendanceNotify(), Toast.LENGTH_SHORT).show();
            myIntent.setAction("ACTION_ONE");
            pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0);
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        } else { }
        ///////////////////////////////////
        //sessionManager.Attendance();
        if (!sessionManager.IsDeparture()&&!sessionManager.IsDepartureNotify()&&sessionManager.IsAttendance()&&H==14) {
            Log.e("Show4",""+sessionManager.IsAttendance()+sessionManager.IsAttendanceNotify()+sessionManager.IsDeparture()+sessionManager.IsDepartureNotify());
            Intent myIntent2;
            PendingIntent pendingIntent2;
            calendar.set(Calendar.HOUR_OF_DAY,14);
            calendar.set(Calendar.MINUTE, 15);
            calendar.set(Calendar.SECOND, 0);
            myIntent2 = new Intent(this, AlarmNotificationReceiver.class);
            myIntent2.setAction("ACTION_TWO");
            pendingIntent2 = PendingIntent.getBroadcast(this, 0, myIntent2, 0);
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent2);
        } else { }
        ////////////////////////////////////
        if (!sessionManager.IsAttendance()&&sessionManager.IsAttendanceNotify()&&H==15) {
            Log.e("Show5",""+sessionManager.IsAttendance()+sessionManager.IsAttendanceNotify()+sessionManager.IsDeparture()+sessionManager.IsDepartureNotify());
            Intent myIntent3;
            PendingIntent pendingIntent3;
            calendar.set(Calendar.HOUR_OF_DAY, 15);
            calendar.set(Calendar.MINUTE, 00);
            calendar.set(Calendar.SECOND, 0);
            myIntent3 = new Intent(this, AlarmNotificationReceiver.class);
            myIntent3.setAction("ACTION_THREE");
            pendingIntent3 = PendingIntent.getBroadcast(this, 0, myIntent3, 0);
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent3);
        } else { }
        ////////////////////////////////////
        if (sessionManager.IsAttendance()&&H==15) {
            Log.e("Show6",""+sessionManager.IsAttendance()+sessionManager.IsAttendanceNotify()+sessionManager.IsDeparture()+sessionManager.IsDepartureNotify());
            Intent myIntent4;
            PendingIntent pendingIntent4;
            calendar.set(Calendar.HOUR_OF_DAY, 15);
            calendar.set(Calendar.MINUTE, 30);
            calendar.set(Calendar.SECOND, 0);
            myIntent4 = new Intent(this, AlarmNotificationReceiver.class);
            myIntent4.setAction("ACTION_FOUR");
            pendingIntent4 = PendingIntent.getBroadcast(this, 0, myIntent4, 0);
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent4);
        } else { }

    }}