package com.gs.bean;

import com.gs.common.Utility;

public class AdminBean
{
	private String adminId = "";
	private String userInfoId = "";
	private Long createDate = 0L;
	private String isTemporary = "1";
	private String deleteRow = "0";

	public AdminBean()
	{
		this.adminId = Utility.getNewGuid();
	}

	public String getAdminId()
	{
		return adminId;
	}

	public void setAdminId(String adminId)
	{
		this.adminId = adminId;
	}

	public String getUserInfoId()
	{
		return userInfoId;
	}

	public void setUserInfoId(String userInfoId)
	{
		this.userInfoId = userInfoId;
	}

	public Long getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Long createDate)
	{
		this.createDate = createDate;
	}

	public String getIsTemporary()
	{
		return isTemporary;
	}

	public void setIsTemporary(String isTemporary)
	{
		this.isTemporary = isTemporary;
	}

	public String getDeleteRow()
	{
		return deleteRow;
	}

	public void setDeleteRow(String deleteRow)
	{
		this.deleteRow = deleteRow;
	}

	@Override
	public String toString()
	{
		return "AdminBean [adminId=" + adminId + ", userInfoId=" + userInfoId + ", createDate="
				+ createDate + ", isTemporary=" + isTemporary + ", deleteRow=" + deleteRow + "]";
	}
}
