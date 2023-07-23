package cc.winboll.studio.apputils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import cc.winboll.studio.libapputils.LogUtils;
import cc.winboll.studio.libapputils.LogView;

public class MainActivity extends Activity {

	public static final String TAG = "MainActivity";

    LogView mLogView;
    CheckBox mCheckBoxAppDebugMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		mLogView = findViewById(R.id.activitymainLogView1);
        mLogView.startWatching();
        LogUtils.i(TAG, "LogView Start Watching.");
        
        mCheckBoxAppDebugMode = findViewById(R.id.activitymainCheckBox1);
        App app = (App)getApplication();
        mCheckBoxAppDebugMode.setChecked(app.getDebugFlag());
    }
    
    public void onAppDebugModeClick(View view) {
        App app = (App)getApplication();
        app.setDebugFlag(mCheckBoxAppDebugMode.isChecked());
    }
    
    
    public void onAddDebugLog(View view) {
        for (int i = 0; i < 5; i++) {
            LogUtils.d(TAG, "Add Debug Log " + Integer.toString(i));
        }
    }

    public void onAddInfoLog(View view) {
        for (int i = 0; i < 5; i++) {
            LogUtils.i(TAG, "Add Info Log " + Integer.toString(i));
        }
    }

    public void onCleanLog(View view) {
        LogUtils.cleanLog();
    }

    public void onTestAPPCrashHandler(View view) {
        for (int i = Integer.MIN_VALUE; i < Integer.MAX_VALUE; i++) {
            getString(i);
        }
    }
}
