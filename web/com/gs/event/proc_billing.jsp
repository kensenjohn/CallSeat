<%@page import="com.gs.manager.event.*" %>
<%@page import="com.gs.manager.*" %>
<%@page import="com.gs.bean.*" %>
<%@page import="com.gs.common.*" %>
<%@page import="org.json.*" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="java.util.*"%>
<%@page import="com.gs.json.*"%>
<%@page import="com.validate.*"%>


<%@include file="/web/com/gs/common/security_proc_page.jsp"%>
<%
        JSONObject jsonResponseObj = new JSONObject();

        Logger jspLogging = LoggerFactory.getLogger("JspLogging");
        Logger appLogging = LoggerFactory.getLogger("AppLogging");
        response.setContentType("application/json");

        boolean isProcessTransactionId = ParseUtil.sTob(request.getParameter("process_purchase_transaction"));
        String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
        String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));

    String sPurchaseFirstName = ParseUtil.checkNull(request.getParameter("purchase_first_name"));
    String sPurchaseLastName = ParseUtil.checkNull(request.getParameter("purchase_last_name"));
    String sPurchaseState = ParseUtil.checkNull(request.getParameter("purchase_state"));
    String sPurchaseZipcode = ParseUtil.checkNull(request.getParameter("purchase_zip_code"));
    String sPurchaseCountry = ParseUtil.checkNull(request.getParameter("purchase_country"));
    String sPurchaseStripeToken = ParseUtil.checkNull(request.getParameter("purchase_stripe_token"));
    String sPurchaseLast4CreditCard = ParseUtil.checkNull(request.getParameter("purchase_cc_last4"));

        ArrayList<Text> arrOkText = new ArrayList<Text>();
        ArrayList<Text> arrErrorText = new ArrayList<Text>();
        RespConstants.Status responseStatus = RespConstants.Status.ERROR;

        RespObjectProc responseObject = new RespObjectProc();


        try
        {
            if(sAdminId!=null && !"".equalsIgnoreCase(sAdminId) && sEventId!=null && !"".equalsIgnoreCase(sEventId)
                    && sPurchaseState!=null && !"".equalsIgnoreCase(sPurchaseState)
                    && sPurchaseStripeToken!=null && !"".equalsIgnoreCase(sPurchaseStripeToken))
            {

                ValidateStatusBean validateStatusBean = ValidateStateZipCode.validate(sPurchaseState,sPurchaseZipcode);
                if(validateStatusBean.isValid() )
                {
                    PurchaseTransactionManager purchaseTransactionManager = new PurchaseTransactionManager();

                    PurchaseTransactionBean requestPurchaseTransactionBean = new  PurchaseTransactionBean();
                    requestPurchaseTransactionBean.setAdminId(sAdminId);
                    requestPurchaseTransactionBean.setEventId(sEventId);

                    PurchaseTransactionBean responsePurchaseTransactionBean = purchaseTransactionManager.getPurchaseTransactionByEventAdmin(requestPurchaseTransactionBean);

                    Integer iNumOfRows = 0;
                    if(responsePurchaseTransactionBean != null && responsePurchaseTransactionBean.getPurchaseTransactionId()!=null && !"".equalsIgnoreCase(responsePurchaseTransactionBean.getPurchaseTransactionId()))
                    {
                        responsePurchaseTransactionBean.setFirstName(sPurchaseFirstName);
                        responsePurchaseTransactionBean.setLastName(sPurchaseLastName);
                        responsePurchaseTransactionBean.setState(sPurchaseState);
                        responsePurchaseTransactionBean.setZipcode(sPurchaseZipcode);
                        responsePurchaseTransactionBean.setCountry(sPurchaseCountry);
                        responsePurchaseTransactionBean.setStripeToken(sPurchaseStripeToken);
                        responsePurchaseTransactionBean.setCreditCardLast4Digits(sPurchaseLast4CreditCard);
                        // update Transaction with latest phone numbers
                        appLogging.error("Purchase transaction record." + responsePurchaseTransactionBean );

                        iNumOfRows = purchaseTransactionManager.modifyPurchaseTransaction(responsePurchaseTransactionBean);
                    }
                    else
                    {
                        // shouldn't come here. This means there was no transaction record previously created.
                    }

                    if(iNumOfRows>0)
                    {
                        Text okText = new OkText("Transaction records were created.","my_id");
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
                    appLogging.error("Invalid state and zip code used. state : " + sPurchaseState + " zipcode : " + sPurchaseZipcode );

                    Text errorText = new ErrorText("We were unable to identify a valid zip code.","my_id") ;
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
            Text errorText = new ErrorText("Oops!! Your request could not be processed at this time. Please try again later.","my_id") ;
            arrErrorText.add(errorText);

            responseObject.setErrorMessages(arrErrorText);
            responseObject.setResponseStatus(RespConstants.Status.ERROR);
            responseObject.setJsonResponseObj(jsonResponseObj);

            appLogging.error("Error saving purchase transaction." );
            out.println(responseObject.getJson());
        }

%>