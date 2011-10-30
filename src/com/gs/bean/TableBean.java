package com.gs.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class TableBean
{

	private String tableId = "";
	private String tableName = "";
	private String tableNum = "";
	private String numOfSeats = "";
	private String isTmp = "";
	private String delRow = "";
	private Long createDate = 0L;
	private String adminId = "";
	private Long modifyDate = 0L;
	private String modifyBy = "";

	private String humanCreateDate = "";
	private String humanModifyDate = "";

	public String getTableId()
	{
		return tableId;
	}

	public String getTableNum()
	{
		return tableNum;
	}

	public void setTableNum(String tableNum)
	{
		this.tableNum = tableNum;
	}

	public String getNumOfSeats()
	{
		return numOfSeats;
	}

	public void setNumOfSeats(String numOfSeats)
	{
		this.numOfSeats = numOfSeats;
	}

	public String getIsTmp()
	{
		return isTmp;
	}

	public void setIsTmp(String isTmp)
	{
		this.isTmp = isTmp;
	}

	public String getDelRow()
	{
		return delRow;
	}

	public void setDelRow(String delRow)
	{
		this.delRow = delRow;
	}

	public Long getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Long createDate)
	{
		this.createDate = createDate;
	}

	public String getAdminId()
	{
		return adminId;
	}

	public void setAdminId(String adminId)
	{
		this.adminId = adminId;
	}

	public Long getModifyDate()
	{
		return modifyDate;
	}

	public void setModifyDate(Long modifyDate)
	{
		this.modifyDate = modifyDate;
	}

	public String getModifyBy()
	{
		return modifyBy;
	}

	public void setModifyBy(String modifyBy)
	{
		this.modifyBy = modifyBy;
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

	public String getHumanCreateDate()
	{
		return humanCreateDate;
	}

	public void setHumanCreateDate(String humanCreateDate)
	{
		this.humanCreateDate = humanCreateDate;
	}

	public String getHumanModifyDate()
	{
		return humanModifyDate;
	}

	public void setHumanModifyDate(String humanModifyDate)
	{
		this.humanModifyDate = humanModifyDate;
	}

	@Override
	public String toString()
	{
		return "TableBean [tableId=" + tableId + ", tableName=" + tableName + ", tableNum="
				+ tableNum + ", numOfSeats=" + numOfSeats + ", isTmp=" + isTmp + ", delRow="
				+ delRow + ", createDate=" + createDate + ", adminId=" + adminId + ", modifyDate="
				+ modifyDate + ", modifyBy=" + modifyBy + ", humanCreateDate=" + humanCreateDate
				+ ", humanModifyDate=" + humanModifyDate + "]";
	}

	public JSONObject toJson()
	{
		/*
		 * private String tableId = ""; private String tableName = ""; private
		 * String tableNum = ""; private String numOfSeats = ""; private String
		 * isTmp = ""; private String delRow = ""; private Long createDate = 0L;
		 * private String adminId = ""; private Long modifyDate = 0L; private
		 * String modifyBy = "";
		 */
		JSONObject jsonObject = new JSONObject();
		try
		{
			jsonObject.put("table_id", tableId);
			jsonObject.put("table_name", tableName);
			jsonObject.put("table_num", tableNum);
			jsonObject.put("num_of_seats", numOfSeats);
			jsonObject.put("createDate", createDate);
			jsonObject.put("admin_id", adminId);

		} catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}

}
