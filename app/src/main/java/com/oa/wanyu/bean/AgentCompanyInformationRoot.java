package com.oa.wanyu.bean;

import java.util.List;

/**
 * Created by liuhaidong on 2019/6/3.
 */

public class AgentCompanyInformationRoot {

    private String code;

    private List<AgentCompanyInformationRows> userList;

    private AgentCompanyMessage company;

    private String message;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<AgentCompanyInformationRows> getUserList() {
        return userList;
    }

    public void setUserList(List<AgentCompanyInformationRows> userList) {
        this.userList = userList;
    }

    public AgentCompanyMessage getCompany() {
        return company;
    }

    public void setCompany(AgentCompanyMessage company) {
        this.company = company;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
