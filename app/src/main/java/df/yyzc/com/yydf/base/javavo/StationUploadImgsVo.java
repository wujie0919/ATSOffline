package df.yyzc.com.yydf.base.javavo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/1.
 */
public class StationUploadImgsVo implements Parcelable {
    private int createAt;
    private long createTime;
    private int id;
    private int stationId;
    private String stationImg;

    protected StationUploadImgsVo(Parcel in) {
        createAt = in.readInt();
        createTime = in.readLong();
        id = in.readInt();
        stationId = in.readInt();
        stationImg = in.readString();
    }

    public static final Creator<StationUploadImgsVo> CREATOR = new Creator<StationUploadImgsVo>() {
        @Override
        public StationUploadImgsVo createFromParcel(Parcel in) {
            return new StationUploadImgsVo(in);
        }

        @Override
        public StationUploadImgsVo[] newArray(int size) {
            return new StationUploadImgsVo[size];
        }
    };

    public int getCreateAt() {
        return createAt;
    }

    public void setCreateAt(int createAt) {
        this.createAt = createAt;
    }

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

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public String getStationImg() {
        return stationImg;
    }

    public void setStationImg(String stationImg) {
        this.stationImg = stationImg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(createAt);
        dest.writeLong(createTime);
        dest.writeInt(id);
        dest.writeInt(stationId);
        dest.writeString(stationImg);
    }
}
