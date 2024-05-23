package cc.winboll.studio.apputils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import cc.winboll.studio.libapputils.LogRelativeLayout;
import cc.winboll.studio.libapputils.LogUtils;

/**
 * @Author ZhanGSKen@QQ.COM
 * @Date 2024/04/30 13:49:23
 * @Describe 弹出的日志窗口
 */
public class LogActivity extends Activity {

    public static final String TAG = "LogActivity";
    LogRelativeLayout mLogRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        mLogRelativeLayout = findViewById(R.id.logRelativeLayout);
        mLogRelativeLayout.start();
        LogUtils.i(TAG, "LogView Start Watching.");
        
        // 添加日志的按钮响应
        findViewById(R.id.activitylogButton1).setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    LogUtils.i(TAG, "Info log.");
                    LogUtils.d(TAG, "Debug log.");
                }
            });

        // 清理日志
        findViewById(R.id.activitylogButton2).setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    LogUtils.cleanLog();
                    LogUtils.i(TAG, "Log clear.");
                }
            });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



}
