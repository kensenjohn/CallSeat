package com.gs.bean.sms;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 2/16/13
 * Time: 1:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class SmsObject {
    protected String fromPhoneNumber = "";
    protected String toPhoneNumber = "";
    protected String message = "";
    protected boolean isSmsObjectExist = false;

    public String getFromPhoneNumber() {
        return fromPhoneNumber;
    }

    public void setFromPhoneNumber(String fromPhoneNumber) {
        this.fromPhoneNumber = fromPhoneNumber;
    }

    public String getToPhoneNumber() {
        return toPhoneNumber;
    }

    public void setToPhoneNumber(String toPhoneNumber) {
        this.toPhoneNumber = toPhoneNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSmsObjectExist() {
        return isSmsObjectExist;
    }

    public void setSmsObjectExist(boolean smsObjectExist) {
        isSmsObjectExist = smsObjectExist;
    }

    @Override
    public String toString() {
        return "SmsObject{" +
                "fromPhoneNumber='" + fromPhoneNumber + '\'' +
                ", toPhoneNumber='" + toPhoneNumber + '\'' +
                ", message='" + message + '\'' +
                ", isSmsObjectExist=" + isSmsObjectExist +
                '}';
    }
}
