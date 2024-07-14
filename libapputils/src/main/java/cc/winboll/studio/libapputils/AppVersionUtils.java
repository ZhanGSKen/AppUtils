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
        if(szCurrentName.matches(".*_\\d+\\.\\d+\\.\\d+-beta.*\\.apk")) {
            LogUtils.d(TAG, "Current Name is Beta");
            if(getReleasePackageName(szCurrentName).equals(szNextName)) {
                LogUtils.d(TAG, "App beta version is released. Release name : " + szNextName);
                return true;
            }
        }
        return checkNewVersion(getCodeInPackageName(szCurrentName), getCodeInPackageName(szNextName));
    }
    
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
    
    public static String getReleasePackageName(String szBetaPackageName) {
        //String apkName = "AppUtils_7.0.0.apk";
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
