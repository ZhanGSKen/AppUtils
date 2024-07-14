package cc.winboll.studio.libapputils.views;

/**
 * @Author ZhanGSKen@QQ.COM
 * @Date 2024/05/23 22:34:03
 * @Describe 日志视图类，继承 RelativeLayout 类。
 */
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import cc.winboll.studio.libapputils.LogUtils;
import cc.winboll.studio.libapputils.threads.LogViewThread;

public class LogView extends RelativeLayout {

    public static final String TAG = "LogView";

    public volatile boolean mIsHandling;
    public volatile boolean mIsAddNewLog;

    Context mContext;
    ScrollView mScrollView;
    TextView mTextView;
    CheckBox mSelectableCheckBox;
    LogViewThread mLogViewThread;
    LogViewHandler mLogViewHandler;

    public LogView(Context context) {
        super(context);
        initView(context);
    }

    public LogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public LogView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    public void start() {
        mLogViewThread = new LogViewThread(LogView.this);
        mLogViewThread.start();
        // 显示日志
        showAndScrollLogView();
    }

    void initView(Context context) {
        mContext = context;
        mLogViewHandler = new LogViewHandler();
        // 加载视图布局
        addView(inflate(mContext, cc.winboll.studio.libapputils.R.layout.view_log, null));
        // 初始化日志子控件视图
        //
        mScrollView = findViewById(cc.winboll.studio.libapputils.R.id.viewlogScrollViewLog);
        mTextView = findViewById(cc.winboll.studio.libapputils.R.id.viewlogTextViewLog);

        (findViewById(cc.winboll.studio.libapputils.R.id.viewlogButtonClean)).setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    LogUtils.cleanLog();
                    LogUtils.d(TAG, "Log is cleaned.");
                }
            });
        (findViewById(cc.winboll.studio.libapputils.R.id.viewlogButtonCopy)).setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                    ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setPrimaryClip(ClipData.newPlainText(mContext.getPackageName(), LogUtils.loadLog()));
                    LogUtils.d(TAG, "Log is copied.");
                }
            });
        mSelectableCheckBox = findViewById(cc.winboll.studio.libapputils.R.id.viewlogCheckBoxSelectable);
        mSelectableCheckBox.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (mSelectableCheckBox.isChecked()) {
                        setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
                    } else {
                        setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                    }
                }
            });
        // 设置滚动时不聚焦日志
        setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
    }

    public void updateLogView() {
        if (mLogViewHandler.isHandling() == true) {
            // 正在处理日志显示，
            // 就先设置一个新日志标志位
            // 以便日志显示完后，再次显示新日志内容
            mLogViewHandler.setIsAddNewLog(true);
        } else {
            //LogUtils.d(TAG, "LogListener showLog(String path)");
            Message message = mLogViewHandler.obtainMessage(LogViewHandler.MSG_LOGVIEW_UPDATE);
            mLogViewHandler.sendMessage(message);
            mLogViewHandler.setIsAddNewLog(false);
        }
    }

    void showAndScrollLogView() {
        mTextView.setText(LogUtils.loadLog());
        mScrollView.post(new Runnable() {
                @Override
                public void run() {
                    mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    // 日志显示结束
                    mLogViewHandler.setIsHandling(false);
                    // 检查是否添加了新日志
                    if (mLogViewHandler.isAddNewLog()) {
                        // 有新日志添加，先更改新日志标志
                        mLogViewHandler.setIsAddNewLog(false);
                        // 再次发送显示日志的显示
                        Message message = mLogViewHandler.obtainMessage(LogViewHandler.MSG_LOGVIEW_UPDATE);
                        mLogViewHandler.sendMessage(message);
                    }
                }
            });
    }

    class LogViewHandler extends Handler {

        final static int MSG_LOGVIEW_UPDATE = 0;
        volatile boolean isHandling;
        volatile boolean isAddNewLog;

        public LogViewHandler() {
            setIsHandling(false);
            setIsAddNewLog(false);
        }

        public void setIsHandling(boolean isHandling) {
            this.isHandling = isHandling;
        }

        public boolean isHandling() {
            return isHandling;
        }

        public void setIsAddNewLog(boolean isAddNewLog) {
            this.isAddNewLog = isAddNewLog;
        }

        public boolean isAddNewLog() {
            return isAddNewLog;
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOGVIEW_UPDATE:{
                        if (isHandling() == false) {
                            setIsHandling(true);
                            showAndScrollLogView();
                        }
                        break;
                    }
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
