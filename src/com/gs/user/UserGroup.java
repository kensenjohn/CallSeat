package com.gs.user;

import com.gs.bean.AdminBean;
import com.gs.bean.user.GroupBean;
import com.gs.common.Constants;
import com.gs.common.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 9/5/13
 * Time: 12:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class UserGroup {
    private static final Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);
    private User user = new User();
    public UserGroup() {}
    public UserGroup(User user) {
        this.user = user;
    }

    public GroupBean getUserGroup() {
        GroupBean groupBean = new GroupBean();
        if(this.user!=null ) {
            AdminBean adminBean = this.user.getAdminBean();
            appLogging.info("is admin bean found = : " + adminBean );
            if(adminBean!=null && !Utility.isNullOrEmpty(adminBean.getAdminId())) {
                UserGroupData userGroupData = new UserGroupData();
                groupBean = userGroupData.getUserGroup( adminBean );
                appLogging.info("getting group bean = : " + groupBean );
            }
        }
        return groupBean;
    }

    public boolean can( Permission permission ) {
        boolean hasPermission = false;
        GroupBean groupBean = getUserGroup();
        if(groupBean!=null && !Utility.isNullOrEmpty(groupBean.getGroupId())) {
            appLogging.info("before getting the group permission, Group = : " + groupBean );
            GroupPermission groupPermission = new GroupPermission( groupBean );
            hasPermission = groupPermission.can( permission );

            appLogging.info("does user have group permission = : " + hasPermission );
        }
        return hasPermission;
    }
}
