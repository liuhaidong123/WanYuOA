package com.oa.wanyu.bean.out;

/**
 * Created by liuhaidong on 2019/4/4.
 */

public class OutInformationRoot {
    private String code;

    private OutInformationBean applyOut;

    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public OutInformationBean getApplyOut() {
        return applyOut;
    }

    public void setApplyOut(OutInformationBean applyOut) {
        this.applyOut = applyOut;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
