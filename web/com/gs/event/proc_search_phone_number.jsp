<%@page import="com.gs.manager.event.*" %>
<%@page import="com.gs.common.*" %>
<%@ page import="java.util.*"%>
<%@page import="org.json.*" %>
<%@page import="com.gs.json.*"%>

<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="com.gs.bean.PurchaseTransactionBean" %>

<%
    JSONObject jsonResponseObj = new JSONObject();
    response.setContentType("application/json");


    Logger appLogging = LoggerFactory.getLogger("AppLogging");

    ArrayList<Text> arrOkText = new ArrayList<Text>();
    ArrayList<Text> arrErrorText = new ArrayList<Text>();
    RespConstants.Status responseStatus = RespConstants.Status.ERROR;

    RespObjectProc responseObject = new RespObjectProc();

    try
    {
        String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
        String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
        String sRsvpNumber = ParseUtil.checkNull(request.getParameter("purchase_transact_rsvp_num"));
        String sSeatingNumber = ParseUtil.checkNull(request.getParameter("purchase_transact_seating_num"));

        if(sAdminId!=null && !"".equalsIgnoreCase(sAdminId) && sEventId!=null && !"".equalsIgnoreCase(sEventId) )
        {
            PurchaseTransactionManager purchaseTransactionManager = new PurchaseTransactionManager();

            PurchaseTransactionBean requestPurchaseTransactionBean = new  PurchaseTransactionBean();
            requestPurchaseTransactionBean.setAdminId(sAdminId);
            requestPurchaseTransactionBean.setEventId(sEventId);
            requestPurchaseTransactionBean.setRsvpTelNumber(sRsvpNumber);
            requestPurchaseTransactionBean.setSeatingTelNumber(sSeatingNumber);

            PurchaseTransactionBean responsePurchaseTransactionBean = purchaseTransactionManager.getPurchaseTransactionByEventAdmin(requestPurchaseTransactionBean);
            appLogging.info("Response Purchase Transaction Id." + responsePurchaseTransactionBean );
            Integer iNumOfRows = 0;
            if(responsePurchaseTransactionBean != null && responsePurchaseTransactionBean.getPurchaseTransactionId()!=null && !"".equalsIgnoreCase(responsePurchaseTransactionBean.getPurchaseTransactionId()))
            {
                responsePurchaseTransactionBean.setRsvpTelNumber(requestPurchaseTransactionBean.getRsvpTelNumber());
                responsePurchaseTransactionBean.setSeatingTelNumber(requestPurchaseTransactionBean.getSeatingTelNumber());
                 // update Transaction with latest phone numbers
                iNumOfRows = purchaseTransactionManager.modifyPurchaseTransaction(responsePurchaseTransactionBean);
            }
            else
            {
                requestPurchaseTransactionBean.setPurchaseTransactionId(Utility.getNewGuid());
                // insert Transaction with latest phone numbers
                iNumOfRows = purchaseTransactionManager.createPurchaseTransaction(requestPurchaseTransactionBean);
            }

            if(iNumOfRows>0)
            {
                Text okText = new OkText("Loading Data Complete ","my_id");
                arrOkText.add(okText);
                responseStatus = RespConstants.Status.OK;
            }
            else
            {
                appLogging.error("Transaction records were not created." + responsePurchaseTransactionBean );
                Text errorText = new ErrorText("Your request was not processed. Please try again later.","my_id") ;
                arrErrorText.add(errorText);

                responseStatus = RespConstants.Status.ERROR;
            }

        }
        else
        {
            appLogging.error("Invalid data passed for creating purchasing transactions." );
            Text errorText = new ErrorText("Your request was not processed. Please try again later.","my_id") ;
            arrErrorText.add(errorText);

            responseStatus = RespConstants.Status.ERROR;
        }
        responseObject.setErrorMessages(arrErrorText);
        responseObject.setOkMessages(arrOkText);
        responseObject.setResponseStatus(responseStatus);
        responseObject.setJsonResponseObj(jsonResponseObj);

        out.println(responseObject.getJson());
    }
    catch(Exception e)
    {
        Text errorText = new ErrorText("Oops!! Your request could not be processed at this time.","my_id") ;
        arrErrorText.add(errorText);

        responseObject.setErrorMessages(arrErrorText);
        responseObject.setResponseStatus(RespConstants.Status.ERROR);
        responseObject.setJsonResponseObj(jsonResponseObj);

        appLogging.error("Error saving purchase transaction." );
        out.println(responseObject.getJson());
    }

%>