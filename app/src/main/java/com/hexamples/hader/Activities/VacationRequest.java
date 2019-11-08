package com.hexamples.hader.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hexamples.hader.Modules.BasicResponse;
import com.hexamples.hader.Networking.GlobalFunctions;
import com.hexamples.hader.Networking.MyAPI;
import com.hexamples.hader.R;
import com.hexamples.hader.SessionManager;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VacationRequest extends AppCompatActivity {
    Toolbar toolbar;;
    EditText Subject,Details;
    TextView Start,End,FileName;
    ImageButton imageButton;
    Button button;
    SessionManager sessionManager;
    private int year, month, day;
    private Calendar calendar;
    /////////////////////file////////////////
    private int pageNumber = 0;
    private String pdfFileName;
    public ProgressDialog pDialog;
    public static final int FILE_PICKER_REQUEST_CODE = 1;
    private String pdfPath;
    String FileURI,datetxt;
    int y;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vacation_reques);
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        datetxt = "";
        Subject=findViewById(R.id.edit_subject2);
        Details=findViewById(R.id.edit_details_2);
        Start=findViewById(R.id.txt_start2);
        End=findViewById(R.id.txt_end2);
        FileName=findViewById(R.id.file_name2);
        sessionManager=new SessionManager(VacationRequest.this);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        imageButton=findViewById(R.id.upload2);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchPicker();
            }
        });

        button=findViewById(R.id.btn_vacation);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadPDFFile();
            }
        });
        initDialog();

    }

    private void launchPicker() {
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(FILE_PICKER_REQUEST_CODE)
                .withHiddenFiles(true)
                .withFilter(Pattern.compile(".*\\.pdf$"))
                .withTitle("Select PDF file")
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            String path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            File file = new File(path);
            displayFromFile(file);
            if (path != null) {
                Log.d("Path: ", path);
                pdfPath = path;
                Toast.makeText(this, "Picked file: " + path, Toast.LENGTH_LONG).show();
            }
        }

    }

    private void displayFromFile(File file) {

        Uri uri = Uri.fromFile(new File(file.getAbsolutePath()));
        pdfFileName = getFileName(uri);


    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }




    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            //Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public void UploadPDFFile() {
        if(!Validation()){}
        else{

            if (pdfPath == null) {
                Toast.makeText(this, "please select an file ", Toast.LENGTH_LONG).show();
                Log.e("test",pdfPath+"");
                return;
            } else {
                showpDialog();
                RequestBody reqFile = null;
                reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), pdfPath);
                MultipartBody.Part imageFileBody = MultipartBody.Part.createFormData("image", pdfPath, reqFile);

                MyAPI myAPI = GlobalFunctions.getAppRetrofit(VacationRequest.this).create(MyAPI.class);
                Call<BasicResponse> call = myAPI.upload_vacation(imageFileBody);
                call.enqueue(new Callback<BasicResponse>() {
                    @Override
                    public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                        if (response != null) {
                            Log.e("BBB", "response >>> " + new Gson().toJson(response));
                            if(response.body().getStatus().equals("1")){
                                hidepDialog();
                                FileURI=response.body().getMsg();
                                BasicResponse serverResponse = response.body();
                                Toast.makeText(getApplicationContext(), serverResponse.getMsg(), Toast.LENGTH_SHORT).show();
                                SendData();
                            }
                            if (response.body() != null) {
                                Log.e("xxx", "UploadUserImage ===== STATUS ===== ");

                            } else {
                                Log.e("xxx", " A222");

                            }
                        } else {
                            Log.e("xxx",  " A3333");

                        }
                    }

                    @Override
                    public void onFailure(Call<BasicResponse> call, Throwable t) {
                        t.printStackTrace();
                        hidepDialog();
                    }
                });
            }}
    }


    public void SendData(){
        MyAPI myAPI = GlobalFunctions.getAppRetrofit(VacationRequest.this).create(MyAPI.class);
        Log.e("Data",  sessionManager.getUserId()+"  "+
                Start.getText().toString()+"  "+
                End.getText().toString()+"  "+
                Subject.getText().toString()+"  "+
                FileURI+" "+
                Details.getText().toString());
        Call<BasicResponse> call = myAPI.Vacation(
              // "1","1","ppp","oo","uuui","oo","uu"
                sessionManager.getUserId(),
                "1",
                FileURI,
                Start.getText().toString(),
                End.getText().toString(),
                Subject.getText().toString(),
                Details.getText().toString()

                          );
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                // progressDialog.dismiss();
                if (response != null) {
                    if (response.body() != null) {
                        Log.e("result", "Response >> " + new Gson().toJson(response.body()));
                        if(!response.body().getStatus().equals("0")) {
                            Toast.makeText(VacationRequest.this,"Success!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(VacationRequest.this,response.body().getMsg(), Toast.LENGTH_SHORT).show();
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
    public boolean Validation(){
        boolean x;
        x = true;
        if(Subject.getText().toString().equals("")){
            Subject.setError("Required field");
            x=false;
        }
        return x;
    }

    protected void initDialog() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.msg_loading));
        pDialog.setCancelable(true);
    }

    protected void showpDialog() {

        if (!pDialog.isShowing()) pDialog.show();
    }

    protected void hidepDialog() {

        if (pDialog.isShowing()) pDialog.dismiss();
    }



    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        y=1;
        showDialog(999);

    }

    @SuppressWarnings("deprecation")
    public void setDateTwo(View view) {
        y=2;
        showDialog(999);

    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }



    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2 + 1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {

         Toast.makeText(this, day+"/"+month+"/"+year, Toast.LENGTH_SHORT).show();

        datetxt = day + "/" + month + "/" + year;
        if (y==1){
            Start.setText(day + "/" + month + "/" + year);
        }
        else if(y==2){
            End.setText(day + "/" + month + "/" + year);
        }

    }


}

