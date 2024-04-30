package cc.winboll.studio.libapputils;

import android.content.Context;
import android.graphics.Color;
import android.os.FileObserver;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.ArrayList;

public class LogView2 {

    public static final String TAG = "LogView";

    ScrollView mScrollView;
    TextView mTextView;
    Context mContext;
    LogViewHandler2 mLogViewHandler;
    static WatchingThread _mWatchingThread;
    
    final static int MSG_SHOW_LOG = 0;
    LogView2 mLogView;
    public volatile boolean mIsHandling;
    public volatile boolean mIsAddNewLog;

    //
    // 构造函数
    //
    public LogView2(Context context) {
        mContext = context;
        mScrollView = new ScrollView(context);

        if (mLogViewHandler == null) {
            mLogViewHandler = new LogViewHandler2(this);
        }
        ViewGroup.LayoutParams lpMain = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mScrollView.setLayoutParams(lpMain);

        //mScrollView.setBackgroundColor(Color.RED);
        mTextView = new TextView(context);
        mTextView.setTextColor(Color.GREEN);
        //mTextView.setBackgroundColor(Color.GRAY);
        mTextView.setTextIsSelectable(true);

        mScrollView.addView(mTextView);
        addView(mScrollView);

        setBackgroundColor(Color.BLACK);

        if (_mWatchingThread == null) {
            _mWatchingThread = new WatchingThread();
            _mWatchingThread.start();
        }
    }
    
    public ScrollView getMainScrollView() {
        
    }

    //
    // 控件初始化函数
    //
    void initView(Context context) {

    }

    //
    // 开始日志文件监控
    //
    public void startWatching() {
        if (_mWatchingThread != null
            && _mWatchingThread.mIsExist == true) {
            _mWatchingThread = new WatchingThread();
            _mWatchingThread.start();
        }
    }

    //
    // 结束日志文件监控
    //
    public void stopWatching() {
        if (_mWatchingThread != null) {
            _mWatchingThread.mIsExist = true;
            _mWatchingThread = null;
        }
    }



    class WatchingThread extends Thread {
        public volatile boolean mIsExist = false;
        LogListener mLogListener;

        @Override
        public void run() {
            //LogUtils.d(TAG, "run");
            mLogListener = new LogListener(ExceptionHandlerApplication._mszLogFolderPath);
            mLogListener.startWatching();
            while (mIsExist == false) {
                try {
                    Thread.sleep(1000);
                    //LogUtils.d(TAG, "WatchingThread sleep (1000)");
                } catch (InterruptedException e) {}
            }
            mLogListener.stopWatching();
            //LogUtils.d(TAG, "WatchingThread stop.");
        }
    }

    //
    // 日志文件监听类
    //
    class LogListener extends FileObserver {
        public String mLogPath;

        public LogListener(String path) {
            super(path);
            mLogPath = path;
            //LogUtils.d(TAG, "LogListener(String path)");
        }

        @Override
        public void onEvent(int event, String path) {
            int e = event & FileObserver.ALL_EVENTS;
            switch (e) {
                    /*
                     case FileObserver.ACCESS:
                     //Log.d(TAG, "文件操作___" + e + "__1打开文件后读取文件的操作");
                     break;
                     case FileObserver.MODIFY:
                     //Log.d(TAG, "文件操作___" + e + "__2文件被修改");
                     break;
                     case FileObserver.ATTRIB:
                     //Log.d(TAG, "文件操作___" + e + "__4属性变化");
                     break;
                     case FileObserver.CLOSE_NOWRITE:
                     //录音时，最后一个有效回调是这个
                     //Log.d(TAG, "文件操作___" + e + "__16只读文件被关闭");
                     //callback.onEvent(path);
                     break;
                     case FileObserver.OPEN:
                     //Log.d(TAG, "文件操作___" + e + "__32文件被打开");
                     break;
                     case FileObserver.MOVED_FROM:
                     //Log.d(TAG, "文件操作___" + e + "__64移出事件");//试了重命名先MOVED_FROM再MOVED_TO
                     break;
                     case FileObserver.MOVED_TO:
                     //Log.d(TAG, "文件操作___" + e + "__128移入事件");
                     break;
                     case FileObserver.CREATE:
                     //Log.d(TAG, "文件操作___" + e + "__256新建文件");//把文件移动给自己先CREATE在DELETE
                     break;

                     case FileObserver.DELETE_SELF:
                     //Log.d(TAG, "文件操作___" + e + "__1024监听的这个文件夹被删除");
                     break;
                     case FileObserver.MOVE_SELF:
                     //Log.d(TAG, "文件操作___" + e + "__2048监听的这个文件夹被移走");
                     break;
                     case FileObserver.ALL_EVENTS:
                     //Log.d(TAG, "文件操作___" + e + "__4095全部操作");
                     break;
                     */
                case FileObserver.CLOSE_WRITE:
                    showLog(path);
                    break;
                    //Log.d(TAG, "文件操作___" + e + "__8文件写入或编辑后关闭");
                case FileObserver.DELETE:
                    //Log.d(TAG, "文件操作___" + e + "__512有删除文件");//把文件移出去DELETE
                    showLog(path);
                    break;
            }
        }

        void showLog(String path) {
            if (mLogViewHandler.mIsHandling == true) {
                // 正在处理日志显示，
                // 就先设置一个新日志标志位
                // 以便日志显示完后，再次显示新日志内容
                mLogViewHandler.mIsAddNewLog = true;
            } else {
                //LogUtils.d(TAG, "LogListener showLog(String path)");
                Message message = mLogViewHandler.obtainMessage(LogViewHandler2.MSG_SHOW_LOG);
                mLogViewHandler.sendMessage(message);
                mLogViewHandler.mIsAddNewLog = false;
            }
        }
    }
    
    class LogViewHandler2 extends Handler {

        final static int MSG_SHOW_LOG = 0;
        public volatile boolean mIsHandling;
        public volatile boolean mIsAddNewLog;

        public LogViewHandler2(LogView2 logView) {
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
            mTextView.setText(LogUtils.loadLog());
            mScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
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
    
    
}
