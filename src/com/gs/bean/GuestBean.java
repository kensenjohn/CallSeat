package com.gs.bean;

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
	private String isTmp = "";
	private String delRow = "";

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

	@Override
	public String toString()
	{
		return "GuestBean [guestID=" + guestID + ", userInfoId=" + userInfoId + ", adminId="
				+ adminId + ", createDate=" + createDate + ", totalSeat=" + totalSeat + ", isTmp="
				+ isTmp + ", delRow=" + delRow + "]";
	}

}
