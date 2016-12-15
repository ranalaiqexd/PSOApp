package com.inspections.pso.utils;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.inspections.pso.netcome.CheckNetworkStateClass;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author Pintu Kumar Patil 9977368049
 * @author 11-May-2016
 *
 */

public class LogoutService extends Service {
	String TAG = getClass().getSimpleName();
	AppSession appSession = null;
	TaskForLogout taskForLogout=null;
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {

			//Log.i("onStartCommand", "onStartCommand");
			appSession = new AppSession(getBaseContext());
			/* CHECK USER LOGIN INFORMATION */
			if (appSession != null && appSession.isLogin()){

				Log.i("getElapsedTime", "getElapsedTime "+ TimeUnit.SECONDS.toMinutes(appSession.getElapsedTime()));

				if (Foreground.get(getBaseContext()).isBackground() && TimeUnit.SECONDS.toMinutes(appSession.getElapsedTime())>7){
					if (CheckNetworkStateClass.isOnline(getBaseContext())) {
						if (taskForLogout!=null&& !taskForLogout.isCancelled())
							taskForLogout.cancel(true);
							taskForLogout=new TaskForLogout(getBaseContext());
							taskForLogout.execute();
					}
				}
			} else {
				new Utils().stopLogoutService(getBaseContext());
			}
			if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
			} else {
				new Utils().startLogoutService(getBaseContext(), false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (taskForLogout!=null&& !taskForLogout.isCancelled()){
			taskForLogout.cancel(true);
		}
	}
}
