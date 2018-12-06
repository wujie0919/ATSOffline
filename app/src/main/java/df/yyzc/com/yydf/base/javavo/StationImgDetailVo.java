package df.yyzc.com.yydf.base.javavo;

import java.util.List;

/**
 * Created by Administrator on 2016/10/24.
 */
public class StationImgDetailVo extends YYBaseResBean {
    private onebean data;

    public onebean getData() {
        return data;
    }

    public void setData(onebean data) {
        this.data = data;
    }

    public static class onebean {
        private List<Listbeab> result;

        public List<Listbeab> getResult() {
            return result;
        }

        public void setResult(List<Listbeab> result) {
            this.result = result;
        }
    }

    public static class Listbeab

    {
        private String station_name;
        private int station_id;
        private List<StationUploadImgsVo> stationsImg;

        public String getStation_name() {
            return station_name;
        }

        public void setStation_name(String station_name) {
            this.station_name = station_name;
        }

        public int getStation_id() {
            return station_id;
        }

        public void setStation_id(int station_id) {
            this.station_id = station_id;
        }

        public List<StationUploadImgsVo> getStationsImg() {
            return stationsImg;
        }

        public void setStationsImg(List<StationUploadImgsVo> stationsImg) {
            this.stationsImg = stationsImg;
        }
    }
}
