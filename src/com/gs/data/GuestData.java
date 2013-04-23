package com.gs.data;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.EventGuestBean;
import com.gs.bean.GuestBean;
import com.gs.bean.UserInfoBean;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.ParseUtil;
import com.gs.common.Utility;
import com.gs.common.db.DBDAO;
import com.gs.manager.event.TelNumberMetaData;

public class GuestData {
	private static final Logger appLogging = LoggerFactory
			.getLogger(Constants.APP_LOGS);

	Configuration applicationConfig = Configuration
			.getInstance(Constants.APPLICATION_PROP);

	private String ADMIN_DB = applicationConfig.get(Constants.ADMIN_DB);

	public Integer insertGuest(GuestBean guestBean) {
		/*
		 * GUESTID VARCHAR(45) NOT NULL, FK_USERINFOID VARCHAR(45) NOT NULL,
		 * FK_ADMINID VARCHAR(45) NOT NULL, CREATEDATE TIMESTAMP NOT NULL
		 * DEFAULT '1980-01-01 00:00:00', TOTAL_SEATS INT(11) NOT NULL DEFAULT
		 * 1, IS_TMP INT(1) NOT NULL DEFAULT 1, DEL_ROW INT(1) NOT NULL DEFAULT
		 * 0
		 */
		int numOfRowsInserted = 0;
		if (guestBean != null) {
			String sQuery = "INSERT INTO GTGUESTS (GUESTID, FK_USERINFOID, FK_ADMINID, CREATEDATE, "
					+ " TOTAL_SEATS, IS_TMP,DEL_ROW, RSVP_SEATS, HUMANCREATEDATE )"
					+ " VALUES ( ?,?,?,  ?,?,?,  ?,?,?)";
			ArrayList<Object> aParams = DBDAO.createConstraint(
					guestBean.getGuestId(), guestBean.getUserInfoId(),
					guestBean.getAdminId(), guestBean.getCreateDate(),
					guestBean.getTotalSeat(), guestBean.getIsTemporary(),
					guestBean.getDeleteRow(), guestBean.getRsvpSeat(),
					guestBean.getHumanCreateDate());

			numOfRowsInserted = DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB,
					"GuestData.java", "insertGuest()");
		} else {
			appLogging.error("GuestBean was null");
		}

