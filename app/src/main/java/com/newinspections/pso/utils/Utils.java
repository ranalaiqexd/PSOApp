package com.newinspections.pso.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by asus on 16/12/15.
 */
public class Utils {


    public static void sendExceptionReport(Exception e, Context c) {
        e.printStackTrace();

        // try {
        // Writer result = new StringWriter();
        // PrintWriter printWriter = new PrintWriter(result);
        // e.printStackTrace(printWriter);
        // String stacktrace = result.toString();
        // new CustomExceptionHandler(c).sendToServer(stacktrace);
        // } catch (Exception e1) {
        // e1.printStackTrace();
        // }

    }

    public static Typeface getHeavyItalic(Context c) {
        try {
            return Typeface.createFromAsset(c.getAssets(), "Barrez-Heavy Italic.otf");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Typeface getNormal(Context c) {
        try {
            return Typeface.createFromAsset(c.getAssets(), "Raleway-Regular.ttf");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void hideKeyBoard(Context c, View v) {
        InputMethodManager imm = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static boolean isValidEmailAddress(String emailAddress) {
        String expression = "[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})";
        CharSequence inputStr = emailAddress;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }
    public static void setPref(Context c, String pref, String val) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putString(pref, val);
        e.commit();

    }

    public static String getPref(Context c, String pref, String val) {
        return PreferenceManager.getDefaultSharedPreferences(c).getString(pref, val);
    }
    public void startLogoutService(Context context,boolean onCurrentTime) {
        try {
            Calendar cal = Calendar.getInstance();
            Intent intent = new Intent(context, LogoutService.class);
            intent.setData((Uri.parse("SERVICE_ABCFUEL_LOGOUT_1001")));
            PendingIntent pintent = PendingIntent.getService(context, 0,
                    intent, 0);
            AlarmManager alarm = (AlarmManager) context
                    .getSystemService(Context.ALARM_SERVICE);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                alarm.setRepeating(AlarmManager.RTC_WAKEUP,
                        cal.getTimeInMillis(),
                        15 * 1000, pintent);
            } else {
                if (onCurrentTime)
                    alarm.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis()
                            + (0 * 1000), pintent);
                else alarm.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis()
                        + (30 * 1000), pintent);
            }
            Log.i("startLogoutService", "startLogoutService");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopLogoutService(Context context) {
        try {
            Intent intent = new Intent(context, LogoutService.class);
            intent.setData((Uri.parse("SERVICE_ABCFUEL_LOGOUT_1001")));
            PendingIntent pintent = PendingIntent.getService(context, 0,
                    intent, 0);
            // PendingIntent pintent = PendingIntent.getBroadcast(this, 0,
            // intent, 0);
            AlarmManager alarm = (AlarmManager) context
                    .getSystemService(Context.ALARM_SERVICE);
            alarm.cancel(pintent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    static SimpleDateFormat HH_MM_SS = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    public static String getTimeLogByFormat(String updateTime) {
        try {
            if (TextUtils.isEmpty(updateTime))return "N/A";
            if (updateTime.equalsIgnoreCase("N/A"))return "N/A";
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date updateDate = dateFormat.parse(updateTime);

            return HH_MM_SS.format(updateDate);
        } catch (Exception e1) {
            e1.printStackTrace();
            return "N/A";
        }

    }

    public static String getDateLogByFormat(String updateTime) {
        try {
            if (TextUtils.isEmpty(updateTime))return "N/A";
            if (updateTime.equalsIgnoreCase("N/A"))return "N/A";
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date updateDate = dateFormat.parse(updateTime);

            return YYYY_MM_DD.format(updateDate);
        } catch (Exception e1) {
            e1.printStackTrace();
            return "N/A";
        }

    }
}
