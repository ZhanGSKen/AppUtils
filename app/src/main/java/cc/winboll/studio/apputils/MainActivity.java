package cc.winboll.studio.apputils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import cc.winboll.studio.libapputils.LogUtils;
import cc.winboll.studio.libapputils.LogView;

public class MainActivity extends Activity {

	public static final String TAG = "MainActivity";

    LogView mLogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
