package df.yyzc.com.yydf.base.javavo;

import java.util.ArrayList;

/**
 * Created by zhangyu on 16-4-20.
 */
public class StationListRes extends YYBaseResBean {

    private ArrayList<StationVo> data;

    public ArrayList<StationVo> getData() {
        return data;
    }

    public void setData(ArrayList<StationVo> data) {
        this.data = data;
    }
}
