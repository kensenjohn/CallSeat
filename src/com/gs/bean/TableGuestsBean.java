package com.gs.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class TableGuestsBean
{
	/*
	 * FK_EVENTID | TABLEID | TABLEID | TABLENAME | TABLENUM | NUMOFSEATS |
	 * IS_TMP | DEL_ROW | CREATEDATE | FK_ADMINID | MODIFYDATE | MODIFIEDBY
	 * TABLEGUESTID | FK_TABLEID | FK_GUESTID | IS_TMP | DEL_ROW |
	 * ASSIGNED_SEATS | USERINFOID | FIRST_NAME | LAST_NAME | ADDRESS_1 |
	 * ADDRESS_2 | CITY | STATE | COUNTRY | IP_ADDRESS | CELL_PHONE | PHONE_NUM
	 * | EMAIL | IS_TMP | DEL_ROW | CREATEDATE | HUMAN_CREATEDATE |
	 */

	/*
	 * TABLEID,TABLENAME, TABLENUM, NUMOFSEATS, IS_TMP, DEL_ROW, F
	 * K_ADMINID,MODIFYDATE, MODIFIEDBY, TABLEGUESTID, FK_GUESTID, IS_TMP,
	 * DEL_ROW, ASSIGNED_SEATS, GG.GUESTID, GU.FIRST_NAME, GU.LAST_NAME,
	 * GU.CELL_PHONE, GU.PHONE_NUM , GEG.RSVP_SEATS, " + "
	 * GEG.TOTAL_INVITED_SEATS
	 */

	private String tableId = "";
	private String tableName = "";
	private String tableNum = "";
	private String numOfSeats = "";
	private String isTemporary = "";
	private String delelteRow = "";
	private String adminId = "";
	private Long createDate = 0L;
	private Long modifyDate = 0L;
	private String modifiedBy = "";
	private String tableGuestId = "";
	private String guestId = "";
	private String guestTableIsTmp = "";
	private String guestTableDelRow = "";
	private String guestAssignedSeats = "";

	private String firstName = "";
	private String lastName = "";
	private String cellPhone = "";
	private String phoneNum = "";
	private String rsvpSeats = "";
	private String totalInvitedSeats = "";

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getCellPhone()
	{
		return cellPhone;
	}

	public void setCellPhone(String cellPhone)
	{
		this.cellPhone = cellPhone;
	}

	public String getPhoneNum()
	{
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum)
	{
		this.phoneNum = phoneNum;
	}

	public String getRsvpSeats()
	{
		return rsvpSeats;
	}

	public void setRsvpSeats(String rsvpSeats)
	{
		this.rsvpSeats = rsvpSeats;
	}

	public String getTotalInvitedSeats()
	{
		return totalInvitedSeats;
	}

	public void setTotalInvitedSeats(String totalInvitedSeats)
	{
		this.totalInvitedSeats = totalInvitedSeats;
	}

	public String getTableId()
	{
		return tableId;
	}

	public void setTableId(String tableId)
	{
		this.tableId = tableId;
	}

	public String getTableName()
	{
		return tableName;
	}

	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	public String getNumOfSeats()
	{
		return numOfSeats;
	}

	public void setNumOfSeats(String numOfSeats)
	{
		this.numOfSeats = numOfSeats;
	}

	public String getIsTemporary()
	{
		return isTemporary;
	}

	public void setIsTemporary(String isTemporary)
	{
		this.isTemporary = isTemporary;
	}

	public String getDelelteRow()
	{
		return delelteRow;
	}

	public void setDelelteRow(String delelteRow)
	{
		this.delelteRow = delelteRow;
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

	public Long getModifyDate()
	{
		return modifyDate;
	}

	public void setModifyDate(Long modifyDate)
	{
		this.modifyDate = modifyDate;
	}

	public String getModifiedBy()
	{
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy)
	{
		this.modifiedBy = modifiedBy;
	}

	public String getTableGuestId()
	{
		return tableGuestId;
	}

	public void setTableGuestId(String tableGuestId)
	{
		this.tableGuestId = tableGuestId;
	}

	public String getGuestId()
	{
		return guestId;
	}

	public void setGuestId(String guestId)
	{
		this.guestId = guestId;
	}

	public String getGuestTableIsTmp()
	{
		return guestTableIsTmp;
	}

	public void setGuestTableIsTmp(String guestTableIsTmp)
	{
		this.guestTableIsTmp = guestTableIsTmp;
	}

	public String getGuestTableDelRow()
	{
		return guestTableDelRow;
	}

	public void setGuestTableDelRow(String guestTableDelRow)
	{
		this.guestTableDelRow = guestTableDelRow;
	}

	public String getGuestAssignedSeats()
	{
		return guestAssignedSeats;
	}

	public void setGuestAssignedSeats(String guestAssignedSeats)
	{
		this.guestAssignedSeats = guestAssignedSeats;
	}

	public String getTableNum()
	{
		return tableNum;
	}

	public void setTableNum(String tableNum)
	{
		this.tableNum = tableNum;
	}

	public JSONObject toJson()
	{

		JSONObject jsonObject = new JSONObject();
		try
		{
			jsonObject.put("table_id", tableId);
			jsonObject.put("table_name", tableName);
			jsonObject.put("table_num", tableNum);
			jsonObject.put("num_of_seats", numOfSeats);
			jsonObject.put("is_tmp", isTemporary);
			jsonObject.put("del_row", delelteRow);
			jsonObject.put("create_date", createDate);
			jsonObject.put("admin_id", adminId);
			jsonObject.put("modify_date", modifyDate);
			jsonObject.put("modified_by", modifiedBy);
			jsonObject.put("table_guest_id", tableGuestId);
			jsonObject.put("guest_table_is_tmp", guestTableIsTmp);
			jsonObject.put("guest_table_del_row", guestTableDelRow);
			jsonObject.put("guest_assigned_seats", guestAssignedSeats);

			/*
			 * GG.GUESTID, GU.FIRST_NAME, GU.LAST_NAME, GU.CELL_PHONE,
			 * GU.PHONE_NUM , GEG.RSVP_SEATS, " + " GEG.TOTAL_INVITED_SEATS
			 */

			jsonObject.put("guest_first_name", firstName);
			jsonObject.put("guest_last_name", lastName);
			jsonObject.put("guest_cell_phone", cellPhone);
			jsonObject.put("guest_phone_num", phoneNum);
			jsonObject.put("guest_rsvp_num", rsvpSeats);
			jsonObject.put("guest_invited_num", totalInvitedSeats);

		} catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}

	@Override
	public String toString()
	{
		return "TableGuestsBean [tableId=" + tableId + ", tableName=" + tableName + ", tableNum="
				+ tableNum + ", numOfSeats=" + numOfSeats + ", isTemporary=" + isTemporary
				+ ", delelteRow=" + delelteRow + ", adminId=" + adminId + ", createDate="
				+ createDate + ", modifyDate=" + modifyDate + ", modifiedBy=" + modifiedBy
				+ ", tableGuestId=" + tableGuestId + ", guestId=" + guestId + ", guestTableIsTmp="
				+ guestTableIsTmp + ", guestTableDelRow=" + guestTableDelRow
				+ ", guestAssignedSeats=" + guestAssignedSeats + "]";
	}

}
