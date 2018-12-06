package df.yyzc.com.yydf.constans;

import df.yyzc.com.yydf.base.YYDFApp;
import df.yyzc.com.yydf.base.javavo.LoginRes;
import df.yyzc.com.yydf.base.javavo.StatisticsVo;
import df.yyzc.com.yydf.base.javavo.UserVo;
import df.yyzc.com.yydf.tools.GsonTransformUtil;
import df.yyzc.com.yydf.tools.SharedPreferenceTool;

/**
 * Created by zhangyu on 16-4-19.
 */
public class YYConstans {
    /**
     * 数据返回状态标示
     */
    public final static int DATABACKTAG_NOCONNECT = 16902;// 网络异常，请检查网络连接或重试
    public final static int DATABACKTAG_LOCFAIL = 16903;// 定位失败，请重试
    public final static int DATABACKTAG_ERROR = 16904;// 服务器返回数据错误
    public final static int DATABACKTAG_SUCCESS = 16905;// 服务器返回数据成功
    private static UserVo user;
    private static StatisticsVo statistics;

    public static UserVo getUser() {
        if (user == null) {
            LoginRes userRes = ((LoginRes) (GsonTransformUtil.fromJson(
                    SharedPreferenceTool.getPrefString(YYDFApp
                                    .getInstance().getApplicationContext(),
                            SharedPreferenceTool.KEY_USERINFO, null),
                    LoginRes.class)));
            if (userRes != null) {
                user = userRes.getUser();
                statistics = userRes.getStatistics();
            }
        }
        if (user == null) {
            user = new UserVo();
        }
        return user;
    }

    public static void setUser(LoginRes loginRes) {
        SharedPreferenceTool.setPrefString(YYDFApp.getInstance()
                        .getApplicationContext(), SharedPreferenceTool.KEY_USERINFO,
                GsonTransformUtil.toJson(loginRes));


        if (loginRes == null || loginRes.getReturn_code() != 0) {
            YYConstans.user = null;
            YYConstans.setStatistics(null);
            YYConstans.user = getUser();
        } else {
            YYConstans.user = loginRes.getUser();
            YYConstans.setStatistics(loginRes.getStatistics());
        }

    }

    public static StatisticsVo getStatistics() {
        return statistics;
    }

    public static void setStatistics(StatisticsVo statistics) {
        YYConstans.statistics = statistics;
    }
}
