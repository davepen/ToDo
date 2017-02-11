package com.dpenaskovic.todo;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

public class ToDoApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        FlowManager.init(new FlowConfig.Builder(this).build());

        // add for verbose logging
        // FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);
    }
}
