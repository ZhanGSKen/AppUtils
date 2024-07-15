package cc.winboll.studio.apputils.activities;

/**
 * @Author ZhanGSKen@QQ.COM
 * @Date 2024/07/15 18:07:25
 * @Describe TestRSAKeyUtils 测试窗口
 */
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cc.winboll.studio.apputils.R;
import cc.winboll.studio.libapputils.FileUtils;
import cc.winboll.studio.libapputils.LogUtils;
import cc.winboll.studio.libapputils.RSAKeyUtils;
import cc.winboll.studio.libapputils.views.LogView;
import com.hjq.toast.ToastUtils;
import java.io.File;
import java.io.IOException;

public class TestRSAKeyUtilsActivity extends Activity {

    public static final String TAG = "TestRSAKeyUtilsActivity";

    private static final int MSG_KEY_PAIR_CREATED = 0;

    static TestRSAKeyUtilsActivity _mTestRSAKeyUtilsActivity;
    static String _mszKeyName = "anonymous";

    Button btnCreateRSAKeyPair;
    LogView mLogView;
    String mszRSAKeysPath;
    File mfPublic;
    EditText metPublic;
    File mfPrivate;
    EditText metPrivate;
    TextView mtvMSG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testrsakeyutils);

        mLogView = findViewById(R.id.logview);
        mLogView.start();

        _mTestRSAKeyUtilsActivity = this;

        mszRSAKeysPath = getRSAKeysPath(this);
        mfPublic = new File(getRSAPublicKeyPath(this));
        mfPrivate = new File(getRSAPrivateKeyPath(this));

        btnCreateRSAKeyPair = findViewById(R.id.activitytestrsakeyutilsButton1);
        metPublic = findViewById(R.id.activitytestrsakeyutilsEditText1);
        metPrivate = findViewById(R.id.activitytestrsakeyutilsEditText2);
        mtvMSG = findViewById(R.id.activitytestrsakeyutilsTextView1);

        showKeyPairInfo();
    }

    static String getRSAKeysPath(Context context) {
        return context.getDataDir().toString() + "/home/.ssh/" + _mszKeyName;
    }

    public static String getRSAPublicKeyPath(Context context) {
        return RSAKeyUtils.getPublicKeyKeyPath(_mszKeyName, getRSAKeysPath(context));
    }

    public static String getRSAPrivateKeyPath(Context context) {
        return RSAKeyUtils.getPrivateKeyPath(_mszKeyName, getRSAKeysPath(context));
    }

    public void onCreateRSAKeyPair(View view) {
        if (!mfPublic.exists() || !mfPrivate.exists()) {
            btnCreateRSAKeyPair.setEnabled(false);
            createRSAKeyPair();
        }
    }

    void createRSAKeyPair() {
        (new Thread() {
            @Override
            public void run() {
                ToastUtils.show("Create RSA Key Pair");
                if (RSAKeyUtils.genKeyPair(_mTestRSAKeyUtilsActivity, _mszKeyName, getRSAKeysPath(_mTestRSAKeyUtilsActivity))) {
                    Message message = _mTestRSAKeyUtilsActivity.mHandler.obtainMessage(MSG_KEY_PAIR_CREATED);
                    TestRSAKeyUtilsActivity.sendMessage(message);
                    LogUtils.d(TAG, "Create RSA Key Pair done.");
                    ToastUtils.show("Create RSA Key Pair done."); 
                }
            }
        }).start();
    }

    void showKeyPairInfo() {
        if (mfPublic.exists() && mfPrivate.exists()) {

            mtvMSG.setText("Key Pair Created.");
            mtvMSG.setTextColor(Color.BLACK);
            btnCreateRSAKeyPair.setEnabled(false);

            try {
                metPublic.setText(FileUtils.readStringFromFile(mfPublic.getPath()));
                metPrivate.setText(FileUtils.readStringFromFile(mfPrivate.getPath()));
            } catch (IOException e) {
                LogUtils.d(TAG, e, Thread.currentThread().getStackTrace());
            }
        } else {
            mtvMSG.setText("There is no RSA key pair yet.");
            mtvMSG.setTextColor(Color.RED);
            btnCreateRSAKeyPair.setEnabled(true);
        }
    }

    public void onCopyPublicKey(View view) {
        copyFile2Clipboard(mfPublic);
    }

    public void onCopyPrivateKey(View view) {
        copyFile2Clipboard(mfPrivate);
    }

    void copyFile2Clipboard(File file) {
        try {
            // Gets a handle to the clipboard service.
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            // Creates a new text clip to put on the clipboard
            ClipData clip = ClipData.newPlainText("simple text", FileUtils.readStringFromFile(file.getPath()));
            // Set the clipboard's primary clip.
            clipboard.setPrimaryClip(clip);
            String szMSG = file.getName() + " is copy to clipboard.";
            LogUtils.d(TAG, szMSG);
            ToastUtils.show(szMSG);
        } catch (IOException e) {
            LogUtils.d(TAG, e, Thread.currentThread().getStackTrace());
        }
    }

    Handler mHandler = new Handler(){

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_KEY_PAIR_CREATED : {
                        showKeyPairInfo();
                        //ToastUtils.show("MSG_KEY_PAIR_CREATED");
                        break;
                    }
            }
            super.handleMessage(msg);
        }
    };

    public static void sendMessage(Message msg) {
        if (_mTestRSAKeyUtilsActivity != null) {
            _mTestRSAKeyUtilsActivity.mHandler.sendMessage(msg);
        }
    }

}
