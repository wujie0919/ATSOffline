package df.yyzc.com.yydf.base.javavo;

import java.util.ArrayList;

/**
 * Created by zhangyu on 16-4-22.
 */
public class QueryItemsCheckedListRes extends YYBaseResBean {

    private ArrayList<CheckItemDetailVo> data;

    public ArrayList<CheckItemDetailVo> getData() {
        return data;
    }

    public void setData(ArrayList<CheckItemDetailVo> data) {
        this.data = data;
    }
}
