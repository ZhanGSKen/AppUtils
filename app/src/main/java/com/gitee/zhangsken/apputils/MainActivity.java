package com.gitee.zhangsken.apputils;

import android.app.*;
import android.os.*;
import android.view.*;
import com.gitee.zhangsken.libapputils.*;

public class MainActivity extends Activity 
{
	public static final String TAG = "MainActivity";

    LogView mLogView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		
		// 添加应用版本提示
		if(BuildConfig.DEBUG && getPackageName().equals("com.gitee.zhangsken.apputils.debug")) {
			getActionBar().setSubtitle("Debug version");
		} else if(!BuildConfig.DEBUG && getPackageName().equals("com.gitee.zhangsken.apputils")) {
			getActionBar().setSubtitle("Release version");
		}
		
		mLogView = findViewById(R.id.activitymainLogView1);
        mLogView.startWatching();
        LogUtils.d(TAG, "LogView Start Watching.");
    }
	
	public void onAddInfoLog(View view) {
        for (int i = 0; i < 100; i++) {
            LogUtils.i(TAG, "Add Info Log " + Integer.toString(i));
        }
    }

    public void onAddLog(View view) {
        for (int i = 0; i < 100; i++) {
            LogUtils.d(TAG, "Add Log " + Integer.toString(i));
        }
    }

    public void onCleanLog(View view) {
        LogUtils.cleanLog();
        LogUtils.d(TAG, "Clean Log.");
    }

    public void onTest_libapputils_demo(View view) {
        for (int i = Integer.MIN_VALUE; i < Integer.MAX_VALUE; i++) {
            getString(i);
        }
    }
}
