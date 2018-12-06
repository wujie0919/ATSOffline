package df.yyzc.com.yydf.base.javavo;

import java.util.List;

/**
 * Created by Administrator on 2016/10/25.
 */
public class PriceDetailVo extends YYBaseResBean {
    private PriceDetail data;

    public PriceDetail getData() {
        return data;
    }

    public void setData(PriceDetail data) {
        this.data = data;
    }

    public static class PriceDetail {
        private int actualPrice;//实际价格
        private long createTime;
        private int daysState;//单位
        private int groundId;
        private int id;
        private String stoppingTime;
        private int price;//价格
        private List<StopImgVo> stoppingPriceImgInfos;

        public int getActualPrice() {
            return actualPrice;
        }

        public void setActualPrice(int actualPrice) {
            this.actualPrice = actualPrice;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getDaysState() {
            return daysState;
        }

        public void setDaysState(int daysState) {
            this.daysState = daysState;
        }

        public int getGroundId() {
            return groundId;
        }

        public void setGroundId(int groundId) {
            this.groundId = groundId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getStoppingTime() {
            return stoppingTime;
        }

        public void setStoppingTime(String stoppingTime) {
            this.stoppingTime = stoppingTime;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public List<StopImgVo> getStoppingPriceImgInfos() {
            return stoppingPriceImgInfos;
        }

        public void setStoppingPriceImgInfos(List<StopImgVo> stoppingPriceImgInfos) {
            this.stoppingPriceImgInfos = stoppingPriceImgInfos;
        }
    }

    public class StopImgVo {
        private long createTime;
        private int id;
        private String imagesPath;
        private int stopId;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImagesPath() {
            return imagesPath;
        }

        public void setImagesPath(String imagesPath) {
            this.imagesPath = imagesPath;
        }

        public int getStopId() {
            return stopId;
        }

        public void setStopId(int stopId) {
            this.stopId = stopId;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }
    }
}
