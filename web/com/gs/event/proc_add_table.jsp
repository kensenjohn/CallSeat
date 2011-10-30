<%@page import="com.gs.manager.event.*" %>
<%@page import="com.gs.bean.*" %>
<%@page import="com.gs.common.*" %>
<%@page import="org.json.*" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%
JSONObject jsonResponseObj = new JSONObject();

Logger jspLogging = LoggerFactory.getLogger("JspLogging");
Logger appLogging = LoggerFactory.getLogger("AppLogging");
response.setContentType("application/json");
try
{

	

 	boolean isSaveData = ParseUtil.sTob(request.getParameter("save_data"));

	String sEventId =  ParseUtil.checkNull(request.getParameter("event_id"));
	String sAdminId =  ParseUtil.checkNull(request.getParameter("admin_id"));
	String sTableName =  ParseUtil.checkNull(request.getParameter("table_name"));
	String sTableNum =  ParseUtil.checkNull(request.getParameter("table_num"));
	String sNumOfSeats =  ParseUtil.checkNull(request.getParameter("num_of_seats"));
	
	System.out.println("sEventId = " + sEventId + " isSaveData = " + isSaveData);
	
	if(sTableName==null || "".equalsIgnoreCase(sTableName) || sTableNum==null || "".equalsIgnoreCase(sTableNum)
			|| sNumOfSeats==null || "".equalsIgnoreCase(sNumOfSeats) )
	{

		appLogging.warn("Error in data to create Table for event " + sEventId );
		
		jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
		
		JSONObject jsonErrMessage = new JSONObject();
		jsonErrMessage.put(Constants.J_RESP_ERR_MSSG, "Please fill in all the fields" );
		
		jsonResponseObj.put(Constants.J_RESP_RESPONSE,jsonErrMessage);
		
	}
	
	if(isSaveData )
	{
		if(!"".equalsIgnoreCase(sEventId) &&  !"".equalsIgnoreCase(sAdminId))
		{
			TableManager tableManager = new TableManager();
			
			Long lCurrentDate = DateSupport.getEpochMillis();
			TableBean tableBean = new TableBean();
			tableBean.setTableId(Utility.getNewGuid());
			tableBean.setTableName(sTableName);
			tableBean.setTableNum(sTableNum);
			tableBean.setNumOfSeats(sNumOfSeats);
			tableBean.setIsTmp("1");
			tableBean.setDelRow("0");
			tableBean.setCreateDate(lCurrentDate);
			tableBean.setAdminId(sAdminId);
			tableBean.setModifyBy(sAdminId);
			tableBean.setModifyDate(lCurrentDate);
			
			String sHumanDate = DateSupport.getUTCDateTime();
			tableBean.setHumanCreateDate(sHumanDate);
			tableBean.setHumanModifyDate(sHumanDate);
			
			tableBean = tableManager.createNewTable(tableBean);
			
			if(tableBean!=null && tableBean.getTableId()!=null && !"".equalsIgnoreCase(tableBean.getTableId()))
			{
				appLogging.info("Success creating table  for event " + sEventId + " table : " + tableBean.getTableId() );
				
				EventManager eventManager = new EventManager();
				EventTableBean eventTableBean = eventManager.assignTableToEvent(sEventId,tableBean.getTableId());
				
				if(eventTableBean!=null && !"".equalsIgnoreCase(eventTableBean.getEventTableId()))
				{
					jsonResponseObj.put(Constants.J_RESP_SUCCESS, true);
					jsonResponseObj.put("value","1");
				}
				else
				{
					appLogging.error("Error assigning table : " + tableBean.getTableId() + "  to event : " + sEventId);
					jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
					
					JSONObject jsonErrMessage = new JSONObject();

					jsonErrMessage.put(Constants.J_RESP_ERR_MSSG, "The table could not be assigned to the event." );
					
					jsonResponseObj.put(Constants.J_RESP_RESPONSE,jsonErrMessage);
				}
			}
			else
			{
				appLogging.error("Error creating table for " + sEventId + " table : " + tableBean.getTableId() );
				
				jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
				
				JSONObject jsonErrMessage = new JSONObject();
				jsonErrMessage.put(Constants.J_RESP_ERR_MSSG, "Please try again later." );
				
				jsonResponseObj.put(Constants.J_RESP_RESPONSE,jsonErrMessage);
			}
		}
		
	}
	else
	{
		jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
	}
	appLogging.error("Response " + sEventId + " table : " + jsonResponseObj.toString());
	out.println(jsonResponseObj);
}
catch(Exception e)
{
	jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
	appLogging.error("Error creating table " );
	out.println(jsonResponseObj);
	
}
%>