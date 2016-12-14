package com.eb.seeu;


import android.app.Application;

public class OrderApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (!OrderContext.isInitialized()) {
            OrderContext.init(getApplicationContext());
        }
    }
}
