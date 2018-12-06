package df.yyzc.com.yydf.base.javavo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhangyu on 16-7-4.
 */
public class StationUploadImageVo implements Serializable {


    //    private int stationId;
//    private int groundUserId;
    private ArrayList<String> stationsImg;

//    public int getGroundUserId() {
//        return groundUserId;
//    }
//
//    public void setGroundUserId(int groundUserId) {
//        this.groundUserId = groundUserId;
//    }
//
//    public int getStationId() {
//        return stationId;
//    }
//
//    public void setStationId(int stationId) {
//        this.stationId = stationId;
//    }

    public ArrayList<String> getStationsImg() {
        return stationsImg;
    }

    public void setStationsImg(ArrayList<String> stationsImg) {
        this.stationsImg = stationsImg;
    }


    @Override
    public String toString() {

        if (stationsImg != null && stationsImg.size() > 0) {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < stationsImg.size(); i++) {
                if (i != 0) {
                    stringBuffer.append(",");
                }
                stringBuffer.append(stationsImg.get(i));
            }
            return stringBuffer.toString();

        } else {
            return "";
        }

    }
}
