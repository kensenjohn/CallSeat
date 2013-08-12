<%@page import="com.gs.manager.event.*" %>
<%@page import="com.gs.bean.*" %>
<%@page import="com.gs.common.*" %>
<%@page import="org.json.*" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="java.util.*"%>
<%@page import="com.gs.json.*"%>
<%@include file="/web/com/gs/common/security_proc_page.jsp"%>
<%
JSONObject jsonResponseObj = new JSONObject();

Logger jspLogging = LoggerFactory.getLogger("JspLogging");
Logger appLogging = LoggerFactory.getLogger("AppLogging");
response.setContentType("application/json");

ArrayList<Text> arrOkText = new ArrayList<Text>();
ArrayList<Text> arrErrorText = new ArrayList<Text>();
RespConstants.Status responseStatus = RespConstants.Status.ERROR;

RespObjectProc responseObject = new RespObjectProc();

try
{

	

 	boolean isSaveData = ParseUtil.sTob(request.getParameter("save_data"));
 	boolean isEditTableAction = ParseUtil.sTob(request.getParameter("edit_table"));

	String sEventId =  ParseUtil.checkNull(request.getParameter("event_id"));
	String sAdminId =  ParseUtil.checkNull(request.getParameter("admin_id"));
	String sTableName =  ParseUtil.checkNull(request.getParameter("table_name"));
	String sTableNum =  ParseUtil.checkNull(request.getParameter("table_num"));
	String sNumOfSeats =  ParseUtil.checkNull(request.getParameter("num_of_seats"));
	String sTableId =  ParseUtil.checkNull(request.getParameter("table_id"));

    boolean isValidationError = false;
	if(sTableName==null || "".equalsIgnoreCase(sTableName) || sTableNum==null || "".equalsIgnoreCase(sTableNum)
			|| sNumOfSeats==null || "".equalsIgnoreCase(sNumOfSeats)  )
	{

		if(isEditTableAction)
		{
			appLogging.warn("Error in data to editing Table for event " + sEventId );
		}
		else
		{
			appLogging.warn("Error in data to create Table for event " + sEventId );
		}
		
		Text errorText = new ErrorText("Please fill in all the fields.","my_id") ;		
		arrErrorText.add(errorText);
		
		responseStatus = RespConstants.Status.ERROR;

        isValidationError = true;
		
	}
    else if( ParseUtil.sToI(sTableNum) <= 0 )
    {
        appLogging.warn("Invalid Table number used " + sEventId );
        Text errorText = new ErrorText("Please select a valid table number.","my_id") ;
        arrErrorText.add(errorText);
        responseStatus = RespConstants.Status.ERROR;

        isValidationError = true;
    }
    else if( ParseUtil.sToI(sNumOfSeats) <= 0 )
    {
        appLogging.warn("Invalid seat number used " + sEventId );
        Text errorText = new ErrorText("Please select a valid seat number.","my_id") ;
        arrErrorText.add(errorText);
        responseStatus = RespConstants.Status.ERROR;

        isValidationError = true;
    }

    TableManager tableManager = new TableManager();
    ArrayList<EventTableBean> arrEventTableBean = tableManager.getTableByNum( sTableNum , sEventId);

    if(arrEventTableBean!=null && !arrEventTableBean.isEmpty())
    {
        appLogging.warn("Invalid seat number used " + sEventId );
        Text errorText = new ErrorText("The selected table number already exists for this event. Please select a different table number.","my_id") ;
        arrErrorText.add(errorText);
        responseStatus = RespConstants.Status.ERROR;

        isValidationError = true;
    }

    if(!isValidationError)
    {
        if(isSaveData )
        {
            if(!"".equalsIgnoreCase(sEventId) &&  !"".equalsIgnoreCase(sAdminId))
            {
                if(!isEditTableAction)
                {
                    sTableId = (Utility.getNewGuid());
                }
                Long lCurrentDate = DateSupport.getEpochMillis();
                TableBean tableBean = new TableBean();
                tableBean.setTableId(sTableId);
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
                tableBean.setHumanModifyDate(sHumanDate);
                if(!isEditTableAction)
                {
                    tableBean.setHumanCreateDate(sHumanDate);
                    tableBean = tableManager.createNewTable(tableBean);
                }
                else
                {
                    Integer iNumOfRows = tableManager.updateTable(tableBean);
                    if(iNumOfRows <= 0)
                    {
                        tableBean = null;
                    }
                }



                if(tableBean!=null && tableBean.getTableId()!=null && !"".equalsIgnoreCase(tableBean.getTableId()))
                {
                    appLogging.info("Success saving table  for event " + sEventId + " table : " + tableBean.getTableId() );

                    if(!isEditTableAction)
                    {
                        EventManager eventManager = new EventManager();
                        EventTableBean eventTableBean = eventManager.assignTableToEvent(sEventId,tableBean.getTableId());

                        if(eventTableBean!=null && !"".equalsIgnoreCase(eventTableBean.getEventTableId()))
                        {
                            //jsonResponseObj.put(Constants.J_RESP_SUCCESS, true);
                            jsonResponseObj.put("value","1");

                            Text okText = new OkText("Loading Data Complete ","my_id");
                            arrOkText.add(okText);
                            responseStatus = RespConstants.Status.OK;
                        }
                        else
                        {
                            appLogging.error("Error assigning table : " + tableBean.getTableId() + "  to event : " + sEventId);

                            Text errorText = new ErrorText("The table could not be assigned to the event.","my_id") ;
                            arrErrorText.add(errorText);

                            responseStatus = RespConstants.Status.ERROR;
                        }
                    }
                    else
                    {
                        jsonResponseObj.put("value","1");

                        Text okText = new OkText("Your changes were saved successfully.","my_id");
                        arrOkText.add(okText);
                        responseStatus = RespConstants.Status.OK;
                    }



                }
                else
                {
                    appLogging.error("Error creating table for " + sEventId  + " Admin = " + sAdminId );

				/*jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);

				JSONObject jsonErrMessage = new JSONObject();
				jsonErrMessage.put(Constants.J_RESP_ERR_MSSG, "Please try again later." );

				jsonResponseObj.put(Constants.J_RESP_RESPONSE,jsonErrMessage);*/

                    Text errorText = new ErrorText("Please try again later. You request was not saved.","my_id") ;
                    arrErrorText.add(errorText);

                    responseStatus = RespConstants.Status.ERROR;
                }
            }
            else
            {
                appLogging.error(" Missing data in request eventid = " + sEventId + " admin id : " + sAdminId );
            }

        }
        else
        {
            //jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);

            Text errorText = new ErrorText("Oops!! Your request could not be processed at this time.","my_id") ;
            arrErrorText.add(errorText);

            responseStatus = RespConstants.Status.ERROR;
        }
    }
    else
    {
        appLogging.error("Validation error for event : " + sEventId);
    }
	
	appLogging.info("Response " + sEventId + " table : " + jsonResponseObj.toString());
	responseObject.setErrorMessages(arrErrorText);
	responseObject.setOkMessages(arrOkText);
	responseObject.setResponseStatus(responseStatus);
	responseObject.setJsonResponseObj(jsonResponseObj);
	
	out.println(responseObject.getJson());
}
catch(Exception e)
{
	//jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
	appLogging.error("Error creating table " );
	
	Text errorText = new ErrorText("Oops!! Your request could not be processed at this time.","my_id") ;		
	arrErrorText.add(errorText);
	
	responseObject.setErrorMessages(arrErrorText);
	responseObject.setResponseStatus(RespConstants.Status.ERROR);
	responseObject.setJsonResponseObj(jsonResponseObj);
	
	out.println(responseObject.getJson());
	
}
%>