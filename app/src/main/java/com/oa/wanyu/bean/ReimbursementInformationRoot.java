package com.oa.wanyu.bean;

/**
 * Created by liuhaidong on 2019/4/4.
 */

public class ReimbursementInformationRoot {
    private String code;

    private String message;

    private ReimbursementInformationBean applyReimburse;

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

    public ReimbursementInformationBean getApplyReimburse() {
        return applyReimburse;
    }

    public void setApplyReimburse(ReimbursementInformationBean applyReimburse) {
        this.applyReimburse = applyReimburse;
    }
}
