package df.yyzc.com.yydf.base.javavo;

import java.io.Serializable;

/**
 * Created by zhangyu on 16-4-19.
 */
public class Order implements Serializable {
    private int car_id;
    private int receive_gu_id;
    private String gps_info;
    private int station_id;
    /**
     * 订单类型名称(0.接车订单,1.整备订单)
     */
    private int go_type;

    /**
     * 订单状态:order_type=1时：1待接单2待取车3待归还4已归还,
     * <p/>
     * order_type=2时：5待整备6整备中7已上线8已出租
     */
    private int order_state;
    private int ground_order_id;
    private String create_time;
    private int order_type;
    /**
     * 订单类型名称(1.接车订单,2.整备订单)
     */
    private String order_type_nm;
    /**
     * 用户订单id
     */
    private int user_order_id;
    /**
     * 车牌号
     */
    private String car_license;
    /**
     * 剩余电量
     */
    private String energy;


    private double longitude;
    private double latitude;
    private String location;

    /**
     * 失联状态0，正常1
     */
    private int findNotFlag;

    /**
     * 用户订单还车位置和自己现在坐标的距离(单位:m)
     */
    private String noOnlineReason;
    private int noOnlineReasonState;//返回原因状态 0：返回1：取消
    private double order_distance;
    private int total_num; //返回数量
    private int return_code; //返回状态
    private String return_msg; //返回信息

    public String getNoOnlineReason() {
        return noOnlineReason;
    }

    public void setNoOnlineReason(String noOnlineReason) {
        this.noOnlineReason = noOnlineReason;
    }

    public int getNoOnlineReasonState() {
        return noOnlineReasonState;
    }

    public void setNoOnlineReasonState(int noOnlineReasonState) {
        this.noOnlineReasonState = noOnlineReasonState;
    }

    public int getGo_type() {
        return go_type;
    }

    public void setGo_type(int go_type) {
        this.go_type = go_type;
    }

    public int getTotal_num() {
        return total_num;
    }

    public void setTotal_num(int total_num) {
        this.total_num = total_num;
    }

    public int getReturn_code() {
        return return_code;
    }

    public void setReturn_code(int return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    /**
     * 订单状态:order_type=1时：1待接单2待取车3待归还4已归还,
     * <p/>
     * order_type=2时：5待整备6整备中7已上线8已出租
     */
    public int getOrder_state() {
        return order_state;
    }

    public void setOrder_state(int order_state) {
        this.order_state = order_state;
    }

    public String getCar_license() {
        return car_license;
    }

    public void setCar_license(String car_license) {
        this.car_license = car_license;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getEnergy() {
        return energy;
    }

    public void setEnergy(String energy) {
        this.energy = energy;
    }

    public int getGround_order_id() {
        return ground_order_id;
    }

    public void setGround_order_id(int ground_order_id) {
        this.ground_order_id = ground_order_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getOrder_distance() {
        return order_distance;
    }

    public void setOrder_distance(double order_distance) {
        this.order_distance = order_distance;
    }

    public int getOrder_type() {
        return order_type;
    }

    public void setOrder_type(int order_type) {
        this.order_type = order_type;
    }

    public String getOrder_type_nm() {
        return order_type_nm;
    }

    public void setOrder_type_nm(String order_type_nm) {
        this.order_type_nm = order_type_nm;
    }

    public int getUser_order_id() {
        return user_order_id;
    }

    public void setUser_order_id(int user_order_id) {
        this.user_order_id = user_order_id;
    }

    public int getReceive_gu_id() {
        return receive_gu_id;
    }

    public void setReceive_gu_id(int receive_gu_id) {
        this.receive_gu_id = receive_gu_id;
    }

    public String getGps_info() {
        return gps_info;
    }

    public void setGps_info(String gps_info) {
        this.gps_info = gps_info;
    }

    public int getStation_id() {
        return station_id;
    }

    public void setStation_id(int station_id) {
        this.station_id = station_id;
    }

    public int getCar_id() {

        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public int getFindNotFlag() {
        return findNotFlag;
    }

    public void setFindNotFlag(int findNotFlag) {
        this.findNotFlag = findNotFlag;
    }
}
