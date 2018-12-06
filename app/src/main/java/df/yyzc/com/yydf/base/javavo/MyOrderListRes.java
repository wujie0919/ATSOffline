package df.yyzc.com.yydf.base.javavo;

import java.util.ArrayList;

/**
 * Created by zhangyu on 16-4-19.
 * <p/>
 * 我的订单列表
 */
public class MyOrderListRes extends YYBaseResBean {


    private ArrayList<Order> data;

    public ArrayList<Order> getData() {
        return data;
    }

    public void setData(ArrayList<Order> data) {
        this.data = data;
    }
}
