package com.gs.manager.event;

import com.gs.bean.TableGuestsBean;

public class GuestTableResponse
{
	private boolean isSuccess = false;
	private String message = "";
	private TableGuestsBean tableGuestsBean = new TableGuestsBean();

	public boolean isSuccess()
	{
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess)
	{
		this.isSuccess = isSuccess;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public TableGuestsBean getTableGuestsBean()
	{
		return tableGuestsBean;
	}

	public void setTableGuestsBean(TableGuestsBean tableGuestsBean)
	{
		this.tableGuestsBean = tableGuestsBean;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("GuestTableResponse [isSuccess=").append(isSuccess).append(", message=")
				.append(message).append(", tableGuestsBean=").append(tableGuestsBean).append("]");
		return builder.toString();
	}

}
