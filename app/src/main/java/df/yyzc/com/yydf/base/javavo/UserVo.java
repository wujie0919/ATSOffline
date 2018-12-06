package df.yyzc.com.yydf.base.javavo;

import java.io.Serializable;

/**
 * Created by zhangyu on 16-4-19.
 */
public class UserVo implements Serializable {


    private int ground_user_id;
    private String name;
    private String mobile;
    private String account;
    private String skey;
    /**
     * 接车权限(0无1有)
     */
    private int pick_power;
    /**
     * 整备权限(0无1有)
     */
    private int station_power;
    /**
     * 管理权限(0无1有)
     */
    private int manager_power;


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getGround_user_id() {
        return ground_user_id;
    }

    public void setGround_user_id(int ground_user_id) {
        this.ground_user_id = ground_user_id;
    }

    public int getManager_power() {
        return manager_power;
    }

    public void setManager_power(int manager_power) {
        this.manager_power = manager_power;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPick_power() {
        return pick_power;
    }

    public void setPick_power(int pick_power) {
        this.pick_power = pick_power;
    }

    public String getSkey() {
        return skey;
    }

    public void setSkey(String skey) {
        this.skey = skey;
    }

    public int getStation_power() {
        return station_power;
    }

    public void setStation_power(int station_power) {
        this.station_power = station_power;
    }
}
