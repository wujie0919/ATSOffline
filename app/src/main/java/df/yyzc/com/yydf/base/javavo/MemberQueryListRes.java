package df.yyzc.com.yydf.base.javavo;

import java.util.ArrayList;

/**
 * Created by zhangyu on 16-5-5.
 */
public class MemberQueryListRes extends YYBaseResBean {

    private int total_num;
    private ArrayList<MemberVo> data;


    public ArrayList<MemberVo> getData() {
        return data;
    }

    public void setData(ArrayList<MemberVo> data) {
        this.data = data;
    }

    public int getTotal_num() {
        return total_num;
    }

    public void setTotal_num(int total_num) {
        this.total_num = total_num;
    }
}
