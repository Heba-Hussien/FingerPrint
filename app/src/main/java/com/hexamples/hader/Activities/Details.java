package com.hexamples.hader.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.hexamples.hader.Networking.GlobalFunctions;
import com.hexamples.hader.Modules.Item;
import com.hexamples.hader.Networking.MyAPI;
import com.hexamples.hader.R;
import com.hexamples.hader.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Details extends AppCompatActivity {
    Toolbar toolbar;
    TextView textView;
    String kind;
    ProgressDialog dialog;
    private ListView listView;
    String[] dataItems;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        sessionManager = new SessionManager(Details.this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        textView = findViewById(R.id.title);
        textView.setText(getIntent().getStringExtra("Title"));
        kind = getIntent().getStringExtra("kind");
        dialog = new ProgressDialog(Details.this);
        listView = (ListView) findViewById(R.id.list);

        if (kind.equals("1")) {
            dialog.show();
            MyAPI myAPI = GlobalFunctions.getAppRetrofit(Details.this).create(MyAPI.class);
            Call<ArrayList<Item>> call = myAPI.Attendance(
                   sessionManager.getUserId()
            );
            call.enqueue(new Callback<ArrayList<Item>>() {
                @Override
                public void onResponse(Call<ArrayList<Item>> call, Response<ArrayList<Item>> response) {
                    dialog.dismiss();
                    if (response != null) {
                        if (response.body() != null) {
                            Log.e("", "Response >> " + new Gson().toJson(response.body()));
                            dataItems= new String[response.body().size()];
                            for (int i=0;i<response.body().size();i++){
                                String txt=response.body().get(i).getDay()+" at "+response.body().get(i).getTime();
                                dataItems[i]=txt;
                            }
                            ArrayAdapter<String> itemsAdapter =
                                    new ArrayAdapter<String>(Details.this, android.R.layout.simple_list_item_1,dataItems);


                            // dataItems = response.body();
                            //  adapter = new user_borrowed_items_Adapters(dataItems, User_borrowed_items.this,user_ID);
                            listView.setAdapter(itemsAdapter);

                        } else {
                            Log.e("", "body === null");
                            Toast.makeText(Details.this, "There is no Data to show", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("", "response === null");
                    }
                }


                @Override
                public void onFailure(Call<ArrayList<Item>> call, Throwable t) {
                    t.printStackTrace();
                    dialog.dismiss();
                    Toast.makeText(Details.this, "There is no Data to show", Toast.LENGTH_SHORT).show();

                }


            });


        } else if (kind.equals("2")) {

            dialog.show();
            MyAPI myAPI = GlobalFunctions.getAppRetrofit(Details.this).create(MyAPI.class);
            Call<ArrayList<Item>> call = myAPI.Departure(
                    sessionManager.getUserId()
            );
            call.enqueue(new Callback<ArrayList<Item>>() {
                @Override
                public void onResponse(Call<ArrayList<Item>> call, Response<ArrayList<Item>> response) {
                    dialog.dismiss();
                    if (response != null) {
                        if (response.body() != null) {
                            Log.e("", "Response >> " + new Gson().toJson(response.body()));
                            dataItems= new String[response.body().size()];
                            for (int i=0;i<response.body().size();i++){
                                String txt=response.body().get(i).getDay()+" at "+response.body().get(i).getTime();
                                dataItems[i]=txt;
                            }
                            ArrayAdapter<String> itemsAdapter =
                                    new ArrayAdapter<String>(Details.this, android.R.layout.simple_list_item_1,dataItems);


                            // dataItems = response.body();
                            //  adapter = new user_borrowed_items_Adapters(dataItems, User_borrowed_items.this,user_ID);
                            listView.setAdapter(itemsAdapter);
                        } else {
                            Log.e("", "body === null");
                            Toast.makeText(Details.this, "There is no Data to show", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("", "response === null");
                    }
                }


                @Override
                public void onFailure(Call<ArrayList<Item>> call, Throwable t) {
                    t.printStackTrace();
                    dialog.dismiss();
                    Toast.makeText(Details.this, "There is no Data to show", Toast.LENGTH_SHORT).show();

                }


            });

        } else if (kind.equals("3")) {
            dialog.show();
            MyAPI myAPI = GlobalFunctions.getAppRetrofit(Details.this).create(MyAPI.class);
            Call<List<Item>> call = myAPI.LateAttendance(
                    sessionManager.getUserId()
            );
            call.enqueue(new Callback<List<Item>>() {
                @Override
                public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                    Log.e("onResponse", "Response >> " + new Gson().toJson(response));
                    dialog.dismiss();
                    if (response != null) {
                        if (response.body() != null) {
                            Log.e("", "Response >> " + new Gson().toJson(response.body()));
                            dataItems= new String[response.body().size()];
                            for (int i=0;i<response.body().size();i++){
                                String txt=response.body().get(i).getDay()+" at "+response.body().get(i).getTime();
                                dataItems[i]=txt;
                            }
                            ArrayAdapter<String> itemsAdapter =
                                    new ArrayAdapter<String>(Details.this, android.R.layout.simple_list_item_1,dataItems);


                           // dataItems = response.body();
                            //  adapter = new user_borrowed_items_Adapters(dataItems, User_borrowed_items.this,user_ID);
                             listView.setAdapter(itemsAdapter);

                        } else {
                            Log.e("", "body === null");
                            Toast.makeText(Details.this, "There is no Data to showwwwwwwww", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("", "response === null");
                    }
                }


                @Override
                public void onFailure(Call<List<Item>> call, Throwable t) {
                    t.printStackTrace();
                    dialog.dismiss();
                    Log.e("error", t.getMessage());
                    Toast.makeText(Details.this, "There is no Data to show", Toast.LENGTH_SHORT).show();

                }


            });
        } else if (kind.equals("4")) {
            dialog.show();
            MyAPI myAPI = GlobalFunctions.getAppRetrofit(Details.this).create(MyAPI.class);
            Call<ArrayList<Item>> call = myAPI.EarlyDeparture(
                    sessionManager.getUserId()
            );
            call.enqueue(new Callback<ArrayList<Item>>() {
                @Override
                public void onResponse(Call<ArrayList<Item>> call, Response<ArrayList<Item>> response) {
                    dialog.dismiss();
                    if (response != null) {
                        if (response.body() != null) {
                            Log.e("", "Response >> " + new Gson().toJson(response.body()));
                            dataItems= new String[response.body().size()];
                            for (int i=0;i<response.body().size();i++){
                                String txt=response.body().get(i).getDay()+" at "+response.body().get(i).getTime();
                                dataItems[i]=txt;
                            }
                            ArrayAdapter<String> itemsAdapter =
                                    new ArrayAdapter<String>(Details.this, android.R.layout.simple_list_item_1,dataItems);


                            // dataItems = response.body();
                            //  adapter = new user_borrowed_items_Adapters(dataItems, User_borrowed_items.this,user_ID);
                            listView.setAdapter(itemsAdapter);

                        } else {
                            Log.e("", "body === null");
                            Toast.makeText(Details.this, "There is no Data to show", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("", "response === null");
                    }
                }


                @Override
                public void onFailure(Call<ArrayList<Item>> call, Throwable t) {
                    t.printStackTrace();
                    dialog.dismiss();
                    Toast.makeText(Details.this, "There is no Data to show", Toast.LENGTH_SHORT).show();

                }


            });

        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
