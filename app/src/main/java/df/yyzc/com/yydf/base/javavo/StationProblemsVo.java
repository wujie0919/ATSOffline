package df.yyzc.com.yydf.base.javavo;

import java.util.List;

/**
 * Created by Administrator on 2016/10/19.
 */
public class StationProblemsVo extends YYBaseResBean {
    private List<StationProblemListVo> data;

    public List<StationProblemListVo> getData() {
        return data;
    }

    public void setData(List<StationProblemListVo> data) {
        this.data = data;
    }
}
