package df.yyzc.com.yydf.base.javavo;

import java.io.Serializable;

/**
 * Created by zhangyu on 16-4-21.
 */
public class CheckItemName implements Serializable {

    private int item_id;
    private String name;


    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
