package com.hexamples.hader.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubmitAbsentExcuse extends AppCompatActivity {
    Toolbar toolbar;
    EditText Subject,Details;
    TextView Start,End,FileName;
    ImageButton imageButton;
    Button button;
    RadioGroup radioGroup;
    String  Excusetype;
    SessionManager sessionManager;
    /////////////////////file////////////////
    private int pageNumber = 0;
    private String pdfFileName;
    public ProgressDialog pDialog;
    public static final int FILE_PICKER_REQUEST_CODE = 1;
    private String pdfPath;
    String FileURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_absent_excuse);
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
         sessionManager=new SessionManager(SubmitAbsentExcuse.this);
         Subject=findViewById(R.id.edit_subject1);
         Details=findViewById(R.id.edit_details_1);
         Start=findViewById(R.id.txt_start1);
         End=findViewById(R.id.txt_end1);
         FileName=findViewById(R.id.file_name);
         radioGroup = findViewById(R.id.radioGroup);
         imageButton=findViewById(R.id.upload1);
         imageButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 launchPicker();
             }
         });

         button=findViewById(R.id.btn_excuse);
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




    public void printBookmarksTree(List<com.shockwave.pdfium.PdfDocument.Bookmark> tree, String sep) {
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
      if(!RadioResult()){}
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

            MyAPI myAPI = GlobalFunctions.getAppRetrofit(SubmitAbsentExcuse.this).create(MyAPI.class);
            Call<BasicResponse> call = myAPI.upload_Excuse(imageFileBody);
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
        MyAPI myAPI = GlobalFunctions.getAppRetrofit(SubmitAbsentExcuse.this).create(MyAPI.class);
        Log.e("Data",  sessionManager.getUserId()+"  "+
                Subject.getText().toString()+"  "+
                FileURI+" "+
                Excusetype+" "+
                Details.getText().toString());
        Call<BasicResponse> call = myAPI.Absent_Excuse(
                sessionManager.getUserId(),
                Subject.getText().toString(),
                FileURI,
                Excusetype,
                Details.getText().toString()
        );
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                // progressDialog.dismiss();
                if (response != null) {
                    if (response.body() != null) {

                        if(!response.body().getStatus().equals("0")) {
                            Log.e("", "Response >> " + new Gson().toJson(response.body()));
                            Toast.makeText(SubmitAbsentExcuse.this,"Success!", Toast.LENGTH_SHORT).show();


                        }
                        else{
                            Toast.makeText(SubmitAbsentExcuse.this,response.body().getMsg(), Toast.LENGTH_SHORT).show();
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
    public boolean RadioResult(){
        boolean x;
        if (radioGroup.getCheckedRadioButtonId() == R.id.Qone_choice1_radio_button) {
            Excusetype = "Sick Vacation";
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.Qone_choice2_radio_button) {
            Excusetype = "Emergency Vacation";
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.Qone_choice3_radio_button) {
            Excusetype = "Vacation Facilities Patient";
        }else if (radioGroup.getCheckedRadioButtonId() == R.id.Qone_choise4_radio_button) {
            Excusetype = "Other";
        }
        else {
            Excusetype = "";
        }
        if(Excusetype.equals("")){
            Toast.makeText(this, "Please select Excuse type", Toast.LENGTH_SHORT).show();
            x= false;

        }else{
            x=true;}

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
}
