package com.newinspections.pso.newview;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.newinspections.pso.R;
import com.newinspections.pso.utils.AppSession;

public class MacActivity extends AppCompatActivity {

    private static final String TAG = "Mac";

    Context context;
    private Button buttonMacAddress;
    private String deviceIemi = null;
    private String deviceName = null;
    AppSession appSession = null;
    TelephonyManager telephonyManager = null;
    private static final int MY_PERMISSION_READ_PHONE_STATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        appSession = new AppSession(context);

        buttonMacAddress = (Button)findViewById(R.id.button_macAddrss);
        telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);


    }

    @Override
    protected void onStart() {
        super.onStart();
        deviceName = android.os.Build.MODEL;
    }

    @Override
    protected void onResume() {
        super.onResume();

        buttonMacAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appSession.setDevice(deviceName);
                CheckPermissions();
            }
        });
    }

    private void CheckPermissions()
    {
        if((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M))
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSION_READ_PHONE_STATE);
            }
            else
            {
                Log.d("Check", "here");
                deviceIemi = telephonyManager.getDeviceId();
                appSession.setIemiNo(deviceIemi);
                Intent intent = new Intent(context, NewLoginActivity.class);
                startActivity(intent);
            }
        }
        else
        {
            deviceIemi = telephonyManager.getDeviceId();
            appSession.setIemiNo(deviceIemi);
            Intent intent = new Intent(context, NewLoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_READ_PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted!
                    // you may now do the action that requires this permission
                    deviceIemi = telephonyManager.getDeviceId();
                    appSession.setIemiNo(deviceIemi);
                    Intent intent = new Intent(context, NewLoginActivity.class);
                    startActivity(intent);
                }
                else {
                    // permission denied
                    Toast.makeText(context, "Must Provide Permissions First", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }
}
