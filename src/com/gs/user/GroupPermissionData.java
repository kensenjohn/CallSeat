package com.gs.user;

import com.gs.bean.user.GroupBean;
import com.gs.bean.user.GroupPermissionBean;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.Utility;
import com.gs.common.db.DBDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 9/4/13
 * Time: 10:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroupPermissionData {
    private static final Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);

    private Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);
    private String ADMIN_DB = applicationConfig.get(Constants.ADMIN_DB);

    public GroupPermissionBean getGroupPermission(GroupBean groupBean, Permission permission) {
        GroupPermissionBean groupPermissionBean = new GroupPermissionBean();
        if( groupBean!=null && !Utility.isNullOrEmpty(groupBean.getGroupId())) {
            String sQuery = "SELECT * FROM GTGROUPPERMISSIONS GP, GTPERMISSIONS P WHERE GP.FK_GROUPID = ? " +
                    " AND GP.FK_PERMSSIONID = P.PERMISSIONID AND  P.PERMISSION_NAME = ? AND GP.IS_ACTIVE = ? ";
            ArrayList<Object> aParams = DBDAO.createConstraint(groupBean.getGroupId(), permission.name(), "1");
            ArrayList<HashMap<String, String>> arrGroupPermission = DBDAO.getDBData(ADMIN_DB, sQuery, aParams, true,
                    "GroupPermissionData.java", "getGroupPermission()");
            if(arrGroupPermission!=null && !arrGroupPermission.isEmpty()) {
                for(HashMap<String, String> hmGroupPermission : arrGroupPermission ){
                    groupPermissionBean = new GroupPermissionBean(hmGroupPermission);
                }
            }
        }
        return groupPermissionBean;
    }
}
