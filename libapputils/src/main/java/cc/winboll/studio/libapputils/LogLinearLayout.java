package cc.winboll.studio.libapputils;

/**
 * @Author ZhanGSKen@QQ.COM
 * @Date 2024/05/23 22:34:03
 * @Describe 日志视图类LinearLayout布局
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
import cc.winboll.studio.libapputils.LogViewHandler;

public class LogLinearLayout extends RelativeLayout {

    public static final String TAG = "LogLinearLayout";

    Context mContext;
    ScrollView mScrollView;
    TextView mTextView;
    CheckBox mSelectableCheckBox;
    LogViewThread mLogViewThread;

    LogLinearLayoutHandler mLogLinearLayoutHandler;

    public LogLinearLayout(Context context) {
        super(context);
        initView(context);
    }

    public LogLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LogLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public LogLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    public void start() {
        mLogViewThread = new LogViewThread(LogLinearLayout.this);
        mLogViewThread.start();
    }

    void initView(Context context) {
        mContext = context;
        mLogLinearLayoutHandler = new LogLinearLayoutHandler();

        // 初始化工具栏
        addView(inflate(mContext, cc.winboll.studio.libapputils.R.layout.view_loglinearlayout, null));

        // 初始化日志
        //ViewGroup.LayoutParams lpMax = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mScrollView = findViewById(cc.winboll.studio.libapputils.R.id.viewloglinearlayoutScrollView1);
        //mScrollView.setLayoutParams(lpMax);
        //mScrollView.setBackgroundColor(Color.RED);
        mTextView = findViewById(cc.winboll.studio.libapputils.R.id.viewloglinearlayoutTextView1);
        //mTextView.setTextColor(Color.GREEN);
        //mTextView.setBackgroundColor(Color.GRAY);
        //mTextView.setTextIsSelectable(true);
        //mScrollView.addView(mTextView);
        //mScrollView.setBackgroundColor(Color.BLACK);
        (findViewById(cc.winboll.studio.libapputils.R.id.viewloglinearlayoutButton1)).setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    LogUtils.cleanLog();
                    LogUtils.d(TAG, "Log is cleaned.");
                }
            });
        (findViewById(cc.winboll.studio.libapputils.R.id.viewloglinearlayoutButton2)).setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                    ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setPrimaryClip(ClipData.newPlainText(mContext.getPackageName(), LogUtils.loadLog()));
                    LogUtils.d(TAG, "Log is copied.");
                }
            });
        mSelectableCheckBox = findViewById(cc.winboll.studio.libapputils.R.id.viewloglinearlayoutCheckBox1);
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
        if (mLogLinearLayoutHandler.isHandling() == true) {
            // 正在处理日志显示，
            // 就先设置一个新日志标志位
            // 以便日志显示完后，再次显示新日志内容
            mLogLinearLayoutHandler.setIsAddNewLog(true);
        } else {
            //LogUtils.d(TAG, "LogListener showLog(String path)");
            Message message = mLogLinearLayoutHandler.obtainMessage(LogViewHandler.MSG_SHOW_LOG);
            mLogLinearLayoutHandler.sendMessage(message);
            mLogLinearLayoutHandler.setIsAddNewLog(false);
        }
    }

    class LogLinearLayoutHandler extends Handler {

        final static int MSG_UPDATE_LOG_VIEW = 0;
        volatile boolean isHandling;
        volatile boolean isAddNewLog;

        public LogLinearLayoutHandler() {
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
                case MSG_UPDATE_LOG_VIEW:{
                        if (isHandling() == false) {
                            setIsHandling(true);
                            showAndScrollLog();
                        }
                        break;
                    }
                default:
                    break;
            }
            super.handleMessage(msg);
        }

        void showAndScrollLog() {
            mTextView.setText(LogUtils.loadLog());
            mScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        // 日志显示结束
                        setIsHandling(false);
                        // 检查是否添加了新日志
                        if (isAddNewLog()) {
                            // 有新日志添加，先更改新日志标志
                            setIsAddNewLog(false);
                            // 再次发送显示日志的显示
                            Message message = obtainMessage(MSG_UPDATE_LOG_VIEW);
                            sendMessage(message);
                        }
                    }
                });

        }
    }
}
