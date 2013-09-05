package com.gs.user;

import com.gs.bean.AdminBean;
import com.gs.bean.user.BlockedUserPermissionBean;
import com.gs.common.Constants;
import com.gs.common.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 9/4/13
 * Time: 6:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserBlockedPermission {
    private  final Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);
    private User user = new User();
    public UserBlockedPermission(User user) {
        this.user = user;
    }

    public boolean isBlocked( Permission permission ) {
        boolean isBlocked = false;
        BlockedUserPermissionBean blockedUserPermissionBean = getBlockedPermission(permission);
        appLogging.info("checking if Blocked Permission for admin is present : " + blockedUserPermissionBean );
        if(blockedUserPermissionBean == null || (blockedUserPermissionBean!=null && !Utility.isNullOrEmpty(blockedUserPermissionBean.getBlockedUserPermissionId())) ) {
            isBlocked = true;
        }
        appLogging.info("is Admin blocked : " + isBlocked );
        return isBlocked;
    }

    private AdminBean getAdminBean() {
        AdminBean adminBean = new AdminBean();
        if(this.user!=null) {
            adminBean = user.getAdminBean();
        }
        appLogging.info("trying to get Admin bean from user  : " + adminBean );
        return adminBean;
    }

    private BlockedUserPermissionBean getBlockedPermission( Permission permission ) {
        BlockedPermissionData blockedPermissionData = new BlockedPermissionData();
        return blockedPermissionData.getBlockedPermission( getAdminBean(),permission );
    }
}
