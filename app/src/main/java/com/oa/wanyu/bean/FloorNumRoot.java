package com.oa.wanyu.bean;

import java.util.List;

/**
 * Created by liuhaidong on 2019/5/14.
 */

public class FloorNumRoot {
    private int total;

    private String code;

    private String message;

    private List<FloorNumRows> rows;

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

    public List<FloorNumRows> getRows() {
        return rows;
    }

    public void setRows(List<FloorNumRows> rows) {
        this.rows = rows;
    }

    public List<String> getColmodel() {
        return colmodel;
    }

    public void setColmodel(List<String> colmodel) {
        this.colmodel = colmodel;
    }
}
