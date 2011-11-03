package com.gs.data;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.GuestBean;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.ParseUtil;
import com.gs.common.db.DBDAO;

public class GuestData
{
	private static final Logger appLogging = LoggerFactory.getLogger("AppLogging");

	Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);

	private String ADMIN_DB = applicationConfig.get(Constants.ADMIN_DB);

	public Integer insertGuest(GuestBean guestBean)
	{
		/*
		 * GUESTID VARCHAR(45) NOT NULL, FK_USERINFOID VARCHAR(45) NOT NULL,
		 * FK_ADMINID VARCHAR(45) NOT NULL, CREATEDATE TIMESTAMP NOT NULL
		 * DEFAULT '1980-01-01 00:00:00', TOTAL_SEATS INT(11) NOT NULL DEFAULT
		 * 1, IS_TMP INT(1) NOT NULL DEFAULT 1, DEL_ROW INT(1) NOT NULL DEFAULT
		 * 0
		 */
		String sQuery = "INSERT INTO GTGUESTS (GUESTID, FK_USERINFOID, FK_ADMINID, CREATEDATE, TOTAL_SEATS, IS_TMP,DEL_ROW)"
				+ " VALUES ( ?,?,?,  ?,?,?,  ?)";
		ArrayList<Object> aParams = DBDAO.createConstraint(guestBean.getGuestId(),
				guestBean.getUserInfoId(), guestBean.getAdminId(), guestBean.getCreateDate(),
				guestBean.getTotalSeat(), guestBean.getIsTemporary(), guestBean.getDeleteRow());

		int numOfRowsInserted = DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB, "GuestData.java",
				"insertGuest()");

		return numOfRowsInserted;
	}

	public ArrayList<GuestBean> getGuestByAdmin(String sAdmin)
	{
		ArrayList<GuestBean> arrGuestBean = new ArrayList<GuestBean>();

		String sQuery = "SELECT GUESTID, FK_USERINFOID, FK_ADMINID, CREATEDATE, "
				+ " TOTAL_SEATS, RSVP_SEATS, CREATEDATE, IS_TMP, DEL_ROW FROM GTGUESTS "
				+ " WHERE FK_ADMINID = ?  ORDER BY CREATEDATE DESC ";
		ArrayList<Object> aParams = DBDAO.createConstraint(sAdmin);

		ArrayList<HashMap<String, String>> arrHmGuests = DBDAO.getDBData(ADMIN_DB, sQuery, aParams,
				false, "GuestData.java", "getGuestByAdmin()");

		if (arrHmGuests != null && !arrHmGuests.isEmpty())
		{
			for (HashMap<String, String> hmGuests : arrHmGuests)
			{
				GuestBean guestBean = new GuestBean();

				guestBean.setGuestId(hmGuests.get("GUESTID"));
				guestBean.setUserInfoId(hmGuests.get("FK_USERINFOID"));
				guestBean.setAdminId(hmGuests.get("FK_ADMINID"));
				guestBean.setCreateDate(ParseUtil.sToL(hmGuests.get("CREATEDATE")));
				guestBean.setTotalSeat(hmGuests.get("TOTAL_SEATS"));
				guestBean.setRsvpSeat(hmGuests.get("RSVP_SEATS"));
				guestBean.setIsTemporary(hmGuests.get("IS_TMP"));
				guestBean.setDeleteRow(hmGuests.get("DEL_ROW"));

				arrGuestBean.add(guestBean);
			}
		}

		return arrGuestBean;
	}
}
