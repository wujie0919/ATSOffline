package df.yyzc.com.yydf.base.javavo;

import java.io.Serializable;

/**
 * Created by zhangyu on 16-4-20.
 * <p/>
 * 站点信息
 */
public class StationVo implements Serializable {

    private int station_id;
    private String station_name;
    private String privince;
    private String city;
    private String district;
    private String address;
    private double longitude;
    private double latitude;
    private double station_distance;

    private int parkingNum;
    private int usedcarNum;
    private int usedCarNum;
    private int useCount;
    private int problemFlag;//场站问题0是没问题1 有问题
    private int flag;

    public int getUsedCarNum() {
        return usedCarNum;
    }

    public void setUsedCarNum(int usedCarNum) {
        this.usedCarNum = usedCarNum;
    }

    public int getUseCount() {
        return useCount;
    }

    public void setUseCount(int useCount) {
        this.useCount = useCount;
    }

    public int getProblemFlag() {
        return problemFlag;
    }

    public void setProblemFlag(int problemFlag) {
        this.problemFlag = problemFlag;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPrivince() {
        return privince;
    }

    public void setPrivince(String privince) {
        this.privince = privince;
    }

    public double getStation_distance() {
        return station_distance;
    }

    public void setStation_distance(double station_distance) {
        this.station_distance = station_distance;
    }

    public int getStation_id() {
        return station_id;
    }

    public void setStation_id(int station_id) {
        this.station_id = station_id;
    }

    public String getStation_name() {
        return station_name;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getParkingNum() {
        return parkingNum;
    }

    public void setParkingNum(int parkingNum) {
        this.parkingNum = parkingNum;
    }

    public int getUsedcarNum() {
        return usedcarNum;
    }

    public void setUsedcarNum(int usedcarNum) {
        this.usedcarNum = usedcarNum;
    }
}
