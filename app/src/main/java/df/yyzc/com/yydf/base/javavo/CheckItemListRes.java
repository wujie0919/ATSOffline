package df.yyzc.com.yydf.base.javavo;

import java.util.ArrayList;

/**
 * Created by zhangyu on 16-4-21.
 */
public class CheckItemListRes extends YYBaseResBean {


    /**
     * andorid 本地使用
     * <p/>
     * 1车内物品，2车辆外部，3车内环境
     */
    private int type;

    private ArrayList<CheckItemName> data;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ArrayList<CheckItemName> getData() {
        return data;
    }

    public void setData(ArrayList<CheckItemName> data) {
        this.data = data;
    }
}
