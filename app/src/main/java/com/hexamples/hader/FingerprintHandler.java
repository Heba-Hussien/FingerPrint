package com.hexamples.hader;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hexamples.hader.Activities.Home;
import com.hexamples.hader.Activities.Login;
import com.hexamples.hader.Modules.BasicResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    // You should use the CancellationSignal method whenever your app can no longer process user input, for example when your app goes
    // into the background. If you don’t use this method, then other apps will be unable to access the touch sensor, including the lockscreen!//
    ImageButton imageButton;
    private CancellationSignal cancellationSignal;
    private Context context;
    private String FingerPrintType;
    SessionManager sessionManager;
    String Late;
    int H,M;
    String formattedDate ,formattedhour;
    public FingerprintHandler(Context mContext, String fingerPrintType)
    {

        context = mContext;
        FingerPrintType=fingerPrintType;
        sessionManager=new SessionManager(context);


    }

    //Implement the startAuth method, which is responsible for starting the fingerprint authentication process//

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {

        cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    //onAuthenticationError is called when a fatal error has occurred. It provides the error code and error message as its parameters//

    public void onAuthenticationError(int errMsgId, CharSequence errString) {

        //I’m going to display the results of fingerprint authentication as a series of toasts.
        //Here, I’m creating the message that’ll be displayed if an error occurs//

        Toast.makeText(context, "Authentication error\n" + errString, Toast.LENGTH_LONG).show();
    }

    @Override

    //onAuthenticationFailed is called when the fingerprint doesn’t match with any of the fingerprints registered on the device//

    public void onAuthenticationFailed() {
        imageButton=((Activity)context).findViewById(R.id.get_finger_btn);
        imageButton.setImageResource(R.drawable.dislike2);
       Toast.makeText(context, "Wrong Fingerprint try again", Toast.LENGTH_LONG).show();
    }

    @Override

    //onAuthenticationHelp is called when a non-fatal error has occurred. This method provides additional information about the error,
    //so to provide the user with as much feedback as possible I’m incorporating this information into my toast//
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        Toast.makeText(context, "Authentication help\n" + helpString, Toast.LENGTH_LONG).show();
    }@Override

    //onAuthenticationSucceeded is called when a fingerprint has been successfully matched to one of the fingerprints stored on the user’s device//
    public void onAuthenticationSucceeded(
            FingerprintManager.AuthenticationResult result) {
        imageButton=((Activity)context).findViewById(R.id.get_finger_btn);
        imageButton.setImageResource(R.drawable.like);


        final Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(c);
        Log.e("Date",formattedDate);

        SimpleDateFormat hf = new SimpleDateFormat("HH:mm aa");
        String hour = hf.format(c);
        formattedhour="";

        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            final Date dateObj = sdf.parse(hour);
            formattedhour= new SimpleDateFormat("K:mm").format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        Log.e("Date",formattedhour);
         H= Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
         M= Calendar.getInstance().get(Calendar.MINUTE);

      //  H=8;M=2;
        // H=9;M=0;
        //H=8;M=0;
      //  H=14;M=30;
        //H=14;M=11;
        // H=12;M=55;
        if (FingerPrintType.equals("Attendance")) {
            if (!sessionManager.IsAttendance()) {
                if (H >8||(H==8&& M>0)) {
                    FingerPrintType = "Late attendance";
                    Late = (H - 8) + ":" + M;
                    sessionManager.Attendance();
                    Log.e("data", FingerPrintType + "===" + formattedDate + "===" + M + "===" + H + "===" + Late + "===" + sessionManager.getUserId() + sessionManager.IsAttendance());
                    SendData();
                }
                else if(H==8&&M==0) {
                    sessionManager.Attendance();
                    Log.e("data", FingerPrintType + "===" + formattedDate + "===" + M + "===" + H + "===" + Late + "===" + sessionManager.getUserId() + sessionManager.IsAttendance());
                  SendData();
                }

            } else {
                Toast.makeText(context, "You have already record your attendance before", Toast.LENGTH_SHORT).show();
            }
        }

        ////////////////////////////////////////////انصراف///////////////////
        else if (FingerPrintType.equals("Departure")){
            if (!sessionManager.IsDeparture()) {

                if ((H<14)||(H==14&&M<30)) {

                    FingerPrintType = "Early departure";
                    sessionManager.Departure();
                    Log.e("data", FingerPrintType + "===" + formattedDate + "===" + M + "===" + H + "===" + Late + "===" + sessionManager.getUserId() + sessionManager.IsDeparture());
                   SendData();


                } else if(H==14&&M==30) {
                    sessionManager.Departure();
                    Log.e("data", FingerPrintType + "===" + formattedDate + "===" + M + "===" + H + "===" + Late + "===" + sessionManager.getUserId() + sessionManager.IsDeparture());
                    SendData();
                }

            } else {
                Toast.makeText(context, "You have already record your Departure before", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(context, "Something is Wrong , sorry try again ", Toast.LENGTH_SHORT).show();
        }
    }

    public void SendData(){
        MyAPI myAPI = GlobalFunctions.getAppRetrofit(context).create(MyAPI.class);
        Call<BasicResponse> call = myAPI.AddFingerPrintِ(
                FingerPrintType,
                formattedDate,
                formattedhour,
                sessionManager.getUserId()

        );
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                // progressDialog.dismiss();
                if (response != null) {
                    if (response.body() != null) {

                        if(!response.body().getStatus().equals("0")) {
                            Log.e("", "Response >> " + new Gson().toJson(response.body()));
                            Toast.makeText(context,"Success!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, Home.class);
                            context. startActivity(intent);
                            ((Activity) context).finish();

                        }
                        else{
                            Toast.makeText(context,response.body().getMsg(), Toast.LENGTH_SHORT).show();
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
                // progressDialog.dismiss();

            }
        });
    }

}
