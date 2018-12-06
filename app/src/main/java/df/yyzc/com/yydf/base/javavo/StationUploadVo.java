package df.yyzc.com.yydf.base.javavo;

import java.util.List;

/**
 * Created by Administrator on 2016/7/1.
 */
public class StationUploadVo {

    private String station_name;
    private List<StationUploadImgsVo> stationsImg;

    public String getStation_name() {
        return station_name;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }

    public List<StationUploadImgsVo> getStationsImg() {
        return stationsImg;
    }

    public void setStationsImg(List<StationUploadImgsVo> stationsImg) {
        this.stationsImg = stationsImg;
    }
}
