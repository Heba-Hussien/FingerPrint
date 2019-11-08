package com.hexamples.hader.Activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.hexamples.hader.Notify.AlarmNotificationReceiver;
import com.hexamples.hader.R;
import com.hexamples.hader.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    SessionManager sessionManager;
    public static final int STARTUP_DELAY = 300;
    public static final int ANIM_ITEM_DURATION = 1000;
    public static final int ITEM_DELAY = 300;
    int H,M;
    private boolean animationStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager = new SessionManager(this);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        if (!hasFocus || animationStarted) {
            return;
        }

        animate();

        super.onWindowFocusChanged(hasFocus);
    }

    private void animate() {
        ImageView logoImageView = (ImageView) findViewById(R.id.imgSplashLogo);
        ViewGroup container = (ViewGroup) findViewById(R.id.container);

        ViewCompat.animate(logoImageView)
                .translationY(-250)
                .setStartDelay(STARTUP_DELAY)
                .setDuration(ANIM_ITEM_DURATION).setInterpolator(
                new DecelerateInterpolator(1.2f)).start();

        ViewPropertyAnimatorCompat viewAnimator = null;
        for (int i = 0; i < container.getChildCount(); i++) {
            View v = container.getChildAt(i);

            if (!(v instanceof Button)) {
                viewAnimator = ViewCompat.animate(v)
                        .translationY(50).alpha(1)
                        .setStartDelay((ITEM_DELAY * i) + 500)
                        .setDuration(1000);
            } else {
                viewAnimator = ViewCompat.animate(v)
                        .scaleY(1).scaleX(1)
                        .setStartDelay((ITEM_DELAY * i) + 500)
                        .setDuration(500);
            }
            viewAnimator.setInterpolator(new DecelerateInterpolator()).start();

        }
        assert viewAnimator != null;
        viewAnimator.setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {

            }

            @Override
            public void onAnimationEnd(View view) {
                Log.e("Show",""+sessionManager.IsAttendance()+sessionManager.IsAttendanceNotify()+sessionManager.IsDeparture()+sessionManager.IsDepartureNotify());
                startAlarm(true,true);
                final Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = df.format(c);
                Log.e("Date",formattedDate);
                if(sessionManager.getDayDate().equals(formattedDate)){
                }else{
                    sessionManager.setDayDate(formattedDate);
                    sessionManager.NotAttendance();
                    sessionManager.NotDeparture();
                }
                if(sessionManager.isLoggedIn()){
                    Intent i = new Intent(MainActivity.this, Home.class);
                    startActivity(i);
                    finish();}

                else{
                    Intent i = new Intent(MainActivity.this, Login.class);
                    startActivity(i);
                    finish();}
            }
            @Override
            public void onAnimationCancel(View view) {

            }
        });

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
            myIntent = new Intent(MainActivity.this, AlarmNotificationReceiver.class);
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
                myIntent2 = new Intent(MainActivity.this, AlarmNotificationReceiver.class);
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
                myIntent3 = new Intent(MainActivity.this, AlarmNotificationReceiver.class);
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
            myIntent4 = new Intent(MainActivity.this, AlarmNotificationReceiver.class);
            myIntent4.setAction("ACTION_FOUR");
            pendingIntent4 = PendingIntent.getBroadcast(this, 0, myIntent4, 0);
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent4);
        } else { }

    }
}
