package cc.winboll.studio.libapputils;

/**
 * @Author ZhanGSKen@QQ.COM
 * @Date 2024/06/09 10:26:57
 * @Describe 应用基础类
 */
import android.app.Application;

public class BaseApplication extends Application {

    public static final String TAG = "BaseApplication";

    // 应用调试标志
    protected static boolean isDebug = true;

    //
    // 读取调试标志
    //
    public static void setIsDebug(boolean isDebug) {
        BaseApplication.isDebug = isDebug;
    }

    //
    // 设置调试标志
    //
    public static boolean isDebug() {
        return isDebug;
    }

    @Override
    public void onCreate() {
        CrashHandler.init(this);
        LogUtils.init(this);
        super.onCreate();
    }
}
