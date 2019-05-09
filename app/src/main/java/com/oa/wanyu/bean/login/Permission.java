package com.oa.wanyu.bean.login;

import java.io.Serializable;

/**
 * Created by liuhaidong on 2019/3/19.
 */

public class Permission implements Serializable{
    private int status;

    private String createTime;

    private String updateTime;

    private String localId;

    private String info;

    private long id;

    private int moduleId;

    private String permissionName;

    private int permissionType;

    private String target;

    private String operaton;

    private String description;

    private int controlLevel;

    private String moduleName;

    private String createTimeString;

    private String updateTimeString;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public int getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(int permissionType) {
        this.permissionType = permissionType;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getOperaton() {
        return operaton;
    }

    public void setOperaton(String operaton) {
        this.operaton = operaton;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getControlLevel() {
        return controlLevel;
    }

    public void setControlLevel(int controlLevel) {
        this.controlLevel = controlLevel;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
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
}
