package com.oa.wanyu.bean;

/**
 *出差实体类
 */

public class TravelBean {
    private String place;
    private String beginDate;
    private String endDate;
    private int duration;

    public TravelBean(String place, String beginDate, String endDate, int duration) {
        this.place = place;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.duration = duration;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
