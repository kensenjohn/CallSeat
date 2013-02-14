package com.gs.bean.sms;

import com.gs.common.ParseUtil;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 2/14/13
 * Time: 2:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class SmsTemplateBean {
    /*SMSTEMPLATEID,SMSTEMPLATENAME, SMS_BODY*/
    private String smsTemplateId = "";
    private String smsTemplateName = "";
    private String smsBody = "";

    public SmsTemplateBean()
    {

    }

    public SmsTemplateBean(HashMap<String,String> hmResult )
    {
        this.smsTemplateId = ParseUtil.checkNull( hmResult.get("SMSTEMPLATEID") );
        this.smsTemplateName = ParseUtil.checkNull( hmResult.get("SMSTEMPLATENAME") );
        this.smsBody = ParseUtil.checkNull( hmResult.get("SMS_BODY") );
    }

    public String getSmsTemplateId() {
        return smsTemplateId;
    }

    public void setSmsTemplateId(String smsTemplateId) {
        this.smsTemplateId = smsTemplateId;
    }

    public String getSmsTemplateName() {
        return smsTemplateName;
    }

    public void setSmsTemplateName(String smsTemplateName) {
        this.smsTemplateName = smsTemplateName;
    }

    public String getSmsBody() {
        return smsBody;
    }

    public void setSmsBody(String smsBody) {
        this.smsBody = smsBody;
    }

    @Override
    public String toString() {
        return "SmsTemplateBean{" +
                "smsTemplateId='" + smsTemplateId + '\'' +
                ", smsTemplateName='" + smsTemplateName + '\'' +
                ", smsBody='" + smsBody + '\'' +
                '}';
    }
}
