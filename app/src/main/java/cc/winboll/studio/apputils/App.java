package cc.winboll.studio.apputils;

import android.app.Application;

public class App extends Application {

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
