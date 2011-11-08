package com.gs.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class GuestBean
{
	/*
	 * GUESTID VARCHAR(45) NOT NULL, FK_USERINFOID VARCHAR(45) NOT NULL,
	 * FK_ADMINID VARCHAR(45) NOT NULL, CREATEDATE TIMESTAMP NOT NULL DEFAULT
	 * '1980-01-01 00:00:00', TOTAL_SEATS INT(11) NOT NULL DEFAULT 1, IS_TMP
	 * INT(1) NOT NULL DEFAULT 1, DEL_ROW INT(1) NOT NULL DEFAULT 0
	 */

	private String guestID = "";
	private String userInfoId = "";
	private String adminId = "";
	private Long createDate = 0L;
	private String totalSeat = "";
	private String rsvpSeat = "";
	private String isTmp = "";
	private String delRow = "";
	private String humanCreateDate = "";

	public String getHumanCreateDate()
	{
		return humanCreateDate;
	}

	public void setHumanCreateDate(String humanCreateDate)
	{
		this.humanCreateDate = humanCreateDate;
	}

	private UserInfoBean userInfoBean = null;

	public String getGuestId()
	{
		return guestID;
	}

	public void setGuestId(String guestID)
	{
		this.guestID = guestID;
	}

	public String getUserInfoId()
	{
		return userInfoId;
	}

	public void setUserInfoId(String userInfoId)
	{
		this.userInfoId = userInfoId;
	}

	public String getAdminId()
	{
		return adminId;
	}

	public void setAdminId(String adminId)
	{
		this.adminId = adminId;
	}

	public Long getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Long createDate)
	{
		this.createDate = createDate;
	}

	public String getTotalSeat()
	{
		return totalSeat;
	}

	public void setTotalSeat(String totalSeat)
	{
		this.totalSeat = totalSeat;
	}

	public String getIsTemporary()
	{
		return isTmp;
	}

	public void setIsTemporary(String isTmp)
	{
		this.isTmp = isTmp;
	}

	public String getDeleteRow()
	{
		return delRow;
	}

	public void setDeleteRow(String delRow)
	{
		this.delRow = delRow;
	}

	public String getRsvpSeat()
	{
		return rsvpSeat;
	}

	public void setRsvpSeat(String rsvpSeat)
	{
		this.rsvpSeat = rsvpSeat;
	}

	public UserInfoBean getUserInfoBean()
	{
		return userInfoBean;
	}

	public void setUserInfoBean(UserInfoBean userInfoBean)
	{
		this.userInfoBean = userInfoBean;
	}

	@Override
	public String toString()
	{
		return "GuestBean [guestID=" + guestID + ", userInfoId=" + userInfoId + ", adminId="
				+ adminId + ", createDate=" + createDate + ", totalSeat=" + totalSeat
				+ ", rsvpSeat=" + rsvpSeat + ", isTmp=" + isTmp + ", delRow=" + delRow + "]";
	}

	public JSONObject toJson()
	{

		JSONObject jsonObject = new JSONObject();
		try
		{
			jsonObject.put("guest_id", guestID);
			jsonObject.put("user_info_id", userInfoId);
			jsonObject.put("admin_id", adminId);
			jsonObject.put("create_date", createDate);
			jsonObject.put("total_seat", totalSeat);
			jsonObject.put("rsvp_seat", rsvpSeat);
			jsonObject.put("is_tmp", isTmp);
			jsonObject.put("del_row", delRow);

			if (this.userInfoBean != null)
			{
				JSONObject jsonUserInfoObject = new JSONObject();

				jsonObject.put("user_info", this.userInfoBean.toJson());

			}

		} catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}
}
