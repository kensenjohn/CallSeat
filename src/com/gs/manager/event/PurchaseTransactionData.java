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
            String sQuery = "select PURCHASETRANSACTIONID, FK_EVENTID,  FK_ADMINID,  IS_PURCHASE_COMPLETE , RSVP_TELNUMBER, " +
                    " SEATING_TELNUMBER, FK_SUBSCRIPTIONID, FIRSTNAME, LASTNAME, STATE, ZIPCODE, STRIPE_CUSTOMER_ID "
                    + " from GTPURCHASETRANSACTIONS where FK_ADMINID = ? and FK_EVENTID = ?";

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
            String sQuery = "select PURCHASETRANSACTIONID, FK_EVENTID,  FK_ADMINID,  IS_PURCHASE_COMPLETE , RSVP_TELNUMBER, " +
                    " SEATING_TELNUMBER, FK_SUBSCRIPTIONID, FIRSTNAME, LASTNAME, STATE, CITY, ZIPCODE, STRIPE_CUSTOMER_ID "
                    + " from GTPURCHASETRANSACTIONS where PURCHASETRANSACTIONID = ?";

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
                    " IS_PURCHASE_COMPLETE,RSVP_TELNUMBER,SEATING_TELNUMBER, FK_SUBSCRIPTIONID,FIRSTNAME,LASTNAME, STATE,ZIPCODE,STRIPE_CUSTOMER_ID," +
                    " CREATEDATE,HUMANCREATEDATE ) "
                    + " VALUES(?,?,?,   ?,?,?,	 ?,?,?,  ?,?,?,  ?,?)";

            Long lCreateDate = DateSupport.getEpochMillis();
            String sHumanCreateDate = DateSupport.getUTCDateTime();

            ArrayList<Object> aParams = DBDAO.createConstraint(ParseUtil.checkNull(purchaseTransactionBean.getPurchaseTransactionId()),
                    ParseUtil.checkNull(purchaseTransactionBean.getEventId()), ParseUtil.checkNull(purchaseTransactionBean.getAdminId()),
                    purchaseTransactionBean.isPurchaseComplete()?"1":"0", ParseUtil.checkNull(purchaseTransactionBean.getRsvpTelNumber()),
                    ParseUtil.checkNull(purchaseTransactionBean.getSeatingTelNumber()),ParseUtil.checkNull(purchaseTransactionBean.getSubscriptionId()),
                    ParseUtil.checkNull(purchaseTransactionBean.getFirstName()),ParseUtil.checkNull(purchaseTransactionBean.getLastName()),
                    ParseUtil.checkNull(purchaseTransactionBean.getState()),ParseUtil.checkNull(purchaseTransactionBean.getZipcode()),
                    ParseUtil.checkNull(purchaseTransactionBean.getStripeCustomerId()), lCreateDate , sHumanCreateDate );
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
                    " RSVP_TELNUMBER = ? , SEATING_TELNUMBER = ? , FK_SUBSCRIPTIONID = ? , FIRSTNAME = ? , LASTNAME = ? , " +
                    " STATE = ? , ZIPCODE = ? , STRIPE_CUSTOMER_ID = ? , CREATEDATE = ? , HUMANCREATEDATE = ? WHERE PURCHASETRANSACTIONID = ?";

            Long lCreateDate = DateSupport.getEpochMillis();
            String sHumanCreateDate = DateSupport.getUTCDateTime();

            ArrayList<Object> aParams = DBDAO.createConstraint(
                    ParseUtil.checkNull(purchaseTransactionBean.getEventId()), ParseUtil.checkNull(purchaseTransactionBean.getAdminId()),
                    purchaseTransactionBean.isPurchaseComplete()?"1":"0", ParseUtil.checkNull(purchaseTransactionBean.getRsvpTelNumber()),
                    ParseUtil.checkNull(purchaseTransactionBean.getSeatingTelNumber()),ParseUtil.checkNull(purchaseTransactionBean.getSubscriptionId()),
                    ParseUtil.checkNull(purchaseTransactionBean.getFirstName()),ParseUtil.checkNull(purchaseTransactionBean.getLastName()),
                    ParseUtil.checkNull(purchaseTransactionBean.getState()),ParseUtil.checkNull(purchaseTransactionBean.getZipcode()),
                    ParseUtil.checkNull(purchaseTransactionBean.getStripeCustomerId()), lCreateDate , sHumanCreateDate,
                    ParseUtil.checkNull(purchaseTransactionBean.getPurchaseTransactionId()) );
            iNumOfRows = DBDAO.putRowsQuery(sQuery,aParams,ADMIN_DB,"PurchaseTransactionData.java","updatePurchaseTransaction()");
        }
        return iNumOfRows;
    }
}
