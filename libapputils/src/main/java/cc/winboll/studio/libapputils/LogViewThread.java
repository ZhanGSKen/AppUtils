package cc.winboll.studio.libapputils;

import android.os.FileObserver;

public class LogViewThread extends Thread {

    public static final String TAG = "LogViewThread";

    volatile boolean isExist = false;
    LogListener mLogListener;
    LogLinearLayout mLogLinearLayout;

    //
    // 构造函数
    // @logLinearLayout : 日志显示输出布局类
    public LogViewThread(LogLinearLayout logLinearLayout) {
        mLogLinearLayout = logLinearLayout;
    }

    public void setIsExist(boolean isExist) {
        this.isExist = isExist;
    }

    public boolean isExist() {
        return isExist;
    }

    @Override
    public void run() {
        //LogUtils.d(TAG, "run");
        mLogListener = new LogListener(ExceptionHandlerApplication._mszLogFolderPath);
        mLogListener.startWatching();
        while (isExist() == false) {
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
                case FileObserver.CLOSE_WRITE:
                    mLogLinearLayout.updateLogView();
                    break;
                    //Log.d(TAG, "文件操作___" + e + "__8文件写入或编辑后关闭");
                case FileObserver.DELETE:
                    //Log.d(TAG, "文件操作___" + e + "__512有删除文件");//把文件移出去DELETE
                    mLogLinearLayout.updateLogView();
                    break;
            }
        }
    }




}
