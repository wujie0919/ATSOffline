package df.yyzc.com.yydf.base.javavo;

import java.util.List;

/**
 * 未完成订单Bean
 * Created by Administrator on 2016/4/20.
 */
public class MainOrderListBean extends YYBaseResBean {

    /**
     * return_code : 0
     * total_num : 1247
     * data : [{"ground_order_id":"680","car_id":"11","create_time":"2016-03-23 01:41:26","order_type":"1","order_type_nm":"接车订单","user_order_id":"1691","receive_gu_id":"18","car_license":"京Q5MW81","energy":"74","gps_info":"$GPGGA,042805.999,,,,,0,0,,,M,,M,,*4A","order_state":"4","station_id":"2","order_distance":"3439.725637435913","longitude":null,"latitude":null,"location":""},{"ground_order_id":"683","car_id":"11","create_time":"2016-03-23 02:29:28","order_type":"2","order_type_nm":"整备订单","user_order_id":"1691","receive_gu_id":"18","car_license":"京Q5MW81","energy":"74","gps_info":"$GPGGA,042805.999,,,,,0,0,,,M,,M,,*4A","order_state":"8","station_id":"2","order_distance":"3439.725637435913","longitude":null,"latitude":null,"location":""}]
     */
    private String total_num;

    private List<Order> data;

    public String getTotal_num() {
        return total_num;
    }

    public void setTotal_num(String total_num) {
        this.total_num = total_num;
    }

    public List<Order> getData() {
        return data;
    }

    public void setData(List<Order> data) {
        this.data = data;
    }

}
