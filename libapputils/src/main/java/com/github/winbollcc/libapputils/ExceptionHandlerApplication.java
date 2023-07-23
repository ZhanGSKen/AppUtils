package com.github.winbollcc.libapputils;

import android.app.Application;
import java.io.File;

public class ExceptionHandlerApplication extends Application {

    public static final String TAG = "ExceptionHandlerApplication";
    
    // 调试标志
    protected static boolean DEBUG = true;

    public static String _mszLogFolderPath;
    public static String _mszLogFilePath;
    public static String _mszLogFileName = TAG + "_log.txt";

    @Override
    public void onCreate() {
        CrashHandler.init(this);
        super.onCreate();
        _mszLogFolderPath = getCacheDir().getPath();
        _mszLogFilePath = _mszLogFolderPath + File.separator + _mszLogFileName;

    }

    //
    // 读取调试标志
    //
    public static boolean getDebugFlag() {
        return DEBUG;
    }

    //
    // 设置调试标志
    //
    protected void setDebugFlag(boolean isDebug) {
        DEBUG = isDebug;
    }


}
