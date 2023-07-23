package com.github.winbollcc.libapputils;

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

public class LogView extends LinearLayout {

    public static final String TAG = "LogView";

    ScrollView mScrollView;
    TextView mTextView;
    LogViewHandler mLogViewHandler;
    WatchingThread mWatchingThread = null;
    Context mContext;

    //
    // 构造函数
    //
    public LogView(Context context) {
        super(context);
        mContext = context;
        initView(context);
    }

    //
    // 构造函数
    //
    public LogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView(context);
    }

    //
    // 控件初始化函数
    //
    void initView(Context context) {
        mLogViewHandler = new LogViewHandler(this);

        ViewGroup.LayoutParams lpMain = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(lpMain);

        mScrollView = new ScrollView(context);
        //mScrollView.setBackgroundColor(Color.RED);
        mTextView = new TextView(context);
        mTextView.setTextColor(Color.GREEN);
        //mTextView.setBackgroundColor(Color.GRAY);
        mTextView.setTextIsSelectable(true);

        mScrollView.addView(mTextView);
        addView(mScrollView);

        setBackgroundColor(Color.BLACK);
    }

    //
    // 开始日志文件监控
    //
    public void startWatching() {
        if (mWatchingThread == null) {
            mWatchingThread = new WatchingThread();
            mWatchingThread.start();
            /*try {
             Thread.sleep(100);
             LogUtils.d(TAG, "LogView start watching.");
             } catch (InterruptedException e) {}*/
        }
    }

    //
    // 结束日志文件监控
    //
    public void stopWatching() {
        if (mWatchingThread != null) {
            mWatchingThread._mIsExist = true;
            mWatchingThread = null;
        }
    }

    class WatchingThread extends Thread {
        public volatile boolean _mIsExist = false;
        LogListener mLogListener;

        @Override
        public void run() {
            //LogUtils.d(TAG, "run");
            mLogListener = new LogListener(ExceptionHandlerApplication._mszLogFolderPath);
            mLogListener.startWatching();
            while (_mIsExist == false) {
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
            if (mLogViewHandler.misHandling == true) {
                // 正在处理日志显示，
                // 就先设置一个新日志标志位
                // 以便日志显示完后，再次显示新日志内容
                mLogViewHandler.misAddNewLog = true;
            } else {
                //LogUtils.d(TAG, "LogListener showLog(String path)");
                Message message = mLogViewHandler.obtainMessage(LogViewHandler.MSG_SHOW_LOG);
                mLogViewHandler.sendMessage(message);
                mLogViewHandler.misAddNewLog = false;
            }
        }
    }
}
