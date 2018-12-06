package df.yyzc.com.yydf.base.javavo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/19.
 */
public class StaitonproblemimgsVo implements Parcelable, Serializable {
    private long createTime;
    private int id;
    private String imagesPath;
    private int stationProblemId;

    protected StaitonproblemimgsVo(Parcel in) {
        createTime = in.readLong();
        id = in.readInt();
        imagesPath = in.readString();
        stationProblemId = in.readInt();
    }

    public static final Creator<StaitonproblemimgsVo> CREATOR = new Creator<StaitonproblemimgsVo>() {
        @Override
        public StaitonproblemimgsVo createFromParcel(Parcel in) {
            return new StaitonproblemimgsVo(in);
        }

        @Override
        public StaitonproblemimgsVo[] newArray(int size) {
            return new StaitonproblemimgsVo[size];
        }
    };

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

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

    public int getStationProblemId() {
        return stationProblemId;
    }

    public void setStationProblemId(int stationProblemId) {
        this.stationProblemId = stationProblemId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(createTime);
        dest.writeInt(id);
        dest.writeString(imagesPath);
        dest.writeInt(stationProblemId);
    }
}
