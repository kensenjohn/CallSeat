package com.gs.bean.user;

import com.gs.common.Constants;
import com.gs.common.ParseUtil;
import com.gs.user.Permission;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 9/4/13
 * Time: 9:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class BlockedUserPermissionBean {
    public BlockedUserPermissionBean(){}
    public BlockedUserPermissionBean(HashMap<String,String> hmResult) {
        this.blockedUserPermissionId = ParseUtil.checkNull(hmResult.get("BLOCKEDUSERPERMISSIONID"));
        this.adminId = ParseUtil.checkNull(hmResult.get("FK_ADMINID"));
        this.permissionId = ParseUtil.checkNull(hmResult.get("FK_PERMSSIONID"));
        this.createDate = ParseUtil.sToL(hmResult.get("CREATEDATE"));
        this.humanCreateDate = ParseUtil.checkNull(hmResult.get("HUMANCREATEDATE"));
        this.isActive = ParseUtil.sTob(hmResult.get("IS_ACTIVE"));
        this.modifiedDate = ParseUtil.sToL(hmResult.get("MODIFIEDDATE"));
        this.humanModifiedDate = ParseUtil.checkNull(hmResult.get("HUMANMODIFIEDDATE"));

    }
    private String blockedUserPermissionId = Constants.EMPTY;
    private String adminId = Constants.EMPTY;
    private String permissionId = Constants.EMPTY;
    private Long createDate = 0L;
    private String humanCreateDate = Constants.EMPTY;
    private boolean isActive = false;
    private Long modifiedDate = 0L;
    private String humanModifiedDate = Constants.EMPTY;
    private Permission permission;

    public String getBlockedUserPermissionId() {
        return blockedUserPermissionId;
    }

    public void setBlockedUserPermissionId(String blockedUserPermissionId) {
        this.blockedUserPermissionId = blockedUserPermissionId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public String getHumanCreateDate() {
        return humanCreateDate;
    }

    public void setHumanCreateDate(String humanCreateDate) {
        this.humanCreateDate = humanCreateDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Long getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getHumanModifiedDate() {
        return humanModifiedDate;
    }

    public void setHumanModifiedDate(String humanModifiedDate) {
        this.humanModifiedDate = humanModifiedDate;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return "BlockedUserPermissionBean{" +
                "blockedUserPermissionId='" + blockedUserPermissionId + '\'' +
                ", adminId='" + adminId + '\'' +
                ", permissionId='" + permissionId + '\'' +
                ", createDate=" + createDate +
                ", humanCreateDate='" + humanCreateDate + '\'' +
                ", isActive=" + isActive +
                ", modifiedDate=" + modifiedDate +
                ", humanModifiedDate='" + humanModifiedDate + '\'' +
                ", permission=" + permission +
                '}';
    }
}
