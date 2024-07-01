package cc.winboll.studio.apputils.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import cc.winboll.studio.apputils.R;
import cc.winboll.studio.apputils.beans.AppConfigBean;
import cc.winboll.studio.apputils.beans.UserBean;
import java.util.ArrayList;

/**
 * @Author ZhanGSKen@QQ.COM
 * @Date 2024/07/01 12:59:17
 * @Describe BaseBean测试窗口
 */
public class BeanActivity extends Activity {

    public static final String TAG = "BeanActivity";

    Switch swIsEnableService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bean);

        initView();
    }

    void initView() {
        swIsEnableService = findViewById(R.id.activitybeanSwitch1);
        AppConfigBean bean = AppConfigBean.loadBean(this, AppConfigBean.class);
        if (bean != null) {
            swIsEnableService.setChecked(bean.isEnableService());
        }
        
        showUserList();
    }

    public void onSetIsEnableService(View view) {
        AppConfigBean bean = AppConfigBean.loadBean(this, AppConfigBean.class);
        if (bean == null) {
            bean = new AppConfigBean();
        }
        bean.setIsEnableService(swIsEnableService.isChecked());
        AppConfigBean.saveBean(this, bean);
    }

    public void onDeleteUser(View view) {
        EditText etDeleteUserId = findViewById(R.id.activitybeanEditText1);
        int nDeleteUserId = Integer.parseInt(etDeleteUserId.getText().toString().trim());
        ArrayList<UserBean> beanList = new ArrayList<UserBean>();
        if (AppConfigBean.loadBeanList(this, beanList, UserBean.class)) {
            for (int i = 0; i < beanList.size(); i++) {
                if (beanList.get(i).getUserId() == nDeleteUserId) {
                    beanList.remove(i);
                }
            }

        }
        AppConfigBean.saveBeanList(this, beanList, UserBean.class);
        showUserList();
    }

    public void onAddUser(View view) {
        EditText etAddUserId = findViewById(R.id.activitybeanEditText1);
        int nAddUserId = Integer.parseInt(etAddUserId.getText().toString().trim());
        ArrayList<UserBean> beanList = new ArrayList<UserBean>();
        AppConfigBean.loadBeanList(this, beanList, UserBean.class);
        UserBean bean = new UserBean();
        bean.setUserId(nAddUserId);
        bean.setUserName("UserName" + Integer.toString(nAddUserId));
        beanList.add(bean);
        AppConfigBean.saveBeanList(this, beanList, UserBean.class);
        showUserList();
    }

    void showUserList() {
        TextView tvUserList = findViewById(R.id.activitybeanTextView1);
        tvUserList.setText("User List : \n");
        ArrayList<UserBean> beanList = new ArrayList<UserBean>();
        if(AppConfigBean.loadBeanList(this, beanList, UserBean.class)) {
            for(UserBean bean : beanList) {
                tvUserList.append("\nUser Id : " + Integer.toString(bean.getUserId()));
                tvUserList.append("\nUser Name : " + bean.getUserName());
                tvUserList.append("\n");
            }
        }
    }
}
