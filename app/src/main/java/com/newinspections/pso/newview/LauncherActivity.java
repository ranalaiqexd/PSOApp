package com.newinspections.pso.newview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.newinspections.pso.R;
import com.newinspections.pso.utils.AppSession;

public class LauncherActivity extends Activity {

    private static final String TAG = "Launcher";

    Context context;
    AppSession appSession = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        context = this;
        appSession = new AppSession(context);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Thread splash = new Thread() {
            public void run() {

                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                    Log.d("Iemi", appSession.getIemiNo());
                    if(appSession.getIemiNo().equals(""))
                    {
                        Intent i = new Intent(context, MacActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        Intent i = new Intent(context, NewLoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }
                }
            }
        };

        splash.start();
    }
}
