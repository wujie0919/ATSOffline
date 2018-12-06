package df.yyzc.com.yydf.base.javavo;

import java.io.Serializable;

/**
 * Created by zhangyu on 16-4-19.
 */
public class CompleteData extends YYBaseResBean implements Serializable {
    private int data;

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

}
