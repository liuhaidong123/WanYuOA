package com.oa.wanyu.bean;

/**
 * Created by liuhaidong on 2019/4/4.
 */

public class GoodsUseInformationRoot {
    private String code;

    private String message;

    private GoodsUseInformationBean applyRecipients;

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

    public GoodsUseInformationBean getApplyRecipients() {
        return applyRecipients;
    }

    public void setApplyRecipients(GoodsUseInformationBean applyRecipients) {
        this.applyRecipients = applyRecipients;
    }
}
