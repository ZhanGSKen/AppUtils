package cc.winboll.studio.libapputils;

/**
 * @Author ZhanGSKen@QQ.COM
 * @Date 2024/06/09 10:26:57
 * @Describe 应用基础类
 */
import android.app.Application;
import cc.winboll.studio.libapputils.LogUtils;
import cc.winboll.studio.libapputils.handlers.CrashHandler;

public class BaseApplication extends Application {

    public static final String TAG = "BaseApplication";

    // 应用调试标志
    protected volatile static boolean _mIsDebug = true;

    //
    // 读取调试标志
    //
    public static void setIsDebug(boolean isDebug) {
        _mIsDebug = isDebug;
    }

    //
    // 设置调试标志
    //
    public static boolean isDebug() {
        return _mIsDebug;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.init(this);
        LogUtils.init(this);
    }
}
