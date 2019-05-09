package com.oa.wanyu.bean;

/**
 * Created by liuhaidong on 2019/4/4.
 */

public class GoodsBuyInformationRoot {
    private String code;

    private String message;

    private GoodsBuyInformationBean applyBuy;

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

    public GoodsBuyInformationBean getApplyBuy() {
        return applyBuy;
    }

    public void setApplyBuy(GoodsBuyInformationBean applyBuy) {
        this.applyBuy = applyBuy;
    }
}
