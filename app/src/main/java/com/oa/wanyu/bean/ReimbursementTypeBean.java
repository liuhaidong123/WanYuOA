package com.oa.wanyu.bean;

import java.util.List;

/**
 * Created by liuhaidong on 2019/4/1.
 */

public class ReimbursementTypeBean {

    private String code;

    private List<String> ReimburseType;

    private String message;

    public void setCode(String code){
        this.code = code;
    }
    public String getCode(){
        return this.code;
    }
    public void setReimburseType(List<String> ReimburseType){
        this.ReimburseType = ReimburseType;
    }
    public List<String> getReimburseType(){
        return this.ReimburseType;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
}
