package df.yyzc.com.yydf.base.javavo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhangyu on 16-4-22.
 */
public class CheckItemDetailVo implements Serializable {


    /**
     * 检查项id
     */
    private int item_id;


    private String check_id;


    private int ground_order_id;

    /**
     * 大分类名称
     */
    private String groups;


    /**
     * 检查内容(车钥匙\行驶证等)
     */
    private String category;

    /**
     * 检查状态(0无问题,1有问题)
     */
    private int check_state;


    /**
     * 检查备注
     */
    private String remark;

    /**
     * 检查图片
     */
    private ArrayList<ImageVo> imgList;


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCheck_id() {
        return check_id;
    }

    public void setCheck_id(String check_id) {
        this.check_id = check_id;
    }

    public int getCheck_state() {
        return check_state;
    }

    public void setCheck_state(int check_state) {
        this.check_state = check_state;
    }

    public int getGround_order_id() {
        return ground_order_id;
    }

    public void setGround_order_id(int ground_order_id) {
        this.ground_order_id = ground_order_id;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public ArrayList<ImageVo> getImgList() {
        return imgList;
    }

    public void setImgList(ArrayList<ImageVo> imgList) {
        this.imgList = imgList;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public void addImageVo(ImageVo imageVo) {

        if (imgList == null) {
            imgList = new ArrayList<ImageVo>();
        }

        if (imgList.size() < 6) {
            imgList.add(imageVo);
        }
    }


}
