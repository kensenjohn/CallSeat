package com.gs.user;

import com.gs.bean.user.GroupBean;
import com.gs.bean.user.GroupPermissionBean;
import com.gs.common.Constants;
import com.gs.common.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 9/4/13
 * Time: 10:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroupPermission {
    private static final Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);
    private GroupBean groupBean = new GroupBean();
    public GroupPermission(GroupBean groupBean) {
        this.groupBean = groupBean;
    }

    public boolean can( Permission permission ) {
        boolean hasPermission = false;
        if(groupBean!=null && !Utility.isNullOrEmpty(groupBean.getGroupId())){
            GroupPermissionData groupPermissionData = new GroupPermissionData();
            GroupPermissionBean groupPermissionBean = groupPermissionData.getGroupPermission(groupBean,permission);
            appLogging.info("getting the group permission = : " + groupPermissionBean );
            if(groupPermissionBean!=null && !Utility.isNullOrEmpty(groupPermissionBean.getGroupPermissionId())) {
                hasPermission = true;
            }
            appLogging.info("has group permission = : " + hasPermission );
        }
        return hasPermission;
    }
}
