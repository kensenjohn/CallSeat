package com.gs.data;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.GuestBean;
import com.gs.common.db.DBDAO;

public class GuestData
{
	private static final Logger appLogging = LoggerFactory.getLogger("AppLogging");

	public Integer insertGuest(GuestBean guestBean)
	{
		/*
		 * GUESTID VARCHAR(45) NOT NULL, FK_USERINFOID VARCHAR(45) NOT NULL,
		 * FK_ADMINID VARCHAR(45) NOT NULL, CREATEDATE TIMESTAMP NOT NULL
		 * DEFAULT '1980-01-01 00:00:00', TOTAL_SEATS INT(11) NOT NULL DEFAULT
		 * 1, IS_TMP INT(1) NOT NULL DEFAULT 1, DEL_ROW INT(1) NOT NULL DEFAULT
		 * 0
		 */
		appLogging.debug("Invoking insert Admin Data");
		String sQuery = "INSERT INTO GTGUESTS (GUESTID, FK_USERINFOID, FK_ADMINID, CREATEDATE, TOTAL_SEATS, IS_TMP,DEL_ROW)"
				+ " VALUES ( ?,?,?,  ?,?,?,  ?)";
		ArrayList<Object> aParams = DBDAO.createConstraint(guestBean.getGuestId(),
				guestBean.getUserInfoId(), guestBean.getAdminId(), guestBean.getCreateDate(),
				guestBean.getTotalSeat(), guestBean.getIsTemporary(), guestBean.getDeleteRow());

		int numOfRowsInserted = DBDAO.putRowsQuery(sQuery, aParams, "admin", "AdminData.java",
				"insertAdmin() ");

		return numOfRowsInserted;
	}
}
