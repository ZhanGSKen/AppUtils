package cc.winboll.studio.apputils;

import cc.winboll.studio.libapputils.BaseApplication;

public class App extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }
    
    public static boolean isDebug(){
        return BuildConfig.DEBUG;
    }
    
    public static void setIsDebug(boolean isDebug){
        
    }
}
