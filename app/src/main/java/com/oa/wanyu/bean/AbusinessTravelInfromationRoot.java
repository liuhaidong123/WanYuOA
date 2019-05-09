package com.oa.wanyu.bean;

/**
 * Created by liuhaidong on 2019/4/4.
 */

public class AbusinessTravelInfromationRoot {
    private String code;

    private AbusinessTravelInfromationBean applyTrip;

    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public AbusinessTravelInfromationBean getApplyTrip() {
        return applyTrip;
    }

    public void setApplyTrip(AbusinessTravelInfromationBean applyTrip) {
        this.applyTrip = applyTrip;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
