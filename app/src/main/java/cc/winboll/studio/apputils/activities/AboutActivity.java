package cc.winboll.studio.apputils.activities;

import android.app.Activity;
import android.os.Bundle;
import cc.winboll.studio.apputils.R;
import cc.winboll.studio.libapputils.views.AboutView;

/**
 * @Author ZhanGSKen@QQ.COM
 * @Date 2024/07/14 13:20:33
 * @Describe AboutFragment Test
 */
public class AboutActivity extends Activity {

    public static final String TAG = "AboutFragmentActivity";

    AboutView mAboutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mAboutView = findViewById(R.id.activityaboutAboutView1);
    }

}
