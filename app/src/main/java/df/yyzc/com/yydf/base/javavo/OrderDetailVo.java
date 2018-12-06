package df.yyzc.com.yydf.base.javavo;

/**
 * Created by zhangyu on 16-4-20.
 * <p/>
 * 订单详情
 */
public class OrderDetailVo {

    /**
     * 订单状态:order_type=1时：1待接单2待取车3待归还4已归还,
     * <p/>
     * order_type=2时：5待整备6整备中7已上线8已出租
     */
    private int order_state;
    private int car_id;
    private String create_time;
    private int order_type;
    private String order_type_nm;
    private int user_order_id;
    private String car_license;
    private String energy;
    private String longitude;
    private String latitude;
    private String location;
    private double order_distance;
    private int ground_order_id;
    private String station_name;
    private String receive_time;
    private String pick_time;
    private String back_time;
    private String service_time;
    private String online_time;
    private String pick_mileage;

    private String order_user_name;
    private String order_user_mobile;


    public String getBack_time() {
        return back_time;
    }

    public void setBack_time(String back_time) {
        this.back_time = back_time;
    }

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getOnline_time() {
        return online_time;
    }

    public void setOnline_time(String online_time) {
        this.online_time = online_time;
    }

    public double getOrder_distance() {
        return order_distance;
    }

    public void setOrder_distance(double order_distance) {
        this.order_distance = order_distance;
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

    public String getPick_mileage() {
        return pick_mileage;
    }

    public void setPick_mileage(String pick_mileage) {
        this.pick_mileage = pick_mileage;
    }

    public String getPick_time() {
        return pick_time;
    }

    public void setPick_time(String pick_time) {
        this.pick_time = pick_time;
    }

    public String getReceive_time() {
        return receive_time;
    }

    public void setReceive_time(String receive_time) {
        this.receive_time = receive_time;
    }

    public String getService_time() {
        return service_time;
    }

    public void setService_time(String service_time) {
        this.service_time = service_time;
    }

    public String getStation_name() {
        return station_name;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }

    public int getUser_order_id() {
        return user_order_id;
    }

    public void setUser_order_id(int user_order_id) {
        this.user_order_id = user_order_id;
    }

    public String getOrder_user_mobile() {
        return order_user_mobile;
    }

    public void setOrder_user_mobile(String order_user_mobile) {
        this.order_user_mobile = order_user_mobile;
    }

    public String getOrder_user_name() {
        return order_user_name;
    }

    public void setOrder_user_name(String order_user_name) {
        this.order_user_name = order_user_name;
    }
}
