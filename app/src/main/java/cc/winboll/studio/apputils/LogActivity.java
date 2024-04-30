package cc.winboll.studio.apputils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import cc.winboll.studio.libapputils.LogUtils;
import cc.winboll.studio.libapputils.LogViewThread;

/**
 * @Author ZhanGSKen@QQ.COM
 * @Date 2024/04/30 13:49:23
 * @Describe 弹出的日志窗口
 */
public class LogActivity extends Activity {

    public static final String TAG = "LogActivity";
    LogViewThread mLogViewThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        LinearLayout linearLayout = findViewById(R.id.activitylogLinearLayout1);
        mLogViewThread = new LogViewThread(this, linearLayout);
        mLogViewThread.start();
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
