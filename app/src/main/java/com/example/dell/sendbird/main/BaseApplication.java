package com.example.dell.sendbird.main;


import android.app.Application;

import com.sendbird.android.SendBird;
import com.example.dell.sendbird.utils.PreferenceUtils;

public class BaseApplication extends Application {

    private static final String APP_ID = "3879B3C7-81F3-4E12-BCA3-4D00DDAC2065"; // US-1 Demo
    public static final String VERSION = "3.0.39";

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceUtils.init(getApplicationContext());

        SendBird.init(APP_ID, getApplicationContext());
    }
}
