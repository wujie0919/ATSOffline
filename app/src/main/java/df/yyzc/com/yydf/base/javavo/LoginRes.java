package df.yyzc.com.yydf.base.javavo;

/**
 * Created by zhangyu on 16-4-19.
 */
public class LoginRes extends YYBaseResBean {

    private UserVo user;

    private StatisticsVo statistics;


    public UserVo getUser() {
        return user;
    }

    public void setUser(UserVo user) {
        this.user = user;
    }


    public StatisticsVo getStatistics() {
        return statistics;
    }

    public void setStatistics(StatisticsVo statistics) {
        this.statistics = statistics;
    }
}
