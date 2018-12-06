package df.yyzc.com.yydf.base.javavo;

import java.io.Serializable;

/**
 * Created by zhangyu on 16-4-19.
 */
public class YYBaseResBean implements Serializable {

    private int return_code;
    private String return_msg;

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
}
