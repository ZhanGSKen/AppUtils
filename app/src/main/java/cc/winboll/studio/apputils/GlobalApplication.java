package cc.winboll.studio.apputils;

import cc.winboll.studio.libapputils.BaseApplication;

public class GlobalApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        setIsDebug(cc.winboll.studio.apputils.BuildConfig.DEBUG);
    }
}
