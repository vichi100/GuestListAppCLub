package com.application.club.guestlist.profile;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.application.club.guestlist.R;
import com.application.club.guestlist.login.AboutUsActivity;
import com.application.club.guestlist.utils.Constants;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by vichi on 12/02/18.
 */

public class ProfileScreenFragment extends Fragment {

    private static final String cameraPerm = Manifest.permission.CAMERA;

    // UI
    private TextView text;

    // QREader
    private SurfaceView mySurfaceView;
    private QREader qrEader;

    boolean hasCameraPermission = false;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.qr_reader_fragment,null,false);


    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        hasCameraPermission = RuntimePermissionUtil.checkPermissonGranted(getActivity(), cameraPerm);

        text = getActivity().findViewById(R.id.code_info);

//        final Button stateBtn = getActivity().findViewById(R.id.btn_start_stop);
        // change of reader state in dynamic
//        stateBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (qrEader.isCameraRunning()) {
//                    stateBtn.setText("Start QREader");
//                    qrEader.stop();
//                } else {
//                    stateBtn.setText("Stop QREader");
//                    qrEader.start();
//                }
//            }
//        });
//
//        stateBtn.setVisibility(View.VISIBLE);
//
//        Button restartbtn = getActivity().findViewById(R.id.btn_restart_activity);
//        restartbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                restartActivity();
//            }
//        });

        // Setup SurfaceView
        // -----------------
        mySurfaceView = getActivity().findViewById(R.id.camera_view);

        if (hasCameraPermission) {
            // Setup QREader
            setupQREader();
        } else {
            RuntimePermissionUtil.requestPermission(getActivity(), cameraPerm, 100);
        }

    }

    void restartActivity() {
//        startActivity(new Intent(getActivity(), MainActivity.class));
//        finish();
    }

    void setupQREader() {
        // Init QREader
        // ------------
        qrEader = new QREader.Builder(getActivity(), mySurfaceView, new QRDataListener() {
            @Override
            public void onDetected(final String data) {
                Log.d("QREader", "Value : " + data);
                text.post(new Runnable() {
                    @Override
                    public void run() {
                        text.setText(data);
                    }
                });
            }
        }).facing(QREader.BACK_CAM)
                .enableAutofocus(true)
                .height(mySurfaceView.getHeight())
                .width(mySurfaceView.getWidth())
                .build();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (hasCameraPermission) {

            // Cleanup in onPause()
            // --------------------
            qrEader.releaseAndCleanup();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (hasCameraPermission) {

            // Init and Start with SurfaceView
            // -------------------------------
            qrEader.initAndStart(mySurfaceView);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions,
                                           @NonNull final int[] grantResults) {
        if (requestCode == 100) {
            RuntimePermissionUtil.onRequestPermissionsResult(grantResults, new RPResultListener() {
                @Override
                public void onPermissionGranted() {
                    if ( RuntimePermissionUtil.checkPermissonGranted(getActivity(), cameraPerm)) {
                        restartActivity();
                    }
                }

                @Override
                public void onPermissionDenied() {
                    // do nothing
                }
            });
        }
    }

}


