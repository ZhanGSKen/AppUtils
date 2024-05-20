package cc.winboll.studio.libapputils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.FileObserver;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class LogViewThread extends Thread {

    public static final String TAG = "LogViewThread";

    public volatile boolean mIsExist = false;
    LogListener mLogListener;


    Context mContext;
    ScrollView mScrollView;
    TextView mTextView;
    LogViewHandler mLogViewHandler;

    final static int MSG_SHOW_LOG = 0;
    public volatile boolean mIsHandling;
    public volatile boolean mIsAddNewLog;
    LinearLayout mLinearLayoutRoot;
    CheckBox mSelectableCheckBox;

    //
    // 构造函数
    // @context ：视图环境上下文
    // @linearLayoutRoot ：将要加入ScrollView为主的日志视图的根视图
    public LogViewThread(Context context, LinearLayout linearLayoutRoot) {
        mContext = context;
        mLinearLayoutRoot = linearLayoutRoot;
        mScrollView = new ScrollView(context);

        if (mLogViewHandler == null) {
            mLogViewHandler = new LogViewHandler();
        }

        // 初始化工具栏
        View vLogViewThread = View.inflate(mContext, cc.winboll.studio.libapputils.R.layout.view_logviewthread, null);

        // 初始化日志
        //ViewGroup.LayoutParams lpMain = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        mScrollView = vLogViewThread.findViewById(cc.winboll.studio.libapputils.R.id.headerlogviewthreadScrollView1);
        //mScrollView.setLayoutParams(lpMain);
        //mScrollView.setBackgroundColor(Color.RED);
        mTextView = vLogViewThread.findViewById(cc.winboll.studio.libapputils.R.id.headerlogviewthreadTextView1);
        //mTextView.setTextColor(Color.GREEN);
        //mTextView.setBackgroundColor(Color.GRAY);
        //mTextView.setTextIsSelectable(true);
        //mScrollView.addView(mTextView);
        //mScrollView.setBackgroundColor(Color.BLACK);
        (vLogViewThread.findViewById(cc.winboll.studio.libapputils.R.id.headerlogviewthreadButton1)).setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    LogUtils.cleanLog();
                    LogUtils.d(TAG, "Log is cleaned.");
                }
            });
        (vLogViewThread.findViewById(cc.winboll.studio.libapputils.R.id.headerlogviewthreadButton2)).setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                    ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setPrimaryClip(ClipData.newPlainText(mContext.getPackageName(), LogUtils.loadLog()));
                    LogUtils.d(TAG, "Log is copied.");
                }
            });
        mSelectableCheckBox = vLogViewThread.findViewById(cc.winboll.studio.libapputils.R.id.viewlogviewthreadCheckBox1);
        mSelectableCheckBox.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if (mSelectableCheckBox.isChecked()) {
                        mLinearLayoutRoot.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
                    } else {
                        mLinearLayoutRoot.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                    }
                }
            });

        // 清理根视图
        mLinearLayoutRoot.removeAllViews();
        // 加入新日志视图
        mLinearLayoutRoot.addView(vLogViewThread);
        // 设置滚动时不聚焦日志
        mLinearLayoutRoot.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
    }


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
                Message message = mLogViewHandler.obtainMessage(LogViewHandler.MSG_SHOW_LOG);
                mLogViewHandler.sendMessage(message);
                mLogViewHandler.mIsAddNewLog = false;
            }
        }
    }

    class LogViewHandler extends Handler {

        final static int MSG_SHOW_LOG = 0;
        public volatile boolean mIsHandling;
        public volatile boolean mIsAddNewLog;

        public LogViewHandler() {
            mIsHandling = false;
            mIsAddNewLog = false;
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
