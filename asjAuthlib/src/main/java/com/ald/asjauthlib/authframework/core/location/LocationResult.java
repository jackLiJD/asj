package com.ald.asjauthlib.authframework.core.location;


import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

/**
 * 此类封装了定位的结果。
 *
 * @author Jacky
 */
public class LocationResult extends BaseModel {

    private String resultTime;// 结果的时间
    private double longitude;// 经度
    private double latitude;// 纬度
    private double radius;// 精确度
    private String address;// 地址
    private String province;// 省
    private String cityName;// 市名
    private String cityCode;// 城市编码
    private String district;// 区

    public String toString() {
        return "lng=" + longitude + ",lat=" + latitude + ",cityCode=" + cityCode + ",cityName=" + cityName + ",district=" + district + ",address="
                + address;
    }

    public String getResultTime() {
        return resultTime;
    }

    public void setResultTime(String resultTime) {
        this.resultTime = resultTime;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

}
