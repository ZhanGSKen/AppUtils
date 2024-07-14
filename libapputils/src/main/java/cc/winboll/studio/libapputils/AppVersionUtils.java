package cc.winboll.studio.libapputils;

/**
 * @Author ZhanGSKen@QQ.COM
 * @Date 2024/07/14 16:55:46
 * @Describe 应用版本工具集
 */
import com.hjq.toast.ToastUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppVersionUtils {
    
    public static final String TAG = "AppVersionUtils";
    
    //
    // 检查新版本是否成立
    // szCurrentCode : 当前版本应用包名
    // szNextCode : 新版本应用包名
    // 返回 ：情况1：当前版本是发布版
    //       返回 true (新版本 > 当前版本)
    //       情况1：当前版本是Beta版
    //       true 新版本 == 当前版本
    //
    public static boolean isHasNewVersion(String szCurrentName, String szNextName) {
        //boolean isVersionNewer = false;
        //if(szCurrentName.equals(szNextName)) {
        //    isVersionNewer = false;
        //} else {
            //ToastUtils.show("szCurrent : " + szCurrent + "\nszNext : " + szNext);
            //int nApk = szNextName.lastIndexOf(".apk");
            //ToastUtils.show("nApk : " + Integer.toString(nApk));
            //String szNextNoApkName = szNextName.substring(0, nApk);
            //ToastUtils.show("szNextNoApkName : " + szNextNoApkName);
            //String szCurrentNoApkName = szCurrentName.substring(0, szNextNoApkName.length());
            //ToastUtils.show("szCurrentNoApkName : " + szCurrentNoApkName);
            //String str1 = "3.4.50";
            //String str2 = "3.3.60";
            //String str1 = getCodeInPackageName(szCurrentName);
            //String str2 = getCodeInPackageName(szNextName);
            //String str1 = getCodeInPackageName(szNextName);
            //String str2 = getCodeInPackageName(szCurrentName);
            //Boolean isVersionNewer2 = checkNewVersion(str1,str2);
            //ToastUtils.show("isVersionNewer2 : " + Boolean.toString(isVersionNewer2));
            //ToastUtils.show(checkNewVersion(getCodeInPackageName(szCurrentName), getCodeInPackageName(szNextName)));
            //return checkNewVersion(getCodeInPackageName(szCurrentName), getCodeInPackageName(szNextName));
        //}
        //return isVersionNewer;
        if(checkNewVersion(getCodeInPackageName(szCurrentName), getCodeInPackageName(szNextName))) {
            return true;
        } else {
            if(szCurrentName.matches(".*_\\d+\\.\\d+\\.\\d+-beta.*\\.apk")) {
                LogUtils.d(TAG, "Current Name is Beta");
                if(getReleasePackageName(szCurrentName).equals(szNextName)) {
                    LogUtils.d(TAG, "App beta version is released. Release name : " + szNextName);
                    return true;
                }
            }
        }

        return false;
    }
    
    //
    // 检查新版本是否成立
    // szCurrentCode : 当前版本
    // szNextCode : 新版本
    // 返回 ：true 新版本 > 当前版本
    //
    public static Boolean checkNewVersion (String szCurrentCode, String szNextCode){
        boolean isNew = false;
        String[] appVersion1 = szCurrentCode.split("\\.");
        String[] appVersion2 = szNextCode.split("\\.");
        //根据位数最短的判断
        int lim = appVersion1.length > appVersion2.length ? appVersion2.length : appVersion1.length;
        //根据位数循环判断各个版本
        for(int i = 0; i < lim; i++){
            if(Integer.parseInt(appVersion2[i]) > Integer.parseInt(appVersion1[i])){
                isNew = true;
                return isNew;
            }
        }
        return isNew;
    }
    
    //
    // 截取应用包名称版本号信息
    // 如 ：AppUtils_7.0.4-beta1_0120.apk 版本号为 7.0.4
    // 如 ：AppUtils_7.0.4.apk 版本号为 7.0.4
    //
    public static String getCodeInPackageName(String apkName) {
        //String apkName = "AppUtils_7.0.0.apk";
        Pattern pattern = Pattern.compile("\\d+\\.\\d+\\.\\d+");
        Matcher matcher = pattern.matcher(apkName);
        if (matcher.find()) {
            String version = matcher.group();
            return version;
            //System.out.println("Version number: " + version); // 输出：7.0.0
        }
        return "";
    }
    
    //
    // 根据Beta版名称生成发布版应用包名称
    // 如 AppUtils_7.0.4-beta1_0120.apk
    // 发布版名称就为AppUtils_7.0.4.apk
    //
    public static String getReleasePackageName(String szBetaPackageName) {
        //String szBetaPackageName = "AppUtils_7.0.4-beta1_0120.apk";
        Pattern pattern = Pattern.compile(".*\\d+\\.\\d+\\.\\d+");
        Matcher matcher = pattern.matcher(szBetaPackageName);
        if (matcher.find()) {
            String szReleasePackageName = matcher.group();
            return szReleasePackageName + ".apk";
            //System.out.println("Version number: " + version); // 输出：7.0.0
        }
        return "";
    }
    
}