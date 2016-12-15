package com.inspections.pso.netcome;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * CheckNetworkStateClass is a public class use for check network is available or
 * not for entire applications.use this class before hit webservice 
 * 
 * @author Yogesh Tatwal
 * 
 * */
public class CheckNetworkStateClass {
	public static boolean isOnline(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

		if (activeNetwork != null) {
			return activeNetwork.isConnectedOrConnecting();
		}

		NetworkInfo wifiNetwork = cm
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetwork != null) {
			return wifiNetwork.isConnectedOrConnecting();
		}

		NetworkInfo mobileNetwork = cm
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetwork != null) {
			return mobileNetwork.isConnectedOrConnecting();
		}

		NetworkInfo otherNetwork = cm.getActiveNetworkInfo();
		if (otherNetwork != null) {
			return otherNetwork.isConnectedOrConnecting();
		}

		return false;
	}

}
