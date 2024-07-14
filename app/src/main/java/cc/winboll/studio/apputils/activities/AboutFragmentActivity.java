package cc.winboll.studio.apputils.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import cc.winboll.studio.apputils.R;
import cc.winboll.studio.libapputils.fragments.AboutFragment;

/**
 * @Author ZhanGSKen@QQ.COM
 * @Date 2024/07/14 13:20:33
 * @Describe AboutFragment Test
 */
public class AboutFragmentActivity extends Activity {

    public static final String TAG = "AboutFragmentActivity";

    AboutFragment mAboutFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutfragment);

        if (mAboutFragment == null) {
            //mAboutFragment = new AboutFragment(this, "", "", R.drawable.ic_launcher);
            mAboutFragment = new AboutFragment(this, "AppUtils", "Desc456", R.drawable.ic_launcher_background);
            FragmentTransaction tx = getFragmentManager().beginTransaction();
            tx.add(R.id.activityaboutfragmentFrameLayout1, mAboutFragment);
            tx.show(mAboutFragment);
            tx.commit();
        }
    }

}
