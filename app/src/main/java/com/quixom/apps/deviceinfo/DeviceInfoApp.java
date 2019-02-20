package com.quixom.apps.deviceinfo;

import android.app.Application;

/**
 * Created by quixomtech on 16/2/18.
 */

public class DeviceInfoApp extends Application {
    static DeviceInfoApp sApp;
    public static DeviceInfoApp get() {
        return sApp;
    }
    @Override
    public void onCreate() {
        super.onCreate();
//        Fabric.with(this, new Crashlytics());
        sApp = this;
    }
}
