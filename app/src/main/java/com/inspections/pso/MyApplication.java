package com.inspections.pso;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.inspections.pso.utils.Foreground;

/**
 * Created by mobiweb on 5/4/16.
 */
public class MyApplication extends android.app.Application  {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        Foreground.init(this);//for tracking app in background
    }
}

