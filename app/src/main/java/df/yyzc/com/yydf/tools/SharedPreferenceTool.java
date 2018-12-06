package df.yyzc.com.yydf.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferenceTool {

    /**
     * 用户信息
     */
    public static String KEY_USERINFO = "user_info";

    // public static String KEY_LOGIN_SKEY = "skey";
    // public static String KEY_USERID = "userid";
    // public static String KEY_MOBILE = "mobile";
    // public static String KEY_NAME = "name";
    // public static String KEY_THUMB = "thumb";// 用户头像
    // public static String KEY_ROLE = "role"; // normal是正常员工
    // public static String KEY_ENTERPRISEID = "entId"; // 企业 id
    // public static String KEY_ENTERPRISENAME = "enterprisename"; // 企业 名称
    public static String KEY_XGTOKEN = "xgtoken";// 信鸽token

    // public static String KEY_AUTHSTATE = "authstate";// 用户认证状态

    public static String KEY_ADS = "adphotos";// 启动广告页面
    public static String KEY_ADS_LEFT = "adphotos_left";// 左侧菜单广告
    public static String KEY_ADS_MAIN = "adphotos_main";// 主页面广告
    public static String KEY_SYSCONFIG = "service_sysconfig";//服务器端字段配置
    public static String KEY_LASTSHOWAD_DATE = "key_lastshowad_date";//上一次展示广告日期
    public static String KEY_CANCELORDER_DATE = "key_cancelorder_date";//取消订单日期记录

    public static String KEY_VERSION = "";


    /**
     * 已下单过的车型
     */
    public static String KEY_USEDCARMODE = "userCarMode";

    /**
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getPrefBoolean(Context context, final String key,
                                         final boolean defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getBoolean(key, defaultValue);
    }

    /**
     * @param context
     * @param key
     * @param value
     */
    public static void setPrefBoolean(Context context, final String key,
                                      final boolean value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings.edit().putBoolean(key, value).commit();
    }

    public static String getPrefString(Context context, String key,
                                       final String defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getString(key, defaultValue);
    }

    public static void setPrefString(Context context, final String key,
                                     final String value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings.edit().putString(key, value).commit();
    }

    public static void setPrefInt(Context context, final String key,
                                  final int value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings.edit().putInt(key, value).commit();
    }

    public static int getPrefInt(Context context, final String key,
                                 final int defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getInt(key, defaultValue);
    }

}
