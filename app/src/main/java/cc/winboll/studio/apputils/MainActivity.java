package cc.winboll.studio.apputils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import cc.winboll.studio.libapputils.LogUtils;
import cc.winboll.studio.libapputils.LogView2;

public class MainActivity extends Activity {


	public static final String TAG = "MainActivity";

    LogView2 mLogView;
    CheckBox mCheckBoxAppDebugMode;

    private static final int REQUEST_LOGACTIVITY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       LinearLayout linearLayout = findViewById(R.id.activitymainLinearLayout1);
        linearLayout.inflate(this, R.layout.view_log , linearLayout);
        mLogView = linearLayout.findViewById(R.id.viewlogLogView21);
        //mLogView.startWatching();
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
        for (int i = 0; i < 100; i++) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_logactivity : {
                    Intent intent = new Intent(this, LogActivity.class);
                    //startActivity(intent);
                    //mLogView.stopWatching();
                    startActivityForResult(intent, REQUEST_LOGACTIVITY);
                }
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case REQUEST_LOGACTIVITY : {
                    mLogView.startWatching();
                    LogUtils.i(TAG, "REQUEST_LOGACTIVITY");
                    break;
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
