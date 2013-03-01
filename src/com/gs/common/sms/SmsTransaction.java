package com.gs.common.sms;

import com.gs.bean.sms.SmsObject;
import com.gs.bean.sms.SmsTransactionBean;
import com.gs.bean.twilio.IncomingCallBean;
import com.gs.bean.twilio.TwilioIncomingCallBean;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.db.DBDAO;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 2/25/13
 * Time: 8:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class SmsTransaction {

    Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);
    private String ADMIN_DB = applicationConfig.get(Constants.ADMIN_DB);
    private String sourceFile = "SmsTransaction.java";

    /*
    +------------------------+--------------+------+-----+---------+-------+
    | Field                  | Type         | Null | Key | Default | Extra |
    +------------------------+--------------+------+-----+---------+-------+
    | SMSTRANSACTIONID       | varchar(45)  | NO   | PRI | NULL    |       |
    | TELCOM_SERVICE_ACC_SID | varchar(45)  | YES  |     | NULL    |       |
    | TELCOM_SERVICE_SMS_SID | varchar(45)  | YES  |     | NULL    |       |
    | FROM_TELNUMBER         | varchar(45)  | NO   |     | NULL    |       |
    | TO_TELNUMBER           | varchar(45)  | NO   |     | NULL    |       |
    | SMS_MESSAGE            | varchar(160) | YES  |     | NULL    |       |
    | SMS_STATUS             | varchar(45)  | YES  |     | NULL    |       |
    | FK_EVENTID             | varchar(45)  | YES  |     | NULL    |       |
    | FK_ADMINID             | varchar(45)  | YES  |     | NULL    |       |
    | FK_GUESTID             | varchar(45)  | YES  |     | NULL    |       |
    | CREATEDATE             | bigint(20)   | NO   |     | 0       |       |
    | HUMANCREATEDATE        | varchar(45)  | YES  |     | NULL    |       |
    | EVENT_TYPE             | varchar(45)  | YES  |     | NULL    |       |
    +------------------------+--------------+------+-----+---------+-------+
     */
    private static String QUERY_INSERT_TRANSACTION_SMS = "INSERT INTO GTSMSTRANSACTION ( SMSTRANSACTIONID ,TELCOM_SERVICE_ACC_SID  ," +
            " TELCOM_SERVICE_SMS_SID  , FROM_TELNUMBER , TO_TELNUMBER, SMS_MESSAGE , SMS_STATUS , FK_GUESTID , FK_EVENTID  , FK_ADMINID ," +
            " CREATEDATE  , HUMANCREATEDATE  , EVENT_TYPE ) " +
            " VALUES( ?,?,? ,?,?,? ,?,?,? ,?,?,? ,?  )";

    private static SmsTransaction smsTransactionInstance = new SmsTransaction();

    public static SmsTransaction getInstance() {
        if( smsTransactionInstance == null )
        {
            smsTransactionInstance = new SmsTransaction();
        }
        return smsTransactionInstance;
    }

    private SmsTransaction() {
    }

    public Integer  createTransaction(SmsTransactionBean smsTransactionBean)
    {
        Integer iNumOfRows = 0;
        /*
            private static String QUERY_INSERT_TRANSACTION_SMS = "INSERT INTO GTSMSTRANSACTION ( SMSTRANSACTIONID ,TELCOM_SERVICE_ACC_SID  ," +
            " TELCOM_SERVICE_SMS_SID  , FROM_TELNUMBER , TO_TELNUMBER, SMS_MESSAGE , SMS_STATUS , FK_GUESTID , FK_EVENTID  , FK_ADMINID ," +
            " CREATEDATE  , HUMANCREATEDATE  , EVENT_TYPE ) " +
            " VALUES(?,?,? ,?,?,? ,?,?,? ,?,?,? ,? )";
         */
        if (smsTransactionBean != null && !"".equalsIgnoreCase( smsTransactionBean.getSmsTransactionId() )) {
            ArrayList<Object> aParams = DBDAO.createConstraint( smsTransactionBean.getSmsTransactionId(), smsTransactionBean.getTelecomServiceAccSID(),
                    smsTransactionBean.getTelecomServiceSmsSID(), smsTransactionBean.getFromNumber(), smsTransactionBean.getToNumber(),
                    smsTransactionBean.getSmsMessage() , smsTransactionBean.getSmsStatus() , smsTransactionBean.getGuestId(),
                    smsTransactionBean.getEventId() , smsTransactionBean.getAdminId(), smsTransactionBean.getCreateDate(),
                    smsTransactionBean.getHumanCreateDate() , smsTransactionBean.getEventType() );

            iNumOfRows = DBDAO.putRowsQuery( QUERY_INSERT_TRANSACTION_SMS,  aParams, ADMIN_DB, sourceFile, "createTransaction()" );

        }
        return iNumOfRows;
    }
}
