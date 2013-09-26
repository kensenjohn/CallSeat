package com.gs.common.mail;

import java.util.ArrayList;
import java.util.HashMap;

import com.gs.common.ParseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.email.EmailQueueBean;
import com.gs.bean.email.EmailTemplateBean;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.DateSupport;
import com.gs.common.db.DBDAO;

public class MailingServiceData {
	private static String queryAllEmailsUnSent = "SELECT * FROM GTEMAILQUEUE WHERE STATUS = ?";
	private static String queryUpdateEmailStatus = "UPDATE GTEMAILQUEUE SET STATUS = ?, "
			+ " MODIFIEDDATE = ?, HUMANMODIFYDATE = ?  WHERE EMAILQUEUEID = ?";
	private static String queryInsertEmailQueue = "insert into GTEMAILQUEUE (EMAILQUEUEID, "
			+ " FROM_ADDRESS ,FROM_ADDRESS_NAME ,TO_ADDRESS,TO_ADDRESS_NAME,CC_ADDRESS,CC_ADDRESSNAME,BCC_ADDRESS,BCC_ADDRESSNAME, EMAIL_SUBJECT, HTML_BODY, TEXT_BODY, STATUS, CREATEDATE,"
			+ " MODIFIEDDATE , HUMANCREATEDATE, HUMANMODIFYDATE ) "
			+ " VALUES (?,?,?, ?,?,?, ?,?,?, ?,?,?, ?,?,?, ?,? )";
	private static String queryEmailsTemplate = "SELECT * FROM GTEMAILTEMPLATE WHERE EMAILTEMPLATENAME = ?";
    private static String queryEmailsTemplateById = "SELECT * FROM GTEMAILTEMPLATE WHERE EMAILTEMPLATEID = ?";

	Configuration applicationConfig = Configuration
			.getInstance(Constants.APPLICATION_PROP);
	private String ADMIN_DB = applicationConfig.get(Constants.ADMIN_DB);

	private static final Logger appLogging = LoggerFactory
			.getLogger(Constants.APP_LOGS);
	private static final Logger emailLogging = LoggerFactory
			.getLogger(Constants.EMAILER_LOGS);

	public EmailTemplateBean getEmailTemplate(
			Constants.EMAIL_TEMPLATE emailTemplate) {
		EmailTemplateBean emailTemplateBean = new EmailTemplateBean();
		if (emailTemplate != null) {
			ArrayList<Object> aParams = DBDAO.createConstraint(emailTemplate
					.getEmailTemplate());

			ArrayList<HashMap<String, String>> arrHmEmailTemplate = DBDAO
					.getDBData(ADMIN_DB, queryEmailsTemplate, aParams, false,
							"MailingServiceData.java", "getEmailTemplate()");
			if (arrHmEmailTemplate != null && !arrHmEmailTemplate.isEmpty()) {
				for (HashMap<String, String> hmEmailTemplate : arrHmEmailTemplate) {
					emailTemplateBean = new EmailTemplateBean(hmEmailTemplate);
				}
			}
		}
		return emailTemplateBean;
	}

    public EmailTemplateBean getEmailTemplateById( String sTemplateId  )
    {
        EmailTemplateBean emailTemplateBean = new EmailTemplateBean();
        if(sTemplateId!=null && !"".equalsIgnoreCase(sTemplateId))
        {
            ArrayList<Object> aParams = DBDAO.createConstraint( sTemplateId );
            ArrayList<HashMap<String, String>> arrHmEmailTemplate = DBDAO.getDBData(
                    ADMIN_DB, queryEmailsTemplateById, aParams, false,"MailingServiceData.java", "getEmailTemplateById()");
            if (arrHmEmailTemplate != null && !arrHmEmailTemplate.isEmpty()) {
                for (HashMap<String, String> hmEmailTemplate : arrHmEmailTemplate) {
                    emailTemplateBean = new EmailTemplateBean(hmEmailTemplate);
                }
            }

        }
        return emailTemplateBean;
    }


