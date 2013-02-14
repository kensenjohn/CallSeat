package com.gs.common.sms;

import com.gs.bean.email.EmailTemplateBean;
import com.gs.bean.sms.SmsTemplateBean;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.db.DBDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 2/14/13
 * Time: 2:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class SmsServiceData {
    private static String querySmsTemplate = "SELECT * FROM GTSMSTEMPLATE WHERE SMSTEMPLATENAME = ?";

    Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);
    private String ADMIN_DB = applicationConfig.get(Constants.ADMIN_DB);

    private static final Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);
    private String sourceFile = "SmsServiceData.java";

    public SmsTemplateBean getSmsTemplate( Constants.SMS_TEMPLATE smsTemplate ) {
        SmsTemplateBean smsTemplateBean = new SmsTemplateBean();
        if (smsTemplate != null)
        {
            ArrayList<Object> aParams = DBDAO.createConstraint(smsTemplate.getSmsTemplate());

            ArrayList<HashMap<String, String>> arrHmEmailTemplate = DBDAO.getDBData(ADMIN_DB, querySmsTemplate, aParams, false, sourceFile , "getSmsTemplate()");
            if (arrHmEmailTemplate != null && !arrHmEmailTemplate.isEmpty()) {
                for (HashMap<String, String> hmEmailTemplate : arrHmEmailTemplate) {
                    smsTemplateBean = new SmsTemplateBean(hmEmailTemplate);
                }
            }
        }
        return smsTemplateBean;
    }
}
