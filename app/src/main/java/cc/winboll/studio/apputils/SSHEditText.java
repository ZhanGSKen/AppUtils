package cc.winboll.studio.apputils;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import cc.winboll.studio.libapputils.LogUtils;
import java.util.ArrayList;

public class SSHEditText extends EditText {

    public String TAG = "SSHEditText";

    Context mContext;
    StringBuilder mStringBuilder = new StringBuilder("Wellcome to WinBoll!");
    static String _mszStatus = "~$ ";
    ArrayList<String> mszCommandList = new ArrayList<String>();
    IOnCommandListener mIOnCommandListener;

    public SSHEditText(Context context) {
        super(context);
        initView(context);
    }

    public SSHEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SSHEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public SSHEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    void initView(Context context) {
        mContext = context;
        mStringBuilder.append("\n");
        mStringBuilder.append(_mszStatus);
        setText(mStringBuilder.toString());

        addTextChangedListener(mTextWatcher);

        setOnEditorActionListener(mOnEditorActionListener);
    }

    TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
// 回车键被按下
// 进行相应的操作
                LogUtils.d(TAG, "mOnEditorActionListener");
                readInputCommand();
                return true;
            }
            return false;
        }
    };

	TextWatcher mTextWatcher = new TextWatcher() {


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().trim().endsWith("\n")) {
// 回车键被按下
// 进行相应的操作
                LogUtils.d(TAG, "afterTextChanged");
                readInputCommand();
            }
        }
    };

    String[] readInputCommand() {
        String szAllInput = getText().toString();
        String szNewInput = szAllInput.substring(mStringBuilder.length());
        return szNewInput.split("\n");
    }

    public interface IOnCommandListener {
        void onCommandListener(String szCommand);
    }

    public void setOnCommandListener(IOnCommandListener iOnCommandListener) {
        mIOnCommandListener = iOnCommandListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean ret = super.onTouchEvent(event);
        int nAction = event.getAction() & MotionEvent.ACTION_MASK;
        //if (isEnabled() && isClickable() && isFocusable() && (event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
        if (nAction == MotionEvent.ACTION_DOWN
            || nAction == MotionEvent.AXIS_PRESSURE
            || nAction == MotionEvent.ACTION_UP) {
            LogUtils.d(TAG, "onTouchEvent " + Integer.toString(nAction));
            setSelection(getText().length());
            //InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            //imm.hideSoftInputFromWindow(getWindowToken(), 0);
            ret = true;
        } else {
            //LogUtils.d(TAG, "");
        }
        return ret;
    }
}
