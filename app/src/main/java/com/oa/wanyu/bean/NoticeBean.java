package com.oa.wanyu.bean;

/**
 * Created by liuhaidong on 2019/4/10.
 */

public class NoticeBean {
    private String createTimeString;

    private int msgType;

    private String updateTimeString;

    private String title;

    private String userName;

    private int msgStatus;

    private long toUserId;

    private long userId;

    private String content;

    private long referId;

    private String toUserName;

    private long id;

    private String referName;

    private int status;

    private boolean isRead;

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getCreateTimeString() {
        return createTimeString;
    }

    public void setCreateTimeString(String createTimeString) {
        this.createTimeString = createTimeString;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getUpdateTimeString() {
        return updateTimeString;
    }

    public void setUpdateTimeString(String updateTimeString) {
        this.updateTimeString = updateTimeString;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(int msgStatus) {
        this.msgStatus = msgStatus;
    }

    public long getToUserId() {
        return toUserId;
    }

    public void setToUserId(long toUserId) {
        this.toUserId = toUserId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getReferId() {
        return referId;
    }

    public void setReferId(long referId) {
        this.referId = referId;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReferName() {
        return referName;
    }

    public void setReferName(String referName) {
        this.referName = referName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
