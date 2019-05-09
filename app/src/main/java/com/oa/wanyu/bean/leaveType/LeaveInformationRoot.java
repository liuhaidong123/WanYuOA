package com.oa.wanyu.bean.leaveType;

/**
 * Created by liuhaidong on 2019/4/4.
 */

public class LeaveInformationRoot {
    private String code;

    private LeaveInformationBean applyLeave;

    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LeaveInformationBean getApplyLeave() {
        return applyLeave;
    }

    public void setApplyLeave(LeaveInformationBean applyLeave) {
        this.applyLeave = applyLeave;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
