package cc.winboll.studio.libapputils;

import android.app.Application;
import java.io.File;

public class ExceptionHandlerApplication extends Application {

    public static final String TAG = "ExceptionHandlerApplication";
    
    // 调试标志
    protected static boolean DEBUG = true;

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
        return DEBUG;
    }

    //
    // 设置调试标志
    //
    protected void setDebugFlag(boolean isDebug) {
        DEBUG = isDebug;
    }


}
