package com.oa.wanyu.bean.leaveType;

import java.util.List;

/**
 * Created by liuhaidong on 2019/3/29.
 */

public class LeaveRoot {
    private String code;

    private List<String> LeaveType;

    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getLeaveType() {
        return LeaveType;
    }

    public void setLeaveType(List<String> leaveType) {
        LeaveType = leaveType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
