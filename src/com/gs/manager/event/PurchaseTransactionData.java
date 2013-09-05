package com.gs.manager.event;

import com.gs.bean.PricingGroupBean;
import com.gs.bean.PurchaseTransactionBean;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.DateSupport;
import com.gs.common.ParseUtil;
import com.gs.common.db.DBDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 1/2/13
 * Time: 11:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class PurchaseTransactionData {

    Logger appLogging = LoggerFactory.getLogger("AppLogging");
    Configuration applicationConfig = Configuration
            .getInstance(Constants.APPLICATION_PROP);

    private String ADMIN_DB = applicationConfig.get(Constants.ADMIN_DB);

    public PurchaseTransactionBean getPurchaseTransaction(String sAdminId, String sEventId)
    {
        PurchaseTransactionBean purchaseTransactionBean = new PurchaseTransactionBean();
        if (sAdminId != null && !"".equalsIgnoreCase(sAdminId) && sEventId!=null && !"".equalsIgnoreCase(sEventId)) {
            String sQuery = "select PURCHASETRANSACTIONID , FK_EVENTID  , FK_ADMINID  , IS_PURCHASE_COMPLETE , RSVP_TELNUMBER ," +
                    " SEATING_TELNUMBER , FK_PRICEGROUPID  , FIRSTNAME , LASTNAME , ADDRESS1  , STATE , ZIPCODE  , COUNTRY  ," +
                    " STRIPE_CUSTOMER_ID , STRIPE_TOKEN , CREDIT_CARD_LAST4_DIGITS , CREATEDATE , HUMANCREATEDATE   , UNIQUE_PURCHASE_TOKEN  , API_KEY_TYPE "+
                    " from GTPURCHASETRANSACTIONS where FK_ADMINID = ? and FK_EVENTID = ?";

            ArrayList<Object> aParams = DBDAO.createConstraint(sAdminId,sEventId);

            ArrayList<HashMap<String, String>> arrResult = DBDAO.getDBData(
                    ADMIN_DB, sQuery, aParams, false, "PurchaseTransactionData.java",
                    "getPurchaseTransaction(sAdminId,sEventId)");
            if (arrResult != null && !arrResult.isEmpty()) {
                for (HashMap<String, String> hmResult : arrResult) {

                    purchaseTransactionBean = new PurchaseTransactionBean(hmResult);
                    break;
                }
            }
        }
        return purchaseTransactionBean;
    }

    public PurchaseTransactionBean getPurchaseTransaction(String sPricingTransactionId)
    {
        PurchaseTransactionBean purchaseTransactionBean = new PurchaseTransactionBean();
        if(sPricingTransactionId!=null && !"".equalsIgnoreCase(sPricingTransactionId))
        {
            String sQuery = "select PURCHASETRANSACTIONID , FK_EVENTID  , FK_ADMINID  , IS_PURCHASE_COMPLETE , RSVP_TELNUMBER ," +
                    " SEATING_TELNUMBER , FK_PRICEGROUPID  , FIRSTNAME , LASTNAME , ADDRESS1  , STATE , ZIPCODE  , COUNTRY  ," +
                    " STRIPE_CUSTOMER_ID , STRIPE_TOKEN , CREDIT_CARD_LAST4_DIGITS , CREATEDATE , HUMANCREATEDATE   , UNIQUE_PURCHASE_TOKEN  , API_KEY_TYPE   "+
                    " from GTPURCHASETRANSACTIONS where PURCHASETRANSACTIONID = ?";

            ArrayList<Object> aParams = DBDAO.createConstraint(sPricingTransactionId);

            ArrayList<HashMap<String, String>> arrResult = DBDAO.getDBData(
                    ADMIN_DB, sQuery, aParams, false, "PurchaseTransactionData.java",
                    "getPurchaseTransaction(sPricingTransactionId)");

            if (arrResult != null && !arrResult.isEmpty()) {
                for (HashMap<String, String> hmResult : arrResult) {

                    purchaseTransactionBean = new PurchaseTransactionBean(hmResult);
                    break;
                }
            }
        }
        return purchaseTransactionBean;
    }

    public Integer insertPurchaseTransaction(PurchaseTransactionBean purchaseTransactionBean)
    {
        Integer iNumOfRows = 0;
        if(purchaseTransactionBean!=null)
        {
            String sQuery = "INSERT INTO GTPURCHASETRANSACTIONS (PURCHASETRANSACTIONID,FK_EVENTID,FK_ADMINID, " +
                    " IS_PURCHASE_COMPLETE,RSVP_TELNUMBER,SEATING_TELNUMBER, FK_PRICEGROUPID,FIRSTNAME,LASTNAME, STATE,ZIPCODE,STRIPE_CUSTOMER_ID," +
                    " CREATEDATE,HUMANCREATEDATE,UNIQUE_PURCHASE_TOKEN,API_KEY_TYPE ) "
                    + " VALUES(?,?,?,   ?,?,?,	 ?,?,?,  ?,?,?,  ?,?,?,  ?)";

            Long lCreateDate = DateSupport.getEpochMillis();
            String sHumanCreateDate = DateSupport.getUTCDateTime();

            ArrayList<Object> aParams = DBDAO.createConstraint(ParseUtil.checkNull(purchaseTransactionBean.getPurchaseTransactionId()),
                    ParseUtil.checkNull(purchaseTransactionBean.getEventId()), ParseUtil.checkNull(purchaseTransactionBean.getAdminId()),
                    purchaseTransactionBean.isPurchaseComplete()?"1":"0", ParseUtil.checkNull(purchaseTransactionBean.getRsvpTelNumber()),
                    ParseUtil.checkNull(purchaseTransactionBean.getSeatingTelNumber()),ParseUtil.checkNull(purchaseTransactionBean.getPriceGroupId()),
                    ParseUtil.checkNull(purchaseTransactionBean.getFirstName()),ParseUtil.checkNull(purchaseTransactionBean.getLastName()),
                    ParseUtil.checkNull(purchaseTransactionBean.getState()),ParseUtil.checkNull(purchaseTransactionBean.getZipcode()),
                    ParseUtil.checkNull(purchaseTransactionBean.getStripeCustomerId()), lCreateDate , sHumanCreateDate,
                    ParseUtil.checkNull(purchaseTransactionBean.getUniquePurchaseToken()),
                    ParseUtil.checkNull(purchaseTransactionBean.getApiKeyType()) );
            iNumOfRows = DBDAO.putRowsQuery(sQuery,aParams,ADMIN_DB,"PurchaseTransactionData.java","insertPurchaseTransaction()");
        }
        return iNumOfRows;
    }

    public Integer updatePurchaseTransaction(PurchaseTransactionBean purchaseTransactionBean)
    {
        Integer iNumOfRows = 0;
        if(purchaseTransactionBean!=null)
        {
            String sQuery = "UPDATE GTPURCHASETRANSACTIONS set FK_EVENTID = ? , FK_ADMINID = ? , IS_PURCHASE_COMPLETE = ?," +
                    " RSVP_TELNUMBER = ? , SEATING_TELNUMBER = ? , FK_PRICEGROUPID = ? , FIRSTNAME = ? , LASTNAME = ? , " +
                    " STATE = ? , ZIPCODE = ? , COUNTRY = ? , STRIPE_CUSTOMER_ID = ? , CREATEDATE = ? , HUMANCREATEDATE = ? , STRIPE_TOKEN = ? , " +
                    " CREDIT_CARD_LAST4_DIGITS = ? , UNIQUE_PURCHASE_TOKEN = ?, API_KEY_TYPE = ? WHERE PURCHASETRANSACTIONID = ?";

            Long lCreateDate = DateSupport.getEpochMillis();
            String sHumanCreateDate = DateSupport.getUTCDateTime();

            ArrayList<Object> aParams = DBDAO.createConstraint(
                    ParseUtil.checkNull(purchaseTransactionBean.getEventId()),
                    ParseUtil.checkNull(purchaseTransactionBean.getAdminId()),
                    purchaseTransactionBean.isPurchaseComplete()?"1":"0",
                    ParseUtil.checkNull(purchaseTransactionBean.getRsvpTelNumber()),
                    ParseUtil.checkNull(purchaseTransactionBean.getSeatingTelNumber()),
                    ParseUtil.checkNull(purchaseTransactionBean.getPriceGroupId()),
                    ParseUtil.checkNull(purchaseTransactionBean.getFirstName()),
                    ParseUtil.checkNull(purchaseTransactionBean.getLastName()),
                    ParseUtil.checkNull(purchaseTransactionBean.getState()),
                    ParseUtil.checkNull(purchaseTransactionBean.getZipcode()),
                    ParseUtil.checkNull(purchaseTransactionBean.getCountry()),
                    ParseUtil.checkNull(purchaseTransactionBean.getStripeCustomerId()),
                    lCreateDate ,
                    sHumanCreateDate,
                    ParseUtil.checkNull(purchaseTransactionBean.getStripeToken()),
                    ParseUtil.checkNull(purchaseTransactionBean.getCreditCardLast4Digits()),
                    ParseUtil.checkNull(purchaseTransactionBean.getUniquePurchaseToken()),
                    ParseUtil.checkNull(purchaseTransactionBean.getApiKeyType()),
                    ParseUtil.checkNull(purchaseTransactionBean.getPurchaseTransactionId()) );
            appLogging.info("Calling this forever there is nothing to do here. move along" +
                    " " + aParams.toString() + " - " + purchaseTransactionBean );
            iNumOfRows = DBDAO.putRowsQuery(sQuery,aParams,ADMIN_DB,"PurchaseTransactionData.java","updatePurchaseTransaction()");
        }
        return iNumOfRows;
    }
}
