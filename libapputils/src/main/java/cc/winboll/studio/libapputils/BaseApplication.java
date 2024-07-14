package cc.winboll.studio.libapputils;

/**
 * @Author ZhanGSKen@QQ.COM
 * @Date 2024/06/09 10:26:57
 * @Describe 应用基础类
 */
import android.app.Application;
import android.view.Gravity;
import cc.winboll.studio.libapputils.LogUtils;
import cc.winboll.studio.libapputils.handlers.CrashHandler;
import com.hjq.toast.ToastUtils;
import com.hjq.toast.style.WhiteToastStyle;

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
        // 应用环境初始化
        //
        CrashHandler.init(this);
        LogUtils.init(this);
        // 初始化 Toast 框架
        ToastUtils.init(this);
        // 设置 Toast 布局样式
        //ToastUtils.setView(cc.winboll.studio.libaes.R.layout.view_toast);
        ToastUtils.setStyle(new WhiteToastStyle());
        ToastUtils.setGravity(Gravity.BOTTOM, 0, 200);
    }
}
