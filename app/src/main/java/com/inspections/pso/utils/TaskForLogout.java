package com.inspections.pso.utils;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.inspections.pso.view.SplashActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

/**
 *
 * @author Pintu Kumar Patil 9977368049
 * @author 11-May-2016
 *
 */

public class TaskForLogout extends AsyncTask<Void, Void, String[]> {

    Context context;
    public TaskForLogout(Context context){
        this.context=context;
    }

    @Override
    protected String[] doInBackground(Void... params) {
        return logout(ConstantLib.BASE_URL + ConstantLib.URL_LOGOUT,new AppSession(context).getUserId());
    }

    @Override
    protected void onPostExecute(String[] result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        try {
            if (result != null && !TextUtils.isEmpty(result[0])&&result[0].equals("1")) {

                Log.i("logout","logout......");

                new AppSession(context).setLogin(false);
                new Utils().stopLogoutService(context);

                Intent intent = new Intent(context, SplashActivity.class);
                intent.putExtra("exit",1);
                if(Build.VERSION.SDK_INT >= 11)
                {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                }
                else
                {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                context.startActivity(intent);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        cancel(true);
    }
    public String[] logout(String serviceUrl, String userId) {
        String response = "";
        try {
            ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("user_id", userId));
            response = new RemoteRequestResponse().httpGet(serviceUrl, nvp,null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (response == null || response.equals(""))
            return null;
        return parseSuccess(response);
    }
    public String[] parseSuccess(String json) {

        String[] result = new String[2];
        try {
            Object object = new JSONTokener(json).nextValue();
            if (object instanceof JSONObject) {
                JSONObject jsonObject = new JSONObject(json);
                if (jsonObject.has("status"))
                    result[0] = jsonObject.getString("status");
                if (jsonObject.has("msg"))
                    result[1] = jsonObject.getString("msg");
            } else {
                /** Return response if not in proper format */
                result[0]="0";
                result[1] ="INVALID RESPONSE : \n" + json;
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            /** Return response if not in proper format */
            result[0]="0";
            result[1] = "EXCEPTION DATA PARISNG : \n" + e.getMessage();
            return result;
        }
        return result;
    }
}
