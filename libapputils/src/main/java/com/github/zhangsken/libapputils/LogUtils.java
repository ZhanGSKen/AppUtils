package com.github.zhangsken.libapputils;

import java.io.*;
import java.text.*;
import java.util.*;

public class LogUtils {

    public static final String TAG = "LogUtils";

    static SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS", Locale.getDefault());

    //
    // Log Debug 函数
    //
    public static void d(String szTAG, String szMessage) {
        if (BaseApplication.getDebugFlag()) {
            saveLog(szTAG, szMessage);
        }
    }

    //
    // Log Info 函数
    //
    public static void i(String szTAG, String szMessage) {
        saveLog(szTAG, szMessage);
    }

    //
    // 日志文件保存函数
    //
    static void saveLog(String szTAG, String szMessage) {
        try {
            File fLog = new File(BaseApplication._mszLogFilePath);
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
        File fLog = new File(BaseApplication._mszLogFilePath);
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
        File fLog = new File(BaseApplication._mszLogFilePath);
        if (fLog.exists()) {
            fLog.delete();
        }
    }

}
