package com.oa.wanyu.bean;

/**
 * Created by liuhaidong on 2019/4/3.
 */

public class CurrencyInformationRoot {
    private CurrencyInformationBean applyUniversal;

    private String code;

    private String message;

    public CurrencyInformationBean getApplyUniversal() {
        return applyUniversal;
    }

    public void setApplyUniversal(CurrencyInformationBean applyUniversal) {
        this.applyUniversal = applyUniversal;
    }

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
}
