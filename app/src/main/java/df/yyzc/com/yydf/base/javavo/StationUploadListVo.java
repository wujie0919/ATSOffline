package df.yyzc.com.yydf.base.javavo;

import java.util.List;

/**
 * Created by Administrator on 2016/7/1.
 */
public class StationUploadListVo {
    private boolean firstPage;
    private boolean lastPage;
    private int pageNo;
    private List<StationUploadVo> result;

    public boolean isFirstPage() {
        return firstPage;
    }

    public void setFirstPage(boolean firstPage) {
        this.firstPage = firstPage;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public List<StationUploadVo> getResult() {
        return result;
    }

    public void setResult(List<StationUploadVo> result) {
        this.result = result;
    }
}
