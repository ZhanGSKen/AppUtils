package cc.winboll.studio.libapputils;

/**
 * @Author ZhanGSKen@QQ.COM
 * @Date 2023/07/23 13:30:34
 * @Describe 日志视图事务处理类
 */
import android.os.Handler;
import android.os.Message;
import android.widget.ScrollView;

public class LogViewHandler extends Handler {

    final static int MSG_SHOW_LOG = 0;
    LogView mLogView;
    public volatile boolean mIsHandling;
    public volatile boolean mIsAddNewLog;

    public LogViewHandler(LogView logView) {
        mIsHandling = false;
        mIsAddNewLog = false;
        mLogView = logView;
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_SHOW_LOG:{
                    if (mIsHandling == false) {
                        mIsHandling = true;
                        showLog();
                    }
                    break;
                }
            default:
                break;
        }
        super.handleMessage(msg);
    }

    public void showLog() {
        mLogView.mTextView.setText(LogUtils.loadLog());
        mLogView.mScrollView.post(new Runnable() {
                @Override
                public void run() {
                    mLogView.mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    // 日志显示结束
                    mIsHandling = false;
                    // 检查是否添加了新日志
                    if (mIsAddNewLog) {
                        // 有新日志添加，先更改新日志标志
                        mIsAddNewLog = false;
                        // 再次发送显示日志的显示
                        Message message = obtainMessage(MSG_SHOW_LOG);
                        sendMessage(message);
                    }
                }
            });
    }
}
