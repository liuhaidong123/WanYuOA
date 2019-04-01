package com.oa.wanyu.bean;

/**
 * Created by liuhaidong on 2019/3/18.
 */

public class GoodsBuyBean {

    private String nam;
    private String typ;
    private String purpose;
    private String price;
    private String num;
    private String amount;

    public GoodsBuyBean(String nam, String typ, String purpose, String price, String num, String amount) {
        this.nam = nam;
        this.typ = typ;
        this.purpose = purpose;
        this.price = price;
        this.num = num;
        this.amount = amount;
    }

    public String getNam() {
        return nam;
    }

    public void setNam(String nam) {
        this.nam = nam;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
