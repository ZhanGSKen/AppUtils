package cc.winboll.studio.libapputils;

/**
 * @Author ZhanGSKen@QQ.COM
 * @Date 2024/07/15 17:36:37
 * @Describe RSA 秘钥工具
 */
import android.content.Context;
import cc.winboll.studio.libapputils.LogUtils;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.KeyPair;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RSAKeyUtils {

    public static final String TAG = "RSAKeyUtils";

    static Map<String, String> genKeyMap(String comment, int type, int key_size) {
        Map<String,String> keys = new HashMap<>();
        //int type = KeyPair.RSA;
        //int type = KeyPair.ECDSA;
        JSch jsch = new JSch();
        try {
            KeyPair kpair = KeyPair.genKeyPair(jsch, type, key_size);
            //私钥
            ByteArrayOutputStream baos = new ByteArrayOutputStream();//向OutPutStream中写入
            kpair.writePrivateKey(baos);
            String privateKeyString = baos.toString();
            //公钥
            baos = new ByteArrayOutputStream();
            kpair.writePublicKey(baos, comment);
            String publicKeyString = baos.toString();
            LogUtils.d(TAG, "Finger print: " + kpair.getFingerPrint());
            //System.out.println("Finger print: " + kpair.getFingerPrint());
            kpair.dispose();
            // 得到公钥字符串
//          String publicKeyString = RSAEncrypt.loadPublicKeyByFile(filePath,filename + ".pub");
//          System.out.println(publicKeyString.length());
            //System.out.println(publicKeyString);
            keys.put("publicKey", publicKeyString);
            // 得到私钥字符串
//          String privateKeyString = RSAEncrypt.loadPrivateKeyByFile(filePath,filename);
//          System.out.println(privateKeyString.length());
            //System.out.println(privateKeyString);
            keys.put("privateKey", privateKeyString);
        } catch (Exception e) {
            LogUtils.d(TAG, e, Thread.currentThread().getStackTrace());
            //System.out.println(e);
        }
        return keys;
    }

    //
    // 生成秘钥对
    //
    public static boolean genKeyPair(Context context, String szKeyName, String szKeyDir) {
        File fKeyDir = new File(szKeyDir);
        Map<String, String> keyMap = genKeyMap(szKeyName + "@" + context.getPackageName(), KeyPair.RSA, 2048);
        if (!fKeyDir.exists()) {
            fKeyDir.mkdirs();
        }
        try {
            FileUtils.writeStringToFile(getPublicKeyKeyPath(szKeyName, szKeyDir), keyMap.get("publicKey"));
            FileUtils.writeStringToFile(getPrivateKeyPath(szKeyName, szKeyDir), keyMap.get("privateKey"));
            return true;
        } catch (IOException e) {
            LogUtils.d(TAG, e, Thread.currentThread().getStackTrace());
        }
        return false;
    }

    //
    // 获取秘钥对公钥路径
    //
    public static String getPublicKeyKeyPath(String szKeyName, String szKeyDir) {
        return szKeyDir + "/" + szKeyName + ".pub";
    }

    //
    // 获取秘钥对私钥路径
    //
    public static String getPrivateKeyPath(String szKeyName, String szKeyDir) {
        return szKeyDir + "/" + szKeyName;
    }
}
