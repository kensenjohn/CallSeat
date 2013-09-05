package com.gs.user;

import com.gs.bean.AdminBean;
import com.gs.bean.user.GroupBean;
import com.gs.common.Constants;
import com.gs.common.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 9/4/13
 * Time: 6:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class User {
    private static final Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);
    public User(){}
    public User(AdminBean adminBean) {
        this.adminBean = adminBean;
    }
    private AdminBean adminBean = new AdminBean();

    public AdminBean getAdminBean() {
        return this.adminBean;
    }
    public boolean can(Permission permission) {
        boolean hasPermission = false;
        if(this.adminBean!=null && !Utility.isNullOrEmpty(this.adminBean.getAdminId())) {
            hasPermission = true;
            appLogging.info("going to check if user is blocked");
            UserBlockedPermission blockedPermission = new UserBlockedPermission(this);
            if(blockedPermission!=null && blockedPermission.isBlocked( permission )) {
                hasPermission = false;
            }
            appLogging.info("does admin have permission so far : " + hasPermission );
            if(hasPermission) {
                UserGroup userGroup = new UserGroup(this);
                hasPermission = userGroup.can( permission );
            }
            appLogging.info("check if user has group permission: " + hasPermission );
        }
        return hasPermission;
    }
}
