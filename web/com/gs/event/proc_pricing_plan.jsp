<%@page import="com.gs.manager.event.*" %>
<%@page import="com.gs.manager.*" %>
<%@page import="com.gs.bean.*" %>
<%@page import="com.gs.common.*" %>
<%@page import="org.json.*" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="java.util.*"%>
<%@page import="com.gs.json.*"%>
<%
JSONObject jsonResponseObj = new JSONObject();

Logger jspLogging = LoggerFactory.getLogger("JspLogging");
Logger appLogging = LoggerFactory.getLogger("AppLogging");
response.setContentType("application/json");

    boolean isProcessTransactionId = ParseUtil.sTob(request.getParameter("process_purchase_transaction"));
    String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
    String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
    String sPricingGroupId = ParseUtil.checkNull(request.getParameter("purchase_grid_id"));

ArrayList<Text> arrOkText = new ArrayList<Text>();
ArrayList<Text> arrErrorText = new ArrayList<Text>();
RespConstants.Status responseStatus = RespConstants.Status.ERROR;

RespObjectProc responseObject = new RespObjectProc();


try
{
    if(isProcessTransactionId)
    {
        if(sAdminId!=null && !"".equalsIgnoreCase(sAdminId) && sEventId!=null && !"".equalsIgnoreCase(sEventId)
                && sPricingGroupId!=null && !"".equalsIgnoreCase(sPricingGroupId))
        {
            PurchaseTransactionManager purchaseTransactionManager = new PurchaseTransactionManager();

            PurchaseTransactionBean requestPurchaseTransactionBean = new  PurchaseTransactionBean();
            requestPurchaseTransactionBean.setAdminId(sAdminId);
            requestPurchaseTransactionBean.setEventId(sEventId);

            PurchaseTransactionBean responsePurchaseTransactionBean = purchaseTransactionManager.getPurchaseTransactionByEventAdmin(requestPurchaseTransactionBean);
            appLogging.info("Response Purchase Transaction Id." + responsePurchaseTransactionBean );
            Integer iNumOfRows = 0;
            if(responsePurchaseTransactionBean != null && responsePurchaseTransactionBean.getPurchaseTransactionId()!=null && !"".equalsIgnoreCase(responsePurchaseTransactionBean.getPurchaseTransactionId()))
            {
                responsePurchaseTransactionBean.setPriceGroupId(sPricingGroupId);
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
    }
    else
    {
        EventPricingGroupManager eventPricingManager = new EventPricingGroupManager();
        ArrayList<PricingGroupBean> arrPricingBean = eventPricingManager.getPricingGroups();

        JSONArray jsonPricingGroupArray = eventPricingManager.getPricingGroupJsonArray(arrPricingBean);
        if(arrPricingBean!=null && !arrPricingBean.isEmpty() && jsonPricingGroupArray!=null)
        {
            //jsonResponseObj.put(Constants.J_RESP_SUCCESS, true);
            jsonResponseObj.put("value",jsonPricingGroupArray);

            Text okText = new OkText("Loading Data Complete ","my_id");
            arrOkText.add(okText);
            responseStatus = RespConstants.Status.OK;
        }
        else
        {
            appLogging.error("Error retrieving Pricing Groups ");

            Text errorText = new ErrorText("The pricing data could not be accessed at this time. Please try again later.","my_id") ;
            arrErrorText.add(errorText);

            responseStatus = RespConstants.Status.ERROR;
        }
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