package cc.winboll.studio.libapputils;

/**
 * @Author ZhanGSKen@QQ.COM
 * @Date 2024/06/09 10:26:57
 * @Describe 应用日志类
 */
import android.content.Context;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.Collections;
import java.nio.file.StandardOpenOption;

public class LogUtils {

    public static final String TAG = "LogUtils";

    // 日志显示时间格式
    static SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("[yyyyMMdd_HHmmSS]", Locale.getDefault());
    // 应用日志文件夹
    static File _mfLogDir;
    // 应用日志文件
    static File _mfLogFile;;

    //
    // 初始化函数
    //
    public static void init(Context context) {
        // 初始化日志读写文件路径
        _mfLogDir = new File(context.getExternalCacheDir(), TAG);
        if (!_mfLogDir.exists()) {
            _mfLogDir.mkdirs();
        }
        _mfLogFile = new File(_mfLogDir, "log.txt");
    }

    //
    // 获取应用日志文件夹
    //
    public static File getLogDir() {
        return _mfLogDir;
    }

    //
    // 调试日志写入函数
    //
    public static void d(String szTAG, String szMessage) {
        if (BaseApplication.isDebug()) {
            saveLogDebug(szTAG, szMessage);
        }
    }

    //
    // 调试日志写入函数
    // 包含线程调试堆栈信息
    //
    public static void d(String szTAG, String szMessage, StackTraceElement[] listStackTrace) {
        if (BaseApplication.isDebug()) {
            StringBuilder sbMessage = new StringBuilder(szMessage);
            sbMessage.append(" \nAt ");
            sbMessage.append(listStackTrace[2].getMethodName());
            sbMessage.append(" (");
            sbMessage.append(listStackTrace[2].getFileName());
            sbMessage.append(":");
            sbMessage.append(listStackTrace[2].getLineNumber());
            sbMessage.append(")");
            saveLogDebug(szTAG, sbMessage.toString());
        }
    }

    //
    // 调试日志写入函数
    // 包含异常信息和线程调试堆栈信息
    //
    public static void d(String szTAG, Exception e, StackTraceElement[] listStackTrace) {
        if (BaseApplication.isDebug()) {
            StringBuilder sbMessage = new StringBuilder(e.getClass().toGenericString());
            sbMessage.append(" : ");
            sbMessage.append(e.getMessage());
            sbMessage.append(" \nAt ");
            sbMessage.append(listStackTrace[2].getMethodName());
            sbMessage.append(" (");
            sbMessage.append(listStackTrace[2].getFileName());
            sbMessage.append(":");
            sbMessage.append(listStackTrace[2].getLineNumber());
            sbMessage.append(")");
            saveLogDebug(szTAG, sbMessage.toString());
        }
    }

    //
    // 应用信息日志写入函数
    //
    public static void i(String szTAG, String szMessage) {
        saveLogInfo(szMessage);
    }

    //
    // 日志文件保存函数
    //
    static void saveLogInfo(String szMessage) {
        try {

            BufferedWriter out = null;
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(_mfLogFile, true), "UTF-8"));
            out.write(mSimpleDateFormat.format(System.currentTimeMillis()) + ": " + szMessage + "\n");
            out.close();

        } catch (IOException e) {
            LogUtils.d(TAG, "IOException : " + e.getMessage());
        }
    }

    //
    // 日志文件保存函数
    //
    static void saveLogDebug(String szTAG, String szMessage) {
        try {
            BufferedWriter out = null;
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(_mfLogFile, true), "UTF-8"));
            out.write(mSimpleDateFormat.format(System.currentTimeMillis()) + "[" + szTAG + "]: " + szMessage + "\n");
            out.close();
        } catch (IOException e) {
            LogUtils.d(TAG, "IOException : " + e.getMessage());
        }
    }

    //
    // 历史日志加载函数
    //
    public static String loadLog() {
        if (_mfLogFile.exists()) {
            StringBuffer sb = new StringBuffer();
            try {
                BufferedReader in = null;
                in = new BufferedReader(new InputStreamReader(new FileInputStream(_mfLogFile), "UTF-8"));
                String line = "";
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
            } catch (IOException e) {
                LogUtils.d(TAG, "IOException : " + e.getMessage());
            } 
            return sb.toString();
        }
        return "";
    }

    //
    // 清理日志函数
    //
    public static void cleanLog() {
        if (_mfLogFile.exists()) {
            try {
                FileUtils.writeStringToFile(_mfLogFile.getPath(), "");
                LogUtils.d(TAG, "cleanLog");
            } catch (IOException e) {
                LogUtils.d(TAG, e, Thread.currentThread().getStackTrace());
            }
        }
    }
    
    
}
