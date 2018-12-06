package df.yyzc.com.yydf.base.javavo;

/**
 * Created by zhangyu on 16-5-18.
 */
public class UploadImageRes extends YYBaseResBean {

    private String stateMsg;
    private int stateCode;
    private UploadImageBackVo resourceInfo;

    public UploadImageBackVo getResourceInfo() {
        return resourceInfo;
    }

    public void setResourceInfo(UploadImageBackVo resourceInfo) {
        this.resourceInfo = resourceInfo;
    }

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateMsg() {
        return stateMsg;
    }

    public void setStateMsg(String stateMsg) {
        this.stateMsg = stateMsg;
    }
}

