package cc.winboll.studio.apputils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import cc.winboll.studio.apputils.activities.AboutActivity;
import cc.winboll.studio.apputils.activities.BeanActivity;
import cc.winboll.studio.apputils.activities.LogActivity;
import cc.winboll.studio.apputils.activities.TestRSAKeyUtilsActivity;
import cc.winboll.studio.libapputils.LogUtils;
import cc.winboll.studio.libapputils.views.LogView;
import java.io.File;
import cc.winboll.studio.libapputils.FileUtils;
import java.io.IOException;

public class MainActivity extends Activity {

	public static final String TAG = "MainActivity";

    CheckBox mCheckBoxAppDebugMode;
    LogView mLogView;

    private static final int REQUEST_LOGACTIVITY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLogView = findViewById(R.id.logview);
        mLogView.start();
        //LogUtils.i(TAG, "LogView Start Watching.");

        mCheckBoxAppDebugMode = findViewById(R.id.activitymainCheckBox1);
        GlobalApplication app = (GlobalApplication)getApplication();
        mCheckBoxAppDebugMode.setChecked(app.isDebug());
    }

    public void onAppDebugModeClick(View view) {
        GlobalApplication.setIsDebug(mCheckBoxAppDebugMode.isChecked());
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
    
    public void onTestScrollLogUp(View view) {
        mLogView.scrollLogUp();
    }
    
    public void onTestFileUtils(View view) {
        File fMain = getExternalFilesDir(TAG);
        File fSrcFile = new File(fMain, "src.txt");
        File fDstFile = new File(fMain, "dst.txt");
        try {
            FileUtils.writeStringToFile(fSrcFile.getPath(), "Test String -> " + Long.toString(System.currentTimeMillis()));
            String szSrcTemp = FileUtils.readStringFromFile(fSrcFile.getPath());
            LogUtils.d(TAG, "Src is : " + szSrcTemp);
            FileUtils.copyFile(fSrcFile, fDstFile);
            String szDstTemp = FileUtils.readStringFromFile(fDstFile.getPath());
            LogUtils.d(TAG, "Dst is : " + szDstTemp);
        } catch (IOException e) {}
    }

    public void onTestAPPCrashHandler(View view) {
        try {
            for (int i = Integer.MIN_VALUE; i < Integer.MAX_VALUE; i++) {
                getString(i);
            }
        } catch (Exception e) {
            LogUtils.d(TAG, e, Thread.currentThread().getStackTrace());
        }
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
                    break;
                }
            case R.id.item_beanactivity : {
                    Intent intent = new Intent(this, BeanActivity.class);
                    //startActivity(intent);
                    //mLogView.stopWatching();
                    startActivityForResult(intent, REQUEST_LOGACTIVITY);
                    break;
                }
            case R.id.item_testrsakeyutils: {
                    Intent intent = new Intent(this, TestRSAKeyUtilsActivity.class);
                    //startActivity(intent);
                    //mLogView.stopWatching();
                    startActivityForResult(intent, REQUEST_LOGACTIVITY);
                    break;
                }
            case R.id.item_about : {
                    Intent intent = new Intent(this, AboutActivity.class);
                    //startActivity(intent);
                    //mLogView.stopWatching();
                    startActivityForResult(intent, REQUEST_LOGACTIVITY);
                    break;
                }
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case REQUEST_LOGACTIVITY : {
                    LogUtils.i(TAG, "REQUEST_LOGACTIVITY");
                    break;
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLogView.start();
    }
}
