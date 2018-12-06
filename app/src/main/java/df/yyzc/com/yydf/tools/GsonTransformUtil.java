package df.yyzc.com.yydf.tools;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import df.yyzc.com.yydf.base.javavo.YYBaseResBean;

/**
 * gson解析工具
 *
 * @author zhangyu
 */
public class GsonTransformUtil {

    public static YYBaseResBean fromJson(String gsonStr, Class clazz) {
        try {
            if (gsonStr == null) {
                return null;
            }
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            return (YYBaseResBean) gson.fromJson(gsonStr, clazz);
        } catch (Exception ex) {
            Log.e("TEST", " exception , " + ex.getMessage());
            return null;
        }
    }

    public static String toJson(Object obj) {
        try {
            if (obj == null) {
                return null;
            }
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            return gson.toJson(obj);
        } catch (Exception ex) {
            // Log.e("TEST", " exception , " + ex.getMessage());
            return null;
        }
    }


}
