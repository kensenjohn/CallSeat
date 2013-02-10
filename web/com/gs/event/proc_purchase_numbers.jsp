<%@page import="org.json.*" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="java.util.*"%>
<%@page import="com.gs.json.*"%>
<%@page import="com.gs.payment.*"%>
<%@page import="com.gs.manager.event.*"%>
<%@page import="com.gs.common.*" %>
<%@page import="com.gs.phone.account.*" %>
<%@ page import="com.gs.common.usage.UsageMetaData" %>
<%@ page import="com.gs.bean.usage.PhoneCallUsageBean" %>
<%@ page import="com.gs.common.usage.PhoneCallUsage" %>
<%@ page import="com.gs.common.usage.Usage" %>
<%@ page import="com.gs.bean.usage.UsageBean" %>
<%@ page import="com.gs.common.usage.TextMessageUsage" %>
<%@ page import="com.gs.bean.usage.TextMessageUsageBean" %>

<%@include file="../common/security.jsp" %>
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
        String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
        String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
        String sUniquePurchaseToken =  ParseUtil.checkNull(request.getParameter("unique_purchase_token"));
        String sGateAdminId = sAdminId;
%>
<%@include file="../common/gatekeeper.jsp"%>
<%
        PurchaseTransactionManager purchaseTransactionManager = new PurchaseTransactionManager();
        if(isSignedIn)
        {
            if(sAdminId!=null && !"".equalsIgnoreCase(sAdminId)  && sEventId!=null && !"".equalsIgnoreCase(sEventId))
            {
                PurchaseTransactionBean purchaseTransactionBean = new PurchaseTransactionBean();
                purchaseTransactionBean.setAdminId(sAdminId);
                purchaseTransactionBean.setEventId(sEventId);


                PurchaseTransactionBean purchaseResponseTransactionBean = purchaseTransactionManager.getPurchaseTransactionByEventAdmin(purchaseTransactionBean);

                if(purchaseResponseTransactionBean!=null)
                {
                    if(sUniquePurchaseToken!=null && sUniquePurchaseToken.equalsIgnoreCase(purchaseResponseTransactionBean.getUniquePurchaseToken()))
                    {

                        EventPricingGroupManager eventPricingGroupManager = new EventPricingGroupManager();
                        PricingGroupBean pricingGroupBean = eventPricingGroupManager.getPricingGroups(purchaseResponseTransactionBean.getPriceGroupId());

                        if(pricingGroupBean!=null )
                        {
                            CheckoutBean checkoutBean = eventPricingGroupManager.getCheckoutBean(sAdminId, sEventId);


                            BillingMetaData billingMetaData = new BillingMetaData();
                            billingMetaData.setAdminId(sAdminId);
                            billingMetaData.setEventId(sEventId);
                            billingMetaData.setFirstName(purchaseResponseTransactionBean.getFirstName());
                            billingMetaData.setLastName(purchaseResponseTransactionBean.getLastName());
                            billingMetaData.setMiddletName("");
                            billingMetaData.setAddress1("");
                            billingMetaData.setAddress2("");
                            billingMetaData.setZip(purchaseResponseTransactionBean.getZipcode());
                            billingMetaData.setCity("");
                            billingMetaData.setState(purchaseResponseTransactionBean.getState());
                            billingMetaData.setCountry(purchaseResponseTransactionBean.getCountry());
                            billingMetaData.setCreditCardNum("");
                            billingMetaData.setSecureNum("");
                            if(checkoutBean.getFormattedGrandTotal()!=null && !"".equalsIgnoreCase(checkoutBean.getFormattedGrandTotal())
                                    && checkoutBean.getFormattedGrandTotal().length()>1)
                            {
                                billingMetaData.setPrice(checkoutBean.getFormattedGrandTotal().substring(1));
                            }
                            else
                            {
                                throw new Exception();
                            }

                            billingMetaData.setCardLast4(purchaseResponseTransactionBean.getCreditCardLast4Digits());
                            billingMetaData.setStripeToken(purchaseResponseTransactionBean.getStripeToken());
                            billingMetaData.setStripeTokenUsed(true);

                            AdminManager adminManager  = new AdminManager();
                            UserInfoBean adminUserInfoBean = adminManager.getAminUserInfo(sAdminId);
                            if(adminUserInfoBean!=null)
                            {
                                billingMetaData.setEmail(ParseUtil.checkNull(adminUserInfoBean.getEmail()));
                            }


                            BillingManager billingManager = new BillingManager();

                            BillingResponse billingResponse = billingManager.chargePriceToUser(billingMetaData);

                            if(billingResponse!=null && Constants.BILLING_RESPONSE_CODES.SUCCESS.equals( billingResponse.getBillingResponseCode()))
                            {
                                AdminTelephonyAccountMeta adminAccountMeta = new AdminTelephonyAccountMeta();
                                adminAccountMeta.setAdminId(sAdminId);
                                adminAccountMeta.setFriendlyName(purchaseResponseTransactionBean.getLastName()+billingMetaData.getEmail()+purchaseResponseTransactionBean.getZipcode());

                                AdminTelephonyAccountManager accountManager = new AdminTelephonyAccountManager();
                                accountManager.createAccount(adminAccountMeta);

                                TelNumberManager telNumManager = new TelNumberManager();
                                //String sPurchasedRsvpNum = "678690589";
                                String sPurchasedRsvpNum =  telNumManager.purchaseTelephoneNumber(adminAccountMeta,purchaseResponseTransactionBean.getRsvpTelNumber() );
                                String sPurchasedSeatingNum = telNumManager.purchaseTelephoneNumber(adminAccountMeta,purchaseResponseTransactionBean.getSeatingTelNumber() );

                                if(pricingGroupBean!=null)
                                {
                                    EventFeatureManager eventFeatureManager = new EventFeatureManager();
                                    eventFeatureManager.createEventFeatures(sEventId, Constants.EVENT_FEATURES.PREMIUM_TOTAL_CALL_MINUTES,ParseUtil.iToS(pricingGroupBean.getMaxMinutes()));
                                    eventFeatureManager.createEventFeatures(sEventId, Constants.EVENT_FEATURES.PREMIUM_TOTAL_TEXT_MESSAGES,ParseUtil.iToS(pricingGroupBean.getSmsCount()));


                                    UsageMetaData usageMetaData = new UsageMetaData();
                                    usageMetaData.setEventId(sEventId);
                                    usageMetaData.setAdminId(sAdminId);

                                    Usage phoneCallUsage = new PhoneCallUsage();
                                    PhoneCallUsageBean phoneCallUsageBean = (PhoneCallUsageBean)phoneCallUsage.getUsage(usageMetaData);
                                    jspLogging.error("Phone CAll Usage : " + phoneCallUsageBean );
                                    if(phoneCallUsageBean!=null)
                                    {
                                        eventFeatureManager.createEventFeatures(sEventId , Constants.EVENT_FEATURES.DEMO_FINAL_CALL_MINUTES_USED, ParseUtil.iToS(phoneCallUsageBean.getNumOfDemoMinutesUsed()) );
                                    }

                                    Usage textMessageUsage = new TextMessageUsage();
                                    TextMessageUsageBean textMessageUsageBean = (TextMessageUsageBean)textMessageUsage.getUsage(usageMetaData);
                                    if(textMessageUsageBean!=null)
                                    {
                                        eventFeatureManager.createEventFeatures(sEventId , Constants.EVENT_FEATURES.DEMO_FINAL_TEXT_MESSAGES_SENT, ParseUtil.iToS(textMessageUsageBean.getNumOfDemoTextSent()) );
                                    }

                                }

                                if(sPurchasedRsvpNum!=null && !"".equalsIgnoreCase(sPurchasedRsvpNum)
                                        && sPurchasedSeatingNum!=null && !"".equalsIgnoreCase(sPurchasedSeatingNum)){



                                    TelNumberMetaData telNumberMetaData = new TelNumberMetaData();
                                    telNumberMetaData.setAdminId(sAdminId);
                                    telNumberMetaData.setEventId(sEventId);
                                    telNumberMetaData.setRsvpTelNumDigit(purchaseResponseTransactionBean.getRsvpTelNumber());
                                    telNumberMetaData.setSeatingTelNumDigit(purchaseResponseTransactionBean.getSeatingTelNumber());

                                    telNumManager.saveTelNumbers(telNumberMetaData);
                                    telNumManager.sendNewTelnumberPurchasedEmail(telNumberMetaData,adminUserInfoBean);

                                    Text okText = new OkText("Your purchase was completed successfully.","my_id");
                                    arrOkText.add(okText);

                                    responseStatus = RespConstants.Status.OK;
                                }
                                else
                                {
                                    Text errorText = new ErrorText("Your purchase was not completed."+
                                            "Please try again later.","my_id") ;
                                    arrErrorText.add(errorText);

                                    responseStatus = RespConstants.Status.ERROR;

                                    jspLogging.error("Error purchasing your selected number from the telephony provider." );
                                }
                            }
                            else
                            {
                                Text errorText = new ErrorText("Oops the purchase could not be completed. " + billingResponse.getMessage(),"my_id") ;
                                arrErrorText.add(errorText);

                                responseStatus = RespConstants.Status.ERROR;

                                jspLogging.error("Error purchasing , response code is emtpy or null."  + billingResponse.getMessage() );
                            }

                        }
                        else
                        {
                            Text errorText = new ErrorText("Oops the purchase could not be completed. Please try again later.","my_id") ;
                            arrErrorText.add(errorText);

                            responseStatus = RespConstants.Status.ERROR;

                            jspLogging.error("Pricing group bean does not exist" );
                        }
                    }
                    else
                    {
                        Text errorText = new ErrorText("Oops the purchase could not be completed. Please try again later.","my_id") ;
                        arrErrorText.add(errorText);

                        responseStatus = RespConstants.Status.ERROR;

                        jspLogging.error("Matching Unique Purchase Token Failed "  + sUniquePurchaseToken +" = " + purchaseResponseTransactionBean.getUniquePurchaseToken() );
                    }

                }
                else
                {
                    Text errorText = new ErrorText("Your action was not recognized. Please select valid telephone numbers and try again..","my_id") ;
                    arrErrorText.add(errorText);

                    responseStatus = RespConstants.Status.ERROR;
                    //jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
                    jspLogging.error("Missing purcvhase Transaction Admin Id : " + sAdminId  + " Event ID : " +  sEventId );
                }

            }
            else
            {
                Text errorText = new ErrorText("Please try again later. Your request was not completed.","my_id") ;
                arrErrorText.add(errorText);

                responseStatus = RespConstants.Status.ERROR;
                //jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
                jspLogging.error("Missing paramteres in request Admin Id : " + sAdminId  + " Event ID : " +  sEventId );
            }
        }
        else
        {
            Text errorText = new ErrorText("Oops!! You are currently not logged in to complete this purchase. Please log in or register..","my_id") ;
            arrErrorText.add(errorText);

            responseStatus = RespConstants.Status.ERROR;
            //jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
            jspLogging.error("User has not signed in Admin Id : " + sAdminId  + " Event ID : " +  sEventId );
        }

        jspLogging.error("Response status  : " + responseStatus.getStatus() );
        PurchaseTransactionBean tmpPurchaseTransactionBean = new PurchaseTransactionBean();
        tmpPurchaseTransactionBean.setAdminId(sAdminId);
        tmpPurchaseTransactionBean.setEventId(sEventId);
        PurchaseTransactionBean purchaseResponseTransactionBean = purchaseTransactionManager.getPurchaseTransactionByEventAdmin(tmpPurchaseTransactionBean);

        if(purchaseResponseTransactionBean!=null)
        {
            String sNewUniquePurchaseToken = Utility.getNewGuid();
            purchaseResponseTransactionBean.setUniquePurchaseToken(sNewUniquePurchaseToken);

            if( RespConstants.Status.ERROR.getStatus().equalsIgnoreCase(responseStatus.getStatus()))
            {

                jsonResponseObj.put("unique_purchase_token",   sNewUniquePurchaseToken );
            }
            else if(RespConstants.Status.OK.getStatus().equalsIgnoreCase(responseStatus.getStatus()))
            {
                // This indicates purchase is successful.
                // Update transaction to "purchase complete"
                purchaseResponseTransactionBean.setPurchaseComplete(true);
            }

            purchaseTransactionManager.modifyPurchaseTransaction(purchaseResponseTransactionBean);
        }


        responseObject.setErrorMessages(arrErrorText);
        responseObject.setOkMessages(arrOkText);
        responseObject.setResponseStatus(responseStatus);
        responseObject.setJsonResponseObj(jsonResponseObj);

        out.println(responseObject.getJson());
    }
    catch(Exception e)
    {
        jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
        jsonResponseObj.put("message", "Oops!! Your request was not processed. Please try again later.");
        appLogging.error("Error purchasing phone number package." + ExceptionHandler.getStackTrace(e) );
        out.println(jsonResponseObj);
    }
%>