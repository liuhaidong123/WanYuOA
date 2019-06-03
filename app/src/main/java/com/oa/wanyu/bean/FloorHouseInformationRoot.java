package com.oa.wanyu.bean;

/**
 * Created by liuhaidong on 2019/5/21.
 */

public class FloorHouseInformationRoot {
    private String code;

    private String message;

    private FloorHouseInformation house;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public FloorHouseInformation getHouse() {
        return house;
    }

    public void setHouse(FloorHouseInformation house) {
        this.house = house;
    }
}
