package cc.winboll.studio.apputils.beans;

/**
 * @Author ZhanGSKen@QQ.COM
 * @Date 2024/07/01 13:02:49
 * @Describe TestBean
 */
import android.util.JsonReader;
import android.util.JsonWriter;
import cc.winboll.studio.libapputils.BaseBean;
import java.io.IOException;

public class AppConfigBean extends BaseBean {

    public static final String TAG = "AppConfigBean";

    boolean isEnableService;
    
    public AppConfigBean() {
        this.isEnableService = false;
    }

    public AppConfigBean(boolean isEnableService) {
        this.isEnableService = isEnableService;
    }

    public void setIsEnableService(boolean isEnableService) {
        this.isEnableService = isEnableService;
    }

    public boolean isEnableService() {
        return isEnableService;
    }

    @Override
    public String getName() {
        return AppConfigBean.class.getName();
    }

    @Override
    public void writeThisToJsonWriter(JsonWriter jsonWriter) throws IOException {
        super.writeThisToJsonWriter(jsonWriter);
        jsonWriter.name("isEnableService").value(isEnableService());
    }

    @Override
    public BaseBean readBeanFromJsonReader(JsonReader jsonReader) throws IOException {
        AppConfigBean bean = new AppConfigBean();
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            if (name.equals("isEnableService")) {
                bean.setIsEnableService(jsonReader.nextBoolean());
            } else {
                jsonReader.skipValue();
            }
        }
        // 结束 JSON 对象
        jsonReader.endObject();
        return bean;
    }

}