	public ArrayList<EmailQueueBean> getEmailsFromQueue(
			Constants.EMAIL_STATUS emailStatus) {
		ArrayList<EmailQueueBean> arrEmailQueueBean = new ArrayList<EmailQueueBean>();
		if (emailStatus != null) {
			ArrayList<Object> aParams = DBDAO.createConstraint(emailStatus
					.getStatus());

			ArrayList<HashMap<String, String>> arrHmEmailQueue = DBDAO
					.getDBData(ADMIN_DB, queryAllEmailsUnSent, aParams, false,
							"MailingServiceData.java", "getEmailsFromQueue()");

			if (arrHmEmailQueue != null && !arrHmEmailQueue.isEmpty()) {
				for (HashMap<String, String> hmEmailQueue : arrHmEmailQueue) {
					EmailQueueBean emailQueueBean = new EmailQueueBean(
							hmEmailQueue);
					arrEmailQueueBean.add(emailQueueBean);

				}
			}
		}
		return arrEmailQueueBean;

	}

	public Integer updateEmailStatus(EmailQueueBean emailQueueBean,
			Constants.EMAIL_STATUS newEmailStatus) {
		Integer iNumOfRows = 0;
		if (emailQueueBean != null && newEmailStatus != null
				&& emailQueueBean.getEmailQueueId() != null
				&& !"".equalsIgnoreCase(emailQueueBean.getEmailQueueId())) {

			Long lCurrentDate = DateSupport.getEpochMillis();
			String sHumanCurrentDate = DateSupport.getUTCDateTime();

			ArrayList<Object> aParams = DBDAO.createConstraint(
					newEmailStatus.getStatus(), lCurrentDate,
					sHumanCurrentDate, emailQueueBean.getEmailQueueId());

			iNumOfRows = DBDAO.putRowsQuery(queryUpdateEmailStatus, aParams,
					ADMIN_DB, "MailingServiceData.java", "updateEmailStatus()");
		} else {
			emailLogging.debug("Email Queue bean is null of empty "
					+ emailQueueBean);
		}

		return iNumOfRows;
	}

	public Integer insertEmailQueue(EmailQueueBean emailQueueBean) {
		Integer iNumOfRows = 0;
		if (emailQueueBean != null && emailQueueBean.getEmailQueueId() != null
				&& !"".equalsIgnoreCase(emailQueueBean.getEmailQueueId())) {

			Long lCurrentDate = DateSupport.getEpochMillis();
			String sHumanCurrentDate = DateSupport.getUTCDateTime();

			ArrayList<Object> aParams = DBDAO.createConstraint(
					emailQueueBean.getEmailQueueId(),
					emailQueueBean.getFromAddress(),
					emailQueueBean.getFromAddressName(),
					emailQueueBean.getToAddress(),
					emailQueueBean.getToAddressName(),
                    emailQueueBean.getCcAddress(),
                    emailQueueBean.getCcAddressName(),
                    emailQueueBean.getBccAddress(),
                    emailQueueBean.getBccAddressName(),
					emailQueueBean.getEmailSubject(),
					emailQueueBean.getHtmlBody(), emailQueueBean.getTextBody(),
					emailQueueBean.getStatus(), lCurrentDate, lCurrentDate,
					sHumanCurrentDate, sHumanCurrentDate);

			iNumOfRows = DBDAO.putRowsQuery(queryInsertEmailQueue, aParams,
					ADMIN_DB, "MailingServiceData.java", "insertEmailQueue()");
            appLogging.error("aParams : "
                    + aParams + " sQuery : " + queryInsertEmailQueue );
		} else {
			appLogging.error("EmailQueueBean is null or has no ID : "
					+ ParseUtil.checkNullObject(emailQueueBean));
			emailLogging.error("EmailQueueBean is null or has no ID : "
					+  ParseUtil.checkNullObject(emailQueueBean));
		}
		return iNumOfRows;
	}
}
