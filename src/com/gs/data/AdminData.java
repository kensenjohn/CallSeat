package com.gs.data;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.AdminBean;
import com.gs.common.db.DBDAO;

public class AdminData
{
	private static final Logger appLogging = LoggerFactory.getLogger("AppLogging");

	public Integer insertAdmin(AdminBean adminBean)
	{
		appLogging.debug("Invoking insert Admin Data");
		String sQuery = "INSERT INTO GTADMIN (ADMINID, FK_USERINFOID,CREATEDATE,IS_TMP,DEL_ROW)"
				+ " VALUES ( ? , ? , ? , ? , ? )";
		ArrayList<Object> aParams = DBDAO.createConstraint(adminBean.getAdminId(),
				adminBean.getUserInfoId(), adminBean.getCreateDate(), adminBean.getIsTemporary(),
				adminBean.getDeleteRow());

		int numOfRowsInserted = DBDAO.putRowsQuery(sQuery, aParams, "admin", "AdminData.java",
				"insertAdmin() ");

		return numOfRowsInserted;
	}

	public void updateAdmin()
	{

	}

}
