package cc.winboll.studio.apputils.beans;

/**
 * @Author ZhanGSKen@QQ.COM
 * @Date 2024/07/01 13:49:34
 * @Describe UserBean
 */
import android.util.JsonReader;
import android.util.JsonWriter;
import cc.winboll.studio.libapputils.BaseBean;
import java.io.IOException;

public class UserBean extends BaseBean {

    public static final String TAG = "UserBean";

    int userId;
    
    String userName;
    
    public UserBean() {
    }
    
    public UserBean(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
    
    @Override
    public String getName() {
        return UserBean.class.getName();
    }

    @Override
    public void writeThisToJsonWriter(JsonWriter jsonWriter) throws IOException {
        super.writeThisToJsonWriter(jsonWriter);
        UserBean bean = this;
        jsonWriter.name("userId").value(bean.getUserId());
        jsonWriter.name("userName").value(bean.getUserName());
    }

    @Override
    public BaseBean readBeanFromJsonReader(JsonReader jsonReader) throws IOException {
        UserBean bean = new UserBean();
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            if (name.equals("userId")) {
                bean.setUserId(jsonReader.nextInt());
            } else if (name.equals("userName")) {
                bean.setUserName(jsonReader.nextString());
            } else {
                jsonReader.skipValue();
            }
        }
        // 结束 JSON 对象
        jsonReader.endObject();
        return bean;
    }

}
   
