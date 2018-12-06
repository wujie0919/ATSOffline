package df.yyzc.com.yydf.base.javavo;

import java.io.Serializable;

/**
 * Created by zhangyu on 16-4-21.
 * <p/>
 * 车辆检测时的传递信息
 */
public class ChecktTransmitVo implements Serializable {

    private int type;
    private String orderId;
    private String name;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
