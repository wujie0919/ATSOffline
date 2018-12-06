package df.yyzc.com.yydf.base.javavo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/10/19.
 */
public class StationProblemListVo implements Serializable {
    private long createTime;
    private int id;
    private int state;
    private int stationId;
    private String stationName;

    private String stationProblem;
    private List<StaitonproblemimgsVo> stationProblemImgs;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationProblem() {
        return stationProblem;
    }

    public void setStationProblem(String stationProblem) {
        this.stationProblem = stationProblem;
    }

    public List<StaitonproblemimgsVo> getStationProblemImgs() {
        return stationProblemImgs;
    }

    public void setStationProblemImgs(List<StaitonproblemimgsVo> stationProblemImgs) {
        this.stationProblemImgs = stationProblemImgs;
    }
}
