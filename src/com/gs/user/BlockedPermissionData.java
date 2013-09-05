package com.gs.user;

import com.gs.bean.AdminBean;
import com.gs.bean.user.BlockedUserPermissionBean;
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
 * Time: 9:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class BlockedPermissionData {
    private static final Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);

    private Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);
    private String ADMIN_DB = applicationConfig.get(Constants.ADMIN_DB);

    public BlockedUserPermissionBean getBlockedPermission(AdminBean adminBean , Permission permission) {
        BlockedUserPermissionBean blockedUserPermissionBean = new BlockedUserPermissionBean();
        if(adminBean!=null && !Utility.isNullOrEmpty(adminBean.getAdminId()))  {
            String sQuery = "SELECT BP.BLOCKEDUSERPERMISSIONID,BP.FK_ADMINID, BP.FK_PERMSSIONID, BP.IS_ACTIVE " +
                    " FROM GTBLOCKEDUSERPERMISSION BP, GTPERMISSIONS P WHERE BP.FK_ADMINID = ? AND" +
                    " BP.FK_PERMSSIONID = P.PERMISSIONID AND P.PERMISSION_NAME = ? AND BP.IS_ACTIVE = ?";

            ArrayList<Object> aParams = DBDAO.createConstraint( adminBean.getAdminId() , permission.name(), "1" );

            ArrayList<HashMap<String, String>> arrBlockedPermission = DBDAO.getDBData(ADMIN_DB, sQuery, aParams, true,
                    "BlockedPermissionData.java", "getBlockedPermission()");
            if(arrBlockedPermission!=null && !arrBlockedPermission.isEmpty() ) {
                for(HashMap<String, String> hmBlockedPermission : arrBlockedPermission ) {
                    blockedUserPermissionBean = new BlockedUserPermissionBean(hmBlockedPermission) ;
                }
            }
        }
        return blockedUserPermissionBean;
    }
}
