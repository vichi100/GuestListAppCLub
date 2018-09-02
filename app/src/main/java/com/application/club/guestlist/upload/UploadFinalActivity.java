package com.application.club.guestlist.upload;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.application.club.guestlist.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;


public class UploadFinalActivity extends AppCompatActivity {
    Button imgsel,upload;
    ImageView img;
    String path;
    private DatePickerDialog dpd;
    EditText dateTextView;

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_activity_two);


    }

}