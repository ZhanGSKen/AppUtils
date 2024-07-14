package cc.winboll.studio.apputils.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import cc.winboll.studio.apputils.R;
import cc.winboll.studio.libapputils.fragments.AboutSupportFragment;

/**
 * @Author ZhanGSKen@QQ.COM
 * @Date 2024/07/14 13:19:45
 * @Describe AboutSupportFragment Test
 */
public class AboutSupportFragmentActivity extends AppCompatActivity {

    public static final String TAG = "AboutSupportFragmentActivity";

    AboutSupportFragment mAboutSupportFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutsupportfragment);

        if (mAboutSupportFragment == null) {
            mAboutSupportFragment = new AboutSupportFragment(this, "AppUtils", "WinBoll 安卓应用基础类库。", R.drawable.ic_launcher);
            //mAboutSupportFragment = new AboutSupportFragment(this, "AppUtils", "WinBoll 安卓应用基础类库。", R.drawable.ic_launcher_background);
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.add(R.id.activityaboutsupportfragmentFrameLayout1, mAboutSupportFragment);
            tx.show(mAboutSupportFragment);
            tx.commit();
        }
    }

}
