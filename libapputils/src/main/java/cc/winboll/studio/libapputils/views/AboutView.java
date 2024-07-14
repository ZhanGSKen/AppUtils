package cc.winboll.studio.libapputils.views;

/**
 * @Author ZhanGSKen@QQ.COM
 * @Date 2024/07/15 01:28:27
 * @Describe AboutView
 */
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import cc.winboll.studio.libapputils.AppVersionUtils;
import cc.winboll.studio.libapputils.LogUtils;
import cc.winboll.studio.libapputils.R;
import cc.winboll.studio.libapputils.dialogs.YesNoAlertDialog;
import com.hjq.toast.ToastUtils;
import java.io.IOException;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AboutView extends LinearLayout {

    public static final String TAG = "AboutView";

    public static final int MSG_APPUPDATE_CHECKED = 0;

    Context mContext;
    String mszAppName = "";
    String mszAppVersionName = "";
    String mszCurrentAppPackageName = "";
    volatile String mszNewestAppPackageName = "";
    String mszAppDescription = "";
    String mszHomePage = "";
    String mszGitWeb = "";
    int mnAppIcon = 0;
    
    public AboutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }
    
    void initView(Context context, AttributeSet attrs) {
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AboutView);
        mszAppName = typedArray.getString(R.styleable.AboutView_projectname);
        mszAppDescription = typedArray.getString(R.styleable.AboutView_projectdescription);
        mnAppIcon = typedArray.getResourceId(R.styleable.AboutView_projecticon, R.drawable.ic_launcher);
        // 返回一个绑定资源结束的信号给资源
        typedArray.recycle();
        
        try {
            mszAppVersionName = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.d(TAG, e, Thread.currentThread().getStackTrace());
        }
        mszCurrentAppPackageName = mszAppName + "_" + mszAppVersionName + ".apk";
        mszHomePage = "https://winboll.cc/studio/details.php?app=" + mszAppName;
        mszGitWeb = "https://winboll.cc/gitweb/" + mszAppName + ".git";

        addView(createAboutPage());
        // 初始化标题栏
        //setSubtitle(getContext().getString(R.string.text_about));
        //LinearLayout llMain = findViewById(R.id.viewaboutLinearLayout1);
        //llMain.addView(createAboutPage());
    }
    
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_APPUPDATE_CHECKED : {
                        if (!AppVersionUtils.isHasNewVersion(mszCurrentAppPackageName, mszNewestAppPackageName)) {
                            ToastUtils.delayedShow("Current app is the neweest.", 5000);
                        } else {
                            String szMsg = "Current app is :\n[ " + mszCurrentAppPackageName
                                + " ]\nThe newest app is :\n[ " + mszNewestAppPackageName
                                + " ]\nIs download the newest app?";
                            YesNoAlertDialog.show(mContext, "Application Update Prompt", szMsg, mIsDownlaodUpdateListener);
                        }
                        break;
                    }
            }
        }
    };

    protected View createAboutPage() {
        // 定义 GitWeb 按钮
        //
        Element elementGitWeb = new Element("GitWeb", R.drawable.ic_launcher);
        elementGitWeb.setOnClickListener(mGitWebOnClickListener);
        // 定义检查更新按钮
        //
        Element elementAppUpdate = new Element("APP Update", R.drawable.ic_launcher);
        elementAppUpdate.setOnClickListener(mAppUpdateOnClickListener);

        String szAppInfo = "";
        try {
            szAppInfo = mszAppName + " "
                + mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName
                + "\n" + mszAppDescription;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.d(TAG, e, Thread.currentThread().getStackTrace());
        }
        View aboutPage = new AboutPage(mContext)
            .setDescription(szAppInfo)
            //.isRTL(false)
            //.setCustomFont(String) // or Typeface
            .setImage(mnAppIcon)
            //.addItem(versionElement)
            //.addItem(adsElement)
            //.addGroup("Connect with us")
            .addEmail("ZhanGSKen@QQ.COM")
            .addWebsite(mszHomePage)
            .addItem(elementGitWeb)
            .addItem(elementAppUpdate)
            //.addFacebook("the.medy")
            //.addTwitter("medyo80")
            //.addYoutube("UCdPQtdWIsg7_pi4mrRu46vA")
            //.addPlayStore("com.ideashower.readitlater.pro")
            //.addGitHub("medyo")
            //.addInstagram("medyo80")
            .create();
        return aboutPage;
    }

    View.OnClickListener mGitWebOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mszGitWeb));
            mContext.startActivity(browserIntent);
        }
    };

    View.OnClickListener mAppUpdateOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            ToastUtils.show("Start app update checking.");
            new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String szUrl = "https://winboll.cc/studio/details.php?app=" + mszAppName;
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                            .url(szUrl)
                            .header("Accept", "text/plain") // 设置正确的Content-Type头
                            .build();
                        Call call = client.newCall(request);
                        call.enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    // 处理网络请求失败
                                    LogUtils.i(TAG, e.getMessage());
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    if (!response.isSuccessful()) {
                                        LogUtils.i(TAG, "Unexpected code " + response);
                                        return;
                                    }

                                    // 读取响应体作为字符串，注意这里可能需要解码
                                    String text = response.body().string();
                                    org.jsoup.nodes.Document doc = org.jsoup.Jsoup.parse(text);
                                    //LogUtils.i(TAG, doc.text());

                                    // 使用id选择器找到具有特定id的元素
                                    org.jsoup.nodes.Element elementWithId = doc.select("#LastRelease").first(); // 获取第一个匹配的元素

                                    // 提取并打印元素的文本内容
                                    mszNewestAppPackageName = elementWithId.text();
                                    //ToastUtils.delayedShow(textContent + "\n" + mszCurrentAppPackageVersionName, 5000);

                                    mHandler.sendMessage(mHandler.obtainMessage(MSG_APPUPDATE_CHECKED));
                                }
                            });
                    }
                }).start();
        }
    };

    YesNoAlertDialog.OnDialogResultListener mIsDownlaodUpdateListener = new YesNoAlertDialog.OnDialogResultListener() {
        @Override
        public void onYes() {
            String szUrl = "https://winboll.cc/studio/download.php?appname=" + mszAppName + "&apkname=" + mszNewestAppPackageName;
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(szUrl));
            mContext.startActivity(browserIntent);
        }

        @Override
        public void onNo() {
        }
    };
}
