package com.hexamples.hader.Notify;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


public class SampleBootReceiver extends BroadcastReceiver
{

    public void onReceive(Context context, Intent intent)
    {

        // Your code to execute when Boot Completd

        Intent i = new Intent(context,MyService.class);
        context.startService(i);

        Toast.makeText(context, "Booting Completed", Toast.LENGTH_LONG).show();

    }

}