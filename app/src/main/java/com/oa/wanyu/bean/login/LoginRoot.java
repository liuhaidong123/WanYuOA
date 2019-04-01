package com.oa.wanyu.bean.login;

/**
 * Created by liuhaidong on 2019/3/19.
 */

public class LoginRoot {
    private String code;

    private User User;

    private String message;

    public void setCode(String code){
        this.code = code;
    }
    public String getCode(){
        return this.code;
    }
    public void setUser(User User){
        this.User = User;
    }
    public User getUser(){
        return this.User;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
}
