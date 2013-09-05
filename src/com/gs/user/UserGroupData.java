package com.gs.user;

import com.gs.bean.AdminBean;
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
 * Date: 9/5/13
 * Time: 12:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class UserGroupData {
    private static final Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);

    private Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);
    private String ADMIN_DB = applicationConfig.get(Constants.ADMIN_DB);

    public GroupBean getUserGroup(AdminBean adminBean){
        GroupBean groupBean = new GroupBean();
        if(adminBean!=null && !Utility.isNullOrEmpty(adminBean.getAdminId())) {
            String sQuery = "SELECT * FROM GTUSERGROUPS UG, GTGROUPS G WHERE UG.FK_ADMINID = ? AND UG.FK_GROUPID = G.GROUPID AND" +
                    " UG.IS_ACTIVE = ?";
            ArrayList<Object> aParams = DBDAO.createConstraint(adminBean.getAdminId(), "1");
            ArrayList<HashMap<String, String>> arrUserGroup = DBDAO.getDBData(ADMIN_DB, sQuery, aParams, true,
                    "UserGroupData.java", "getUserGroup()");
            appLogging.info("Got result of User groups : " + arrUserGroup + " Qyeru : " + sQuery + " aParams : " +  aParams );
            if(arrUserGroup!=null && !arrUserGroup.isEmpty()) {
                for(HashMap<String, String> hmUserGroup : arrUserGroup ){
                    groupBean = new GroupBean(hmUserGroup);
                }
            }
            appLogging.info("Groupbean after execution : " + groupBean  );
        }
        return groupBean;
    }
}
