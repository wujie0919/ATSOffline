package df.yyzc.com.yydf.base.javavo;

import java.io.Serializable;

/**
 * Created by zhangyu on 16-5-5.
 * 地服人员
 */
public class MemberVo implements Serializable {

    private int ground_user_id;
    private String name;
    private String mobile;
    private String account;
    /**
     * 接车权限
     */
    private int pick_power;
    /**
     * 站点权限
     */
    private int station_power;
    /**
     * 管理权限
     */
    private int manager_power;
    /**
     * 创建时间
     */
    private String create_time;
    /**
     * 是否有地服订单(0:无地服订单,1:有接车订单,2:有整备订单)
     */
    private int in_order_state;


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getGround_user_id() {
        return ground_user_id;
    }

    public void setGround_user_id(int ground_user_id) {
        this.ground_user_id = ground_user_id;
    }

    public int getIn_order_state() {
        return in_order_state;
    }

    public void setIn_order_state(int in_order_state) {
        this.in_order_state = in_order_state;
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

    public int getStation_power() {
        return station_power;
    }

    public void setStation_power(int station_power) {
        this.station_power = station_power;
    }


}
