package com.oa.wanyu.bean;

import java.util.List;

/**
 * Created by liuhaidong on 2019/4/4.
 */

public class GoodsBuyInformationBean {
    private String departmentName;

    private String modifyTimeString;

    private String reason;

    private double amount;

    private String createTimeString;

    private String updateTimeString;

    private String approvalUserName;

    private String deleteTimeString;

    private String userName;

    private long userId;

    private String approvalTrueName;

    private String trueName;

    private long approvalUserId;

    private long id;

    private String position;

    private List<GoodsBuyInformationItems> items;

    private int status;

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getModifyTimeString() {
        return modifyTimeString;
    }

    public void setModifyTimeString(String modifyTimeString) {
        this.modifyTimeString = modifyTimeString;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCreateTimeString() {
        return createTimeString;
    }

    public void setCreateTimeString(String createTimeString) {
        this.createTimeString = createTimeString;
    }

    public String getUpdateTimeString() {
        return updateTimeString;
    }

    public void setUpdateTimeString(String updateTimeString) {
        this.updateTimeString = updateTimeString;
    }

    public String getApprovalUserName() {
        return approvalUserName;
    }

    public void setApprovalUserName(String approvalUserName) {
        this.approvalUserName = approvalUserName;
    }

    public String getDeleteTimeString() {
        return deleteTimeString;
    }

    public void setDeleteTimeString(String deleteTimeString) {
        this.deleteTimeString = deleteTimeString;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getApprovalTrueName() {
        return approvalTrueName;
    }

    public void setApprovalTrueName(String approvalTrueName) {
        this.approvalTrueName = approvalTrueName;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public long getApprovalUserId() {
        return approvalUserId;
    }

    public void setApprovalUserId(long approvalUserId) {
        this.approvalUserId = approvalUserId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public List<GoodsBuyInformationItems> getItems() {
        return items;
    }

    public void setItems(List<GoodsBuyInformationItems> items) {
        this.items = items;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
