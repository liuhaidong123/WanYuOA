package com.oa.wanyu.bean;

import java.util.List;

/**
 * Created by liuhaidong on 2019/5/31.
 */

public class AgentCompanyRoot {
    private int total;

    private String code;

    private String message;

    private List<AgentCompanyRows> rows;

    private List<String> colmodel;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
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

    public List<AgentCompanyRows> getRows() {
        return rows;
    }

    public void setRows(List<AgentCompanyRows> rows) {
        this.rows = rows;
    }

    public List<String> getColmodel() {
        return colmodel;
    }

    public void setColmodel(List<String> colmodel) {
        this.colmodel = colmodel;
    }
}
