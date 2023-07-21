package com.github.zhangsken.libapputils;

import android.app.*;
import java.io.*;

public class BaseApplication extends Application {

    public static final String TAG = "BaseApplication";

    // 调试标志
    protected static boolean BASEAPPLICATION_DEBUG = BuildConfig.DEBUG;

    public static String _mszLogFolderPath;
    public static String _mszLogFilePath;
    public static String _mszLogFileName = "log.txt";

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.init(this);
        _mszLogFolderPath = getExternalFilesDir(TAG).toString();
        _mszLogFilePath = _mszLogFolderPath + File.separator + _mszLogFileName;

    }

    //
    // 读取调试标志
    //
    public static boolean getDebugFlag() {
        return BASEAPPLICATION_DEBUG;
    }

    //
    // 设置调试标志
    //
    protected void setDebugFlag(boolean isDebug) {
        BASEAPPLICATION_DEBUG = isDebug;
    }


}
