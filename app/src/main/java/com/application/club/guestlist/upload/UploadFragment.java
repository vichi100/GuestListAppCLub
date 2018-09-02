package com.application.club.guestlist.upload;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.application.club.guestlist.MainActivity;
import com.application.club.guestlist.R;
import com.application.club.guestlist.service.EventListener;
import com.application.club.guestlist.service.SocketOperator;
import com.application.club.guestlist.utils.Constants;
import com.application.club.guestlist.utils.UtillMethods;
import com.bumptech.glide.Glide;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.Future;

import static android.app.Activity.RESULT_OK;


public class UploadFragment extends Fragment implements DatePickerDialog.OnDateSetListener, EventListener {

    SocketOperator socketOperator;

    Button upload;//imgsel
    ImageView img;
    String path;
    private DatePickerDialog dpd;
    EditText dateTextView;
    Context context;
    private ProgressBar progressBar;
    boolean isImageUploaded = false;
    String isEventCreated = "success";

    boolean getData = false;
    private static int screenWight = 0;

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    private int getScreenWight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.upload_activity_one, container, false);
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        context = getActivity().getApplicationContext();

        //socketOperator = new SocketOperator(this);
        screenWight = getScreenWight();

        final Calendar cal = Calendar.getInstance();
        cal.get(Calendar.YEAR);
        cal.get(Calendar.MONTH);
        cal.get(Calendar.DAY_OF_MONTH);

        final ScrollView scrollView = (ScrollView)getActivity().findViewById(R.id.activity_main);
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                //replace this line to scroll up or down
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 100L);

        dateTextView = (EditText) getActivity().findViewById(R.id.eventDatetv);
        dateTextView.setKeyListener(null);
        ImageButton datePickerButton = getActivity().findViewById(R.id.datePicker);

        progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);

        final EditText eventNametv = (EditText) getActivity().findViewById(R.id.eventNametv);


        final EditText djtv = (EditText) getActivity().findViewById(R.id.djtv);


        final EditText musictv = (EditText) getActivity().findViewById(R.id.music);


        //dateTextView = findViewById(R.id.date_textview);

        // Show a datepicker when the dateButton is clicked
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                /*
                It is recommended to always create a new instance whenever you need to show a Dialog.
                The sample app is reusing them because it is useful when looking for regressions
                during testing
                 */
                if (dpd == null) {
                    dpd = DatePickerDialog.newInstance(
                            UploadFragment.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );

                } else {
                    dpd.initialize(
                            UploadFragment.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                }

                dpd.setMinDate(cal);

                dpd.setVersion(DatePickerDialog.Version.VERSION_2);

                //dpd.setVersion(showVersion2.isChecked() ? DatePickerDialog.Version.VERSION_2 : DatePickerDialog.Version.VERSION_1);

                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });



        img = (ImageView)getActivity().findViewById(R.id.imageView);
        Glide.with(getActivity())
                .load(R.drawable.gallery)
                .override(200, 200)
                .centerCrop()
                //.placeholder(R.drawable.circular_progress_bar)
                .into(img);
        Ion.getDefault(getActivity()).configure().setLogging("ion-sample", Log.DEBUG);
        //imgsel = (Button)getActivity().findViewById(R.id.selimg);
        upload =(Button)getActivity().findViewById(R.id.next);
        SharedPreferences settings = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
        final String clubname = settings.getString("clubname","");
        final String clubId = settings.getString("clubid","");
        //upload.setVisibility(View.INVISIBLE);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                socketOperator = new SocketOperator(UploadFragment.this);
                if(path == null){

                    Toast toast = Toast.makeText(getActivity(), "Please Select Image", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                File f = new File(path);
                if (dateTextView.getText().toString().matches("")) {
                    Toast toast = Toast.makeText(getActivity(), "Event Date is empty", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                String changedDateFormat = UtillMethods.changeDateFormate(dateTextView.getText().toString());

                Future uploading = Ion.with(UploadFragment.this)
                        .load("http://192.168.43.64:9090/upload?clubId="+clubId+"&eventDate="+changedDateFormat)
                        .progressBar(progressBar)
                        .setMultipartFile("image", f)
                        .asString()
                        .withResponse()
                        .setCallback(new FutureCallback<Response<String>>() {
                            @Override
                            public void onCompleted(Exception e, Response<String> result) {
                                // print the response code, ie, 200
                                //System.out.println(result.getHeaders().code());
                                // print the String that was downloaded
                                //System.out.println(result.toString());
                                if(result!= null && result.getHeaders().code() == 200){
                                        isImageUploaded = true;
                                     }else{
                                        isImageUploaded = false;
                                     }


                            }
                        });


//                    SharedPreferences settings = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
//                    String clubname = settings.getString("clubname","");
//                    String clubId = settings.getString("clubid","");
                    String eventName = eventNametv.getText().toString();
                    if (eventName.matches("")) {
                        Toast toast = Toast.makeText(getActivity(), "Event Name is empty", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        return;
                    }
                    String djName = djtv.getText().toString();
                    if (djName.matches("")) {
                        Toast toast = Toast.makeText(getActivity(), "DJ Name is empty", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        return;
                    }
                    String music = musictv.getText().toString();
                    if (music.matches("")) {
                        Toast toast = Toast.makeText(getActivity(), "Music is empty", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        return;
                    }

                    JSONObject eventDataJobj = new JSONObject();
                    eventDataJobj.put("action", "insertNewEventDetails");
                    eventDataJobj.put(Constants.CLUB_ID, clubId);
                    eventDataJobj.put(Constants.CLUB_NAME, clubname);
                    eventDataJobj.put(Constants.EVENTNAME, eventName);
                    eventDataJobj.put(Constants.DJ_NAME, djName);
                    eventDataJobj.put(Constants.MUSIC, music);
                    //eventDataJobj.put(Constants.IMAGE_URL, music);
                    //final SimpleDateFormat simpleDateformat = new SimpleDateFormat("dd/MMM/yyyy");
                    eventDataJobj.put(Constants.EVENT_DATE, changedDateFormat);
                    socketOperator.sendMessage(eventDataJobj);

                    int count = 0;
                    while((!getData )){
                        SystemClock.sleep(1000);
                        count++;
                    }

                    getData = false;

                    if(isEventCreated.equalsIgnoreCase("success") ){
                        Toast toast = Toast.makeText(getActivity(), "Uploaded Successfuly", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
//                    else {
//                        Toast.makeText(getActivity(), "Error, Please try after some time !!!", Toast.LENGTH_LONG).show();
//
//                    }
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);

                }catch (Exception ex){
                    ex.printStackTrace();
                    Toast.makeText(getActivity(), "Error, Please try after some time !!!", Toast.LENGTH_SHORT).show();


                }



            }

        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fintent = new Intent(Intent.ACTION_GET_CONTENT);
                fintent.setType("image/* video/*");
                try {
                    startActivityForResult(fintent, 100);
                } catch (ActivityNotFoundException e) {

                }
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;
        switch (requestCode) {
            case 100:
                if (resultCode == RESULT_OK) {
                    path = getPath(context, data.getData());// getPathFromURI(data.getData());
                    //img.setImageURI(data.getData());

                    Glide.with(getActivity())
                            .load(data.getData())
                            .override(screenWight, screenWight * 9 / 16)
                            .centerCrop()
                            //.placeholder(R.drawable.circular_progress_bar)
                            .into(img);
                    upload.setVisibility(View.VISIBLE);

                }
        }
    }


    public void requestRead(Uri contentUri) {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            //getPathFromURI(contentUri);
            getPath(context, contentUri);
        } else {
            //getPathFromURI(contentUri);
            getPath(context, contentUri);
        }
    }


    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }



    private String getPathFromURI(Uri contentUri) {
//        String[] proj = { MediaStore.Images.Media.DATA };
//        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
//        Cursor cursor = loader.loadInBackground();
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);

        String wholeID = DocumentsContract.getDocumentId(contentUri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = getActivity().getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{ id }, null);

        String filePath = "";

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                // Permission Denied
               // Toast.makeText(ToolbarActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getActivity().getFragmentManager().findFragmentByTag("Datepickerdialog");
        if(dpd != null) dpd.setOnDateSetListener(this);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+"/"+(++monthOfYear)+"/"+year;
        dateTextView.setText(date);
    }


    public void eventReceived(String message){
        // conver message to list
        if(message != null){

            try{
                if(message.equalsIgnoreCase("success")){
                    isEventCreated = "success";
                }else{
                    isEventCreated = "fail";
                }


            }catch (Exception ex){
                ex.printStackTrace();

            }

        }


        getData = true;



    }

}