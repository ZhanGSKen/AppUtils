package cc.winboll.studio.libapputils;

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

public class LogUtils {

    public static final String TAG = "LogUtils";

    static SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:SS]", Locale.getDefault());

    //
    // Log Debug 函数
    //
    public static void d(String szTAG, String szMessage) {
        if (ExceptionHandlerApplication.getDebugFlag()) {
            saveLogDebug(szTAG, szMessage);
        }
    }

    public static void d(String szTAG, String szMessage, StackTraceElement[] listStackTrace) {
        if (ExceptionHandlerApplication.getDebugFlag()) {
            StringBuilder sbMessage = new StringBuilder(szMessage);
            sbMessage.append(" \nat ");
            sbMessage.append(listStackTrace[2].getMethodName());
            sbMessage.append(" (");
            sbMessage.append(listStackTrace[2].getFileName());
            sbMessage.append(":");
            sbMessage.append(listStackTrace[2].getLineNumber());
            sbMessage.append(")");
            saveLogDebug(szTAG, sbMessage.toString());
        }
    }

    public static void d(String szTAG, Exception e, StackTraceElement[] listStackTrace) {
        if (ExceptionHandlerApplication.getDebugFlag()) {
            StringBuilder sbMessage = new StringBuilder(e.getClass().toGenericString());
            sbMessage.append(" : ");
            sbMessage.append(e.getMessage());
            sbMessage.append(" \nat ");
            sbMessage.append(listStackTrace[2].getMethodName());
            sbMessage.append(" (");
            sbMessage.append(listStackTrace[2].getFileName());
            sbMessage.append(":");
            sbMessage.append(listStackTrace[2].getLineNumber());
            sbMessage.append(")");
            //StackTraceElement[] listStackTrace = e.getStackTrace();
            /*for (StackTraceElement stackTrace : listStackTrace) {
             sbMessage.append(" \nat ");
             sbMessage.append(stackTrace.getMethodName());
             sbMessage.append(" (");
             sbMessage.append(stackTrace.getFileName());
             sbMessage.append(":");
             sbMessage.append(stackTrace.getLineNumber());
             sbMessage.append(")");
             }*/
            saveLogDebug(szTAG, sbMessage.toString());
        }
    }

    //
    // Log Info 函数
    //
    public static void i(String szTAG, String szMessage) {
        saveLogInfo(szMessage);
    }


    //
    // 日志文件保存函数
    //
    static void saveLogInfo(String szMessage) {
        try {
            File fLog = new File(ExceptionHandlerApplication._mszLogFilePath);
            //FileWriter fw = new FileWriter(fLog, Charset.defaultCharset(), true);
            //fw.append(mSimpleDateFormat.format(System.currentTimeMillis()) + "[" + szTAG + "]: " + szMessage + "\n");
            //fw.close();
            BufferedWriter out = null;
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fLog, true), "UTF-8"));
            out.write(mSimpleDateFormat.format(System.currentTimeMillis()) + " " + szMessage + "\n");
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
            File fLog = new File(ExceptionHandlerApplication._mszLogFilePath);
            //FileWriter fw = new FileWriter(fLog, Charset.defaultCharset(), true);
            //fw.append(mSimpleDateFormat.format(System.currentTimeMillis()) + "[" + szTAG + "]: " + szMessage + "\n");
            //fw.close();
            BufferedWriter out = null;
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fLog, true), "UTF-8"));
            out.write(mSimpleDateFormat.format(System.currentTimeMillis()) + " [" + szTAG + "] : " + szMessage + "\n");
            out.close();

        } catch (IOException e) {
            LogUtils.d(TAG, "IOException : " + e.getMessage());
        }
    }

    //
    // 历史日志加载函数
    //
    public static String loadLog() {
        File fLog = new File(ExceptionHandlerApplication._mszLogFilePath);
        if (fLog.exists()) {
            StringBuffer sb = new StringBuffer();
            try {
                //FileInputStream fileInputStream = new FileInputStream(fLog);
                //int size = fileInputStream.available();
                //for (int i = 0; i < size; i++) {
                //    sb.append((char) fileInputStream.read());
                //}
                BufferedReader in = null;
                in = new BufferedReader(new InputStreamReader(new FileInputStream(fLog), "UTF-8"));
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
        File fLog = new File(ExceptionHandlerApplication._mszLogFilePath);
        if (fLog.exists()) {
            fLog.delete();
        }
    }

}
