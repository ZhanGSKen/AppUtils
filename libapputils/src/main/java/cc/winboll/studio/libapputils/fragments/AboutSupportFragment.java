package cc.winboll.studio.libapputils.fragments;

/**
 * @Author ZhanGSKen@QQ.COM
 * @Date 2024/07/13 17:02:57
 * @Describe AboutFragment
 */
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;
import cc.winboll.studio.libapputils.R;
import cc.winboll.studio.libapputils.LogUtils;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutSupportFragment extends Fragment {

    public static final String TAG = "AboutFragment";

    Context mContext;
    String mszAppName;
    String mszAppDescription;
    String mszHomePage;
    String mszGitWeb;
    int mnAppIcon = 0;

    public AboutSupportFragment(Context context, String szAppName, String szAppDescription, int nAppIcon) {
        mContext = context;
        mszAppName = szAppName;
        mszAppDescription = szAppDescription;
        mnAppIcon = nAppIcon;
        mszHomePage = "https://winboll.cc/studio/details.php?app=" + mszAppName;
        mszGitWeb = "https://winboll.cc/gitweb/" + mszAppName + ".git";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewMain = inflater.inflate(R.layout.fragment_about, container, false);
        // 初始化标题栏
        //setSubtitle(getContext().getString(R.string.text_about));
        LinearLayout llMain = viewMain.findViewById(R.id.fragmentaboutLinearLayout1);
        llMain.addView(createAboutPage());
        return viewMain;
    }

    protected View createAboutPage() {
        Element elementGitWeb = new Element("GitWeb", R.drawable.ic_launcher);
        elementGitWeb.setOnClickListener(mElementGitWebOnClickListener);
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
            .setImage((mnAppIcon == 0)?R.drawable.ic_launcher:mnAppIcon)
            //.addItem(versionElement)
            //.addItem(adsElement)
            //.addGroup("Connect with us")
            .addEmail("ZhanGSKen@QQ.COM")
            .addWebsite(mszHomePage)
            .addItem(elementGitWeb)
            //.addFacebook("the.medy")
            //.addTwitter("medyo80")
            //.addYoutube("UCdPQtdWIsg7_pi4mrRu46vA")
            //.addPlayStore("com.ideashower.readitlater.pro")
            //.addGitHub("medyo")
            //.addInstagram("medyo80")
            .create();
        return aboutPage;
    }

    View.OnClickListener mElementGitWebOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mszGitWeb));
            startActivity(browserIntent);
        }
    };
}