		return numOfRowsInserted;
	}

	public GuestBean getGuest(String sGuestId) {
		GuestBean guestBean = new GuestBean();

		if (sGuestId != null && !"".equalsIgnoreCase(sGuestId)) {
			String sQuery = "SELECT GG.GUESTID, GG.FK_USERINFOID, GG.FK_ADMINID, GG.CREATEDATE, "
					+ " GG.TOTAL_SEATS, GG.RSVP_SEATS, GG.IS_TMP, GG.DEL_ROW, GG.HUMANCREATEDATE, "
					+ " GU.FIRST_NAME, GU.LAST_NAME, GU.ADDRESS_1, GU.ADDRESS_2, GU.CITY, "
					+ " GU.STATE, GU.COUNTRY, GU.IP_ADDRESS, GU.CELL_PHONE, GU.PHONE_NUM, GU.EMAIL, "
					+ " GU.IS_TMP AS USER_IS_TMP , GU.DEL_ROW AS USER_DEL_ROW, GU.CREATEDATE AS USER_CREATEDATE "
					+ " FROM GTGUESTS GG ,  GTUSERINFO GU "
					+ " WHERE GG.GUESTID = ?  AND GG.FK_USERINFOID = GU.USERINFOID ORDER BY GG.CREATEDATE DESC ";
			ArrayList<Object> aParams = DBDAO.createConstraint(sGuestId);

			ArrayList<HashMap<String, String>> arrHmGuests = DBDAO.getDBData(
					ADMIN_DB, sQuery, aParams, false, "GuestData.java",
					"getGuestByAdmin()");

			if (arrHmGuests != null && !arrHmGuests.isEmpty()) {
				for (HashMap<String, String> hmGuests : arrHmGuests) {
					guestBean = createGuestBeanFromResult(hmGuests);
					if (guestBean == null || guestBean.getGuestId() == null
							|| "".equalsIgnoreCase(guestBean.getGuestId())) {
						guestBean = new GuestBean();
					}
				}
			}

		}

		return guestBean;

	}

	public ArrayList<GuestBean> getGuestByAdmin(String sAdmin) {
		ArrayList<GuestBean> arrGuestBean = new ArrayList<GuestBean>();

		String sQuery = "SELECT GG.GUESTID, GG.FK_USERINFOID, GG.FK_ADMINID, GG.CREATEDATE, "
				+ " GG.TOTAL_SEATS, GG.RSVP_SEATS, GG.IS_TMP, GG.DEL_ROW, GG.HUMANCREATEDATE, "
				+ " GU.FIRST_NAME, GU.LAST_NAME, GU.ADDRESS_1, GU.ADDRESS_2, GU.CITY, "
				+ " GU.STATE, GU.COUNTRY, GU.IP_ADDRESS, GU.CELL_PHONE, GU.PHONE_NUM, GU.EMAIL, "
				+ " GU.IS_TMP AS USER_IS_TMP , GU.DEL_ROW AS USER_DEL_ROW, GU.CREATEDATE AS USER_CREATEDATE "
				+ " FROM GTGUESTS GG ,  GTUSERINFO GU "
				+ " WHERE FK_ADMINID = ?  AND GG.FK_USERINFOID = GU.USERINFOID ORDER BY GG.CREATEDATE DESC ";
		ArrayList<Object> aParams = DBDAO.createConstraint(sAdmin);

		ArrayList<HashMap<String, String>> arrHmGuests = DBDAO.getDBData(
				ADMIN_DB, sQuery, aParams, false, "GuestData.java",
				"getGuestByAdmin()");

		if (arrHmGuests != null && !arrHmGuests.isEmpty()) {
			for (HashMap<String, String> hmGuests : arrHmGuests) {
				GuestBean guestBean = createGuestBeanFromResult(hmGuests);
				if (guestBean != null && guestBean.getGuestId() != null
						&& !"".equalsIgnoreCase(guestBean.getGuestId())) {

					arrGuestBean.add(guestBean);
				}
			}
		}

		return arrGuestBean;
	}

	public Integer updateGuestInviteRsvpEvent(EventGuestBean eventGuestBean) {
		int numOfRowsInserted = 0;
		if (eventGuestBean != null && eventGuestBean.getEventId() != null
				&& eventGuestBean.getGuestId() != null
				&& !"".equalsIgnoreCase(eventGuestBean.getEventId())
				&& !"".equalsIgnoreCase(eventGuestBean.getGuestId())) {
			String sQuery = "UPDATE GTEVENTGUESTS SET RSVP_SEATS = ? , TOTAL_INVITED_SEATS = ? WHERE FK_EVENTID = ? AND FK_GUESTID = ?";
			ArrayList<Object> aParams = DBDAO.createConstraint(
					eventGuestBean.getRsvpSeats(),
					eventGuestBean.getTotalNumberOfSeats(),
					eventGuestBean.getEventId(), eventGuestBean.getGuestId());
			numOfRowsInserted = DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB,
					"GuestData.java", "updateGuestInviteRsvpEvent()");
		}
		return numOfRowsInserted;
	}

	public Integer updateGuestRsvpEvent(EventGuestBean eventGuestBean) {
		int numOfRowsInserted = 0;
		if (eventGuestBean != null && eventGuestBean.getEventId() != null
				&& eventGuestBean.getGuestId() != null
				&& !"".equalsIgnoreCase(eventGuestBean.getEventId())
				&& !"".equalsIgnoreCase(eventGuestBean.getGuestId())) {
			String sQuery = "UPDATE GTEVENTGUESTS SET RSVP_SEATS = ? WHERE FK_EVENTID = ? AND FK_GUESTID = ?";
			ArrayList<Object> aParams = DBDAO.createConstraint(
					eventGuestBean.getRsvpSeats(), eventGuestBean.getEventId(),
					eventGuestBean.getGuestId());
			numOfRowsInserted = DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB,
					"GuestData.java", "updateGuestRsvpEvent()");
		}
		return numOfRowsInserted;
	}

	public Integer assignGuestToEvent(EventGuestBean eventGuestBean) {
		/*
		 * EVENTGUESTID | varchar(45) | NO | PRI | NULL | | | FK_EVENTID |
		 * varchar(45) | NO | | NULL | | | FK_GUESTID | varchar(45) | NO | |
		 * NULL | | | IS_TMP | int(1) | NO | | 1 | | | DEL_ROW | int(1) | NO | |
		 * 0 | | | RSVP_SEATS | int(11) | YES | | NULL | | | TOTAL_INVITED_SEATS
		 */
		int numOfRowsInserted = 0;
		if (eventGuestBean != null && eventGuestBean.getEventId() != null
				&& eventGuestBean.getGuestId() != null
				&& !"".equalsIgnoreCase(eventGuestBean.getEventId())
				&& !"".equalsIgnoreCase(eventGuestBean.getGuestId())) {
			String sQuery = "INSERT INTO GTEVENTGUESTS (EVENTGUESTID,FK_EVENTID, FK_GUESTID,  "
					+ " IS_TMP , DEL_ROW , RSVP_SEATS , TOTAL_INVITED_SEATS ) "
					+ " VALUES( ?,?,?  ,?,?,?  ,?)";

			ArrayList<Object> aParams = DBDAO.createConstraint(
					eventGuestBean.getEventGuestId(),
					eventGuestBean.getEventId(), eventGuestBean.getGuestId(),
					eventGuestBean.getIsTemporary(),
					eventGuestBean.getDeleteRow(),
					eventGuestBean.getRsvpSeats(),
					eventGuestBean.getTotalNumberOfSeats());

			numOfRowsInserted = DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB,
					"GuestData.java", "assignGuestToEvent()");

		}
		return numOfRowsInserted;
	}

	public ArrayList<EventGuestBean> getEventGuests(String sEventId,
			ArrayList<String> arrGuestId) {
		ArrayList<EventGuestBean> arrEventGuestBean = new ArrayList<EventGuestBean>();
		if (arrGuestId != null && !arrGuestId.isEmpty() && sEventId != null
				&& !"".equalsIgnoreCase(sEventId)) {
			String sQuery = "SELECT  GU.USERINFOID, GU.CELL_PHONE, GU.PHONE_NUM, GU.FIRST_NAME, GU.LAST_NAME , "
					+ " GU.ADDRESS_1, GU.ADDRESS_2, GU.CITY, GU.STATE , "
					+ " GU.COUNTRY, GU.IP_ADDRESS, GU.EMAIL, "
					+ " GEG.EVENTGUESTID, GEG.FK_EVENTID, "
					+ " GEG.FK_GUESTID , GEG.IS_TMP , GEG.DEL_ROW, GEG.RSVP_SEATS, "
					+ " GEG.TOTAL_INVITED_SEATS FROM GTEVENTGUESTS GEG , GTUSERINFO GU, GTGUESTS GG WHERE "
					+ " GEG.FK_EVENTID = ? AND GG.GUESTID = GEG.FK_GUESTID AND "
					+ " GEG.FK_GUESTID IN (__GUEST_ID_LIST__) AND GG.FK_USERINFOID = GU.USERINFOID ";

			String sParamList = ParseUtil.checkNull(Utility
					.getMultipleParamsList(arrGuestId.size()));

			if (!"".equalsIgnoreCase(sParamList)) {
				sQuery = sQuery.replaceAll("__GUEST_ID_LIST__", sParamList);
				ArrayList<Object> aParams = DBDAO.createConstraint(sEventId);

				for (String sGuestId : arrGuestId) {
					aParams.add(sGuestId);
				}
				ArrayList<HashMap<String, String>> arrHmGuestEvents = DBDAO
						.getDBData(ADMIN_DB, sQuery, aParams, false,
								"GuestData.java", "getEventAllGuests()");
				if (arrHmGuestEvents != null && !arrHmGuestEvents.isEmpty()) {
					for (HashMap<String, String> hmGuestEvents : arrHmGuestEvents) {

						EventGuestBean eventGuestBean = createEventGuestDetailBean(hmGuestEvents);
						if (eventGuestBean != null
								&& !"".equalsIgnoreCase(ParseUtil
										.checkNull(eventGuestBean
												.getEventGuestId()))) {
							arrEventGuestBean.add(eventGuestBean);
						}
					}
				}
			}

		}
		return arrEventGuestBean;

	}

	public ArrayList<EventGuestBean> getEventAllGuests(String sEventId) {
		ArrayList<EventGuestBean> arrEventGuestBean = new ArrayList<EventGuestBean>();
		if (sEventId != null && !"".equalsIgnoreCase(sEventId)) {

			String sQuery = "SELECT  GU.USERINFOID, GU.CELL_PHONE, GU.PHONE_NUM, GU.FIRST_NAME, GU.LAST_NAME , "
					+ " GU.ADDRESS_1, GU.ADDRESS_2, GU.CITY, GU.STATE , "
					+ " GU.COUNTRY, GU.IP_ADDRESS, GU.EMAIL, "
					+ " GEG.EVENTGUESTID, GEG.FK_EVENTID, "
					+ " GEG.FK_GUESTID , GEG.IS_TMP , GEG.DEL_ROW, GEG.RSVP_SEATS, "
					+ " GEG.TOTAL_INVITED_SEATS FROM GTEVENTGUESTS GEG , GTUSERINFO GU, GTGUESTS GG WHERE "
					+ " GEG.FK_EVENTID = ? AND GG.GUESTID = GEG.FK_GUESTID AND GG.FK_USERINFOID = GU.USERINFOID ";

			ArrayList<Object> aParams = DBDAO.createConstraint(sEventId);

			ArrayList<HashMap<String, String>> arrHmGuestEvents = DBDAO
					.getDBData(ADMIN_DB, sQuery, aParams, false,
							"GuestData.java", "getEventAllGuests()");

			if (arrHmGuestEvents != null && !arrHmGuestEvents.isEmpty()) {
				for (HashMap<String, String> hmGuestEvents : arrHmGuestEvents) {

					EventGuestBean eventGuestBean = createEventGuestDetailBean(hmGuestEvents);
					if (eventGuestBean != null
							&& !"".equalsIgnoreCase(ParseUtil
									.checkNull(eventGuestBean.getEventGuestId()))) {
						arrEventGuestBean.add(eventGuestBean);
					}

				}
			}
		}

		return arrEventGuestBean;

	}

	private EventGuestBean createEventGuestDetailBean(
			HashMap<String, String> hmGuestEvents) {
		EventGuestBean eventGuestBean = new EventGuestBean();
		if (hmGuestEvents != null && !hmGuestEvents.isEmpty()) {
			eventGuestBean = new EventGuestBean(hmGuestEvents);

			GuestBean guestBean = new GuestBean();
			guestBean.setUserInfoId(hmGuestEvents.get("USERINFOID"));

			UserInfoBean userInfoBean = new UserInfoBean(hmGuestEvents);
			guestBean.setUserInfoBean(userInfoBean);

			eventGuestBean.setGuestBean(guestBean);
		}
		return eventGuestBean;
	}

	public ArrayList<EventGuestBean> getEventGuests(String sGuestId) {
		/*
		 * EVENTGUESTID | varchar(45) | NO | PRI | NULL | | | FK_EVENTID |
		 * varchar(45) | NO | | NULL | | | FK_GUESTID | varchar(45) | NO | |
		 * NULL | | | IS_TMP | int(1) | NO | | 1 | | | DEL_ROW | int(1) | NO | |
		 * 0 | | | RSVP_SEATS | int(11) | YES | | NULL | | | TOTAL_INVITED_SEATS
		 * | int(11) | YES | | NULL |
		 */
		String sQuery = "SELECT EVENTGUESTID, FK_EVENTID, FK_GUESTID , IS_TMP , DEL_ROW, RSVP_SEATS, "
				+ " TOTAL_INVITED_SEATS FROM GTEVENTGUESTS WHERE FK_GUESTID = ? ";

		ArrayList<Object> aParams = DBDAO.createConstraint(sGuestId);

		ArrayList<HashMap<String, String>> arrHmGuestEvents = DBDAO.getDBData(
				ADMIN_DB, sQuery, aParams, false, "GuestData.java",
				"getGuestEvents()");

		ArrayList<EventGuestBean> arrEventGuestBean = new ArrayList<EventGuestBean>();
		if (arrHmGuestEvents != null && !arrHmGuestEvents.isEmpty()) {
			for (HashMap<String, String> hmGuestEvents : arrHmGuestEvents) {
				EventGuestBean eventGuestBean = new EventGuestBean();

				eventGuestBean.setEventGuestId(ParseUtil
						.checkNull(hmGuestEvents.get("EVENTGUESTID")));
				eventGuestBean.setEventId(ParseUtil.checkNull(hmGuestEvents
						.get("FK_EVENTID")));
				eventGuestBean.setGuestId(ParseUtil.checkNull(hmGuestEvents
						.get("FK_GUESTID")));
				eventGuestBean.setIsTemporary(ParseUtil.checkNull(hmGuestEvents
						.get("IS_TMP")));
				eventGuestBean.setDeleteRow(ParseUtil.checkNull(hmGuestEvents
						.get("DEL_ROW")));
				eventGuestBean.setTotalNumberOfSeats(ParseUtil
						.checkNull(hmGuestEvents.get("TOTAL_INVITED_SEATS")));
				eventGuestBean.setRsvpSeats(ParseUtil.checkNull(hmGuestEvents
						.get("RSVP_SEATS")));

				arrEventGuestBean.add(eventGuestBean);
			}
		}
		return arrEventGuestBean;
	}

	public ArrayList<GuestBean> getGuestsByTelNumber(
			TelNumberMetaData telNumMetaData) {
		ArrayList<GuestBean> arrGuestBean = new ArrayList<GuestBean>();
		if (telNumMetaData != null
				&& telNumMetaData.getGuestTelNumber() != null
				&& !"".equalsIgnoreCase(telNumMetaData.getGuestTelNumber())) {
			String sQuery = "SELECT GG.GUESTID, GG.FK_USERINFOID, GG.FK_ADMINID, GG.CREATEDATE, "
					+ " GG.TOTAL_SEATS, GG.RSVP_SEATS, GG.IS_TMP, GG.DEL_ROW, GG.HUMANCREATEDATE, "
					+ " GU.FIRST_NAME, GU.LAST_NAME, GU.ADDRESS_1, GU.ADDRESS_2, GU.CITY, "
					+ " GU.STATE, GU.COUNTRY, GU.IP_ADDRESS, GU.CELL_PHONE, GU.PHONE_NUM, GU.EMAIL, "
					+ " GU.IS_TMP AS USER_IS_TMP , GU.DEL_ROW AS USER_DEL_ROW, GU.CREATEDATE AS USER_CREATEDATE "
					+ " FROM GTGUESTS GG ,  GTUSERINFO GU  where "
					+ " GG.FK_USERINFOID = GU.USERINFOID AND ( GU.CELL_PHONE =?  OR GU.PHONE_NUM = ? )";
			ArrayList<Object> aParams = DBDAO.createConstraint(
					telNumMetaData.getGuestTelNumber(),
					telNumMetaData.getGuestTelNumber());
            appLogging.info(sQuery + ": " + aParams );
			ArrayList<HashMap<String, String>> arrResult = DBDAO.getDBData(
					ADMIN_DB, sQuery, aParams, false, "GuestData.java",
					"getGuestsByTelNumber()");

			if (arrResult != null && !arrResult.isEmpty()) {
				for (HashMap<String, String> hmGuests : arrResult) {
					GuestBean guestBean = createGuestBeanFromResult(hmGuests);
					if (guestBean != null && guestBean.getGuestId() != null
							&& !"".equalsIgnoreCase(guestBean.getGuestId())) {

						arrGuestBean.add(guestBean);
					}
				}
			}

		}
		return arrGuestBean;
	}

	public ArrayList<EventGuestBean> getEventGuestList(String sEventId,
			ArrayList<String> arrGuestId) {

		ArrayList<EventGuestBean> arrEventGuestList = new ArrayList<EventGuestBean>();

		if (sEventId != null && !"".equalsIgnoreCase(sEventId)
				&& arrGuestId != null && !arrGuestId.isEmpty()) {
			ArrayList<Object> arrParams = new ArrayList<Object>();
			String sGuestParam = Utility.getMultipleParamsList(arrGuestId
					.size());
			for (String sGuestId : arrGuestId) {

				arrParams.add(sGuestId);
			}

			arrParams.add(sEventId);

			String sQuery = "SELECT  GU.CELL_PHONE, GU.PHONE_NUM, GU.FIRST_NAME, GU.LAST_NAME , "
					+ " GU.ADDRESS_1, GU.ADDRESS_2, GU.CITY, GU.STATE , "
					+ " GU.COUNTRY, GU.IP_ADDRESS, GU.EMAIL, "
					+ " GEG.EVENTGUESTID, GEG.FK_EVENTID, "
					+ " GEG.FK_GUESTID , GEG.IS_TMP , GEG.DEL_ROW, GEG.RSVP_SEATS, "
					+ " GEG.TOTAL_INVITED_SEATS FROM GTEVENTGUESTS GEG , GTUSERINFO GU, GTGUESTS GG WHERE GEG.FK_GUESTID in ("
					+ sGuestParam
					+ ") AND GEG.FK_EVENTID = ? AND GG.GUESTID = GEG.FK_GUESTID AND GG.FK_USERINFOID = GU.USERINFOID ";

			ArrayList<HashMap<String, String>> arrResult = DBDAO.getDBData(
					ADMIN_DB, sQuery, arrParams, false, "GuestData.java",
					"getGuest()");

			if (arrResult != null && !arrResult.isEmpty()) {
				for (HashMap<String, String> hmResult : arrResult) {
					EventGuestBean eventGuestBean = new EventGuestBean();
					eventGuestBean.setEventGuestId(ParseUtil.checkNull(hmResult
							.get("EVENTGUESTID")));
					eventGuestBean.setEventId(ParseUtil.checkNull(hmResult
							.get("FK_EVENTID")));
					eventGuestBean.setGuestId(ParseUtil.checkNull(hmResult
							.get("FK_GUESTID")));
					eventGuestBean.setIsTemporary(ParseUtil.checkNull(hmResult
							.get("IS_TMP")));
					eventGuestBean.setDeleteRow(ParseUtil.checkNull(hmResult
							.get("DEL_ROW")));
					eventGuestBean.setRsvpSeats(ParseUtil.checkNull(hmResult
							.get("RSVP_SEATS")));
					eventGuestBean.setTotalNumberOfSeats(ParseUtil
							.checkNull(hmResult.get("TOTAL_INVITED_SEATS")));
					arrEventGuestList.add(eventGuestBean);

				}
			}

		}

		return arrEventGuestList;
	}

	public EventGuestBean getGuest(String sEventId, ArrayList<String> arrGuestId) {
		EventGuestBean eventGuestBean = new EventGuestBean();
		if (sEventId != null && !"".equalsIgnoreCase(sEventId)
				&& arrGuestId != null && !arrGuestId.isEmpty()) {
			ArrayList<Object> arrParams = new ArrayList<Object>();
			String sGuestParam = "";
			boolean isFirstGuest = true;
			for (String sGuestId : arrGuestId) {
				if (!isFirstGuest) {
					sGuestParam = sGuestParam + ",";
				}
				sGuestParam = sGuestParam + "?";
				arrParams.add(sGuestId);
				isFirstGuest = false;
			}

			arrParams.add(sEventId);

			String sQuery = "SELECT  GU.CELL_PHONE, GU.PHONE_NUM, GU.FIRST_NAME, GU.LAST_NAME , "
					+ " GU.ADDRESS_1, GU.ADDRESS_2, GU.CITY, GU.STATE , "
					+ " GU.COUNTRY, GU.IP_ADDRESS, GU.EMAIL, "
					+ " GEG.EVENTGUESTID, GEG.FK_EVENTID, "
					+ " GEG.FK_GUESTID , GEG.IS_TMP , GEG.DEL_ROW, GEG.RSVP_SEATS, "
					+ " GEG.TOTAL_INVITED_SEATS FROM GTEVENTGUESTS GEG , GTUSERINFO GU, GTGUESTS GG WHERE GEG.FK_GUESTID in ("
					+ sGuestParam
					+ ") AND GEG.FK_EVENTID = ? AND GG.GUESTID = GEG.FK_GUESTID AND GG.FK_USERINFOID = GU.USERINFOID ";
            appLogging.debug(sQuery  + "  : " + arrParams );
			ArrayList<HashMap<String, String>> arrResult = DBDAO.getDBData(
					ADMIN_DB, sQuery, arrParams, false, "GuestData.java",
					"getGuest()");

			if (arrResult != null && !arrResult.isEmpty()) {
				for (HashMap<String, String> hmResult : arrResult) {
					eventGuestBean.setEventGuestId(ParseUtil.checkNull(hmResult
							.get("EVENTGUESTID")));
					eventGuestBean.setEventId(ParseUtil.checkNull(hmResult
							.get("FK_EVENTID")));
					eventGuestBean.setGuestId(ParseUtil.checkNull(hmResult
							.get("FK_GUESTID")));
					eventGuestBean.setIsTemporary(ParseUtil.checkNull(hmResult
							.get("IS_TMP")));
					eventGuestBean.setDeleteRow(ParseUtil.checkNull(hmResult
							.get("DEL_ROW")));
					eventGuestBean.setRsvpSeats(ParseUtil.checkNull(hmResult
							.get("RSVP_SEATS")));
					eventGuestBean.setTotalNumberOfSeats(ParseUtil
							.checkNull(hmResult.get("TOTAL_INVITED_SEATS")));

				}
			}

		}

		return eventGuestBean;
	}

	public Integer deleteGuest(String sGuestId)
	{
		Integer iNumOfRecs = 0;
		if(sGuestId!=null && !"".equalsIgnoreCase(sGuestId))
		{
			String sQuery = "DELETE FROM  GTGUESTS WHERE GUESTID = ?";
			ArrayList<Object> aParams = DBDAO.createConstraint(sGuestId);
			iNumOfRecs = DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB,
					"GuestData.java", "deleteGuest()");
		}
		return iNumOfRecs;
	}
	public Integer deleteGuestFromAllEvents(String sGuestId) 
	{
		Integer iNumOfRecs = 0;
		if(sGuestId!=null && !"".equalsIgnoreCase(sGuestId))
		{
			String sQuery = "DELETE FROM GTEVENTGUESTS WHERE FK_GUESTID = ?";
			ArrayList<Object> aParams = DBDAO.createConstraint(sGuestId);
			iNumOfRecs = DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB,
					"GuestData.java", "deleteGuestFromAllEvents()");
			
		}
		return iNumOfRecs;
	}

	public Integer deleteEventGuest(String sEventGuestId, String sEventId,
			String sGuestId) {
		Integer iNumOfRecs = 0;
		if (sEventGuestId != null && !"".equalsIgnoreCase(sEventGuestId)) {
			String sQuery = "DELETE FROM GTEVENTGUESTS WHERE EVENTGUESTID = ? AND FK_GUESTID = ? AND FK_EVENTID = ? ";
			ArrayList<Object> aParams = DBDAO.createConstraint(sEventGuestId,
					sGuestId, sEventId);
			iNumOfRecs = DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB,
					"GuestData.java", "deleteEventGuest()");
		}
		return iNumOfRecs;
	}

	public ArrayList<GuestBean> getUninvitedEventGuests(String sAdminId,
			String sEventId) {
		ArrayList<GuestBean> arrGuestBean = new ArrayList<GuestBean>();

		if (sAdminId != null && !"".equalsIgnoreCase(sAdminId)
				&& sEventId != null && !"".equalsIgnoreCase(sEventId)) {
			String sQuery = " select G.GUESTID,  G.FK_USERINFOID ,  G.FK_ADMINID , U.* "
					+ " FROM GTUSERINFO U, GTGUESTS G WHERE NOT EXISTS "
					+ " ( SELECT * FROM GTEVENTGUESTS EG WHERE EG.FK_GUESTID  = G.GUESTID "
					+ " AND FK_EVENTID= ? ) AND G.FK_ADMINID = ? AND U.USERINFOID = G.FK_USERINFOID";

			ArrayList<Object> arrParams = DBDAO.createConstraint(sEventId,
					sAdminId);

			ArrayList<HashMap<String, String>> arrResult = DBDAO.getDBData(
					ADMIN_DB, sQuery, arrParams, false, "GuestData.java",
					"getUninvitedEventGuests()");

			if (arrResult != null && !arrResult.isEmpty()) {
				for (HashMap<String, String> hmResult : arrResult) {
					GuestBean guestBean = createGuestBeanFromResult(hmResult);
					if (guestBean != null && guestBean.getGuestId() != null
							&& !"".equalsIgnoreCase(guestBean.getGuestId())) {

						arrGuestBean.add(guestBean);
					}
				}
			}

		}
		return arrGuestBean;
	}

	private GuestBean createGuestBeanFromResult(HashMap<String, String> hmGuests) {
		GuestBean guestBean = new GuestBean();
		if (hmGuests != null && !hmGuests.isEmpty()) {
			guestBean.setGuestId(ParseUtil.checkNull(hmGuests.get("GUESTID")));
			guestBean.setUserInfoId(ParseUtil.checkNull(hmGuests
					.get("FK_USERINFOID")));
			guestBean
					.setAdminId(ParseUtil.checkNull(hmGuests.get("FK_ADMINID")));
			guestBean.setCreateDate(ParseUtil.sToL(hmGuests.get("CREATEDATE")));
			guestBean.setTotalSeat(ParseUtil.checkNull(hmGuests
					.get("TOTAL_SEATS")));
			guestBean.setRsvpSeat(ParseUtil.checkNull(hmGuests
					.get("RSVP_SEATS")));
			guestBean
					.setIsTemporary(ParseUtil.checkNull(hmGuests.get("IS_TMP")));
			guestBean
					.setDeleteRow(ParseUtil.checkNull(hmGuests.get("DEL_ROW")));

			UserInfoBean userInfoBean = new UserInfoBean();

			userInfoBean.setFirstName(ParseUtil.checkNull(hmGuests
					.get("FIRST_NAME")));
			userInfoBean.setLastName(ParseUtil.checkNull(hmGuests
					.get("LAST_NAME")));
			userInfoBean.setAddress1(ParseUtil.checkNull(hmGuests
					.get("ADDRESS_1")));
			userInfoBean.setAddress2(ParseUtil.checkNull(hmGuests
					.get("ADDRESS_2")));
			userInfoBean.setCity(ParseUtil.checkNull(hmGuests.get("CITY")));
			userInfoBean.setState(ParseUtil.checkNull(hmGuests.get("STATE")));
			userInfoBean
					.setCountry(ParseUtil.checkNull(hmGuests.get("COUNTRY")));
			userInfoBean.setIpAddress(ParseUtil.checkNull(hmGuests
					.get("IP_ADDRESS")));
			userInfoBean.setCellPhone(ParseUtil.checkNull(hmGuests
					.get("CELL_PHONE")));
			userInfoBean.setPhoneNum(ParseUtil.checkNull(hmGuests
					.get("PHONE_NUM")));
			userInfoBean.setEmail(ParseUtil.checkNull(hmGuests.get("EMAIL")));
			userInfoBean.setIsTemporary(ParseUtil.checkNull(hmGuests
					.get("USER_ISTMP")));
			userInfoBean.setDeleteRow(ParseUtil.checkNull(hmGuests
					.get("USER_DELROW")));

            userInfoBean.setHumanCellPhone( userInfoBean.getHumanFormattedPhoneNumber(userInfoBean.getCellPhone()) );
            userInfoBean.setHumanPhoneNum( userInfoBean.getHumanFormattedPhoneNumber(userInfoBean.getPhoneNum()) );

			guestBean.setUserInfoBean(userInfoBean);
		}

		return guestBean;
	}
}
