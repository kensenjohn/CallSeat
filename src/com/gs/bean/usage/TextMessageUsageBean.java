package com.gs.bean.usage;

import com.gs.common.Constants;
import com.gs.common.exception.ExceptionHandler;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 2/8/13
 * Time: 6:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class TextMessageUsageBean extends UsageBean {

    private static final Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);

    private Integer numOfDemoTextSent = 0;
    private Integer numOfDemoTextRemaining = 0;
    private Integer numOfDemoTextAllocated = 0;
    private Integer numOfPremiumTextSent = 0;
    private Integer numOfPremiumTextRemaining = 0;
    private Integer numOfPremiumTextAllocated = 0;

    public Integer getNumOfDemoTextSent() {
        return numOfDemoTextSent;
    }

    public void setNumOfDemoTextSent(Integer numOfDemoTextSent) {
        this.numOfDemoTextSent = numOfDemoTextSent;
    }

    public Integer getNumOfDemoTextRemaining() {
        return numOfDemoTextRemaining;
    }

    public void setNumOfDemoTextRemaining(Integer numOfDemoTextRemaining) {
        this.numOfDemoTextRemaining = numOfDemoTextRemaining;
    }

    public Integer getNumOfDemoTextAllocated() {
        return numOfDemoTextAllocated;
    }

    public void setNumOfDemoTextAllocated(Integer numOfDemoTextAllocated) {
        this.numOfDemoTextAllocated = numOfDemoTextAllocated;
    }

    public Integer getNumOfPremiumTextSent() {
        return numOfPremiumTextSent;
    }

    public void setNumOfPremiumTextSent(Integer numOfPremiumTextSent) {
        this.numOfPremiumTextSent = numOfPremiumTextSent;
    }

    public Integer getNumOfPremiumTextRemaining() {
        return numOfPremiumTextRemaining;
    }

    public void setNumOfPremiumTextRemaining(Integer numOfPremiumTextRemaining) {
        this.numOfPremiumTextRemaining = numOfPremiumTextRemaining;
    }

    public Integer getNumOfPremiumTextAllocated() {
        return numOfPremiumTextAllocated;
    }

    public void setNumOfPremiumTextAllocated(Integer numOfPremiumTextAllocated) {
        this.numOfPremiumTextAllocated = numOfPremiumTextAllocated;
    }

    public JSONObject toJson() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("telnum_event_id", this.eventId);
            jsonObject.put("telnum_demo_text_mssg_sent", this.numOfDemoTextSent);
            jsonObject.put("telnum_demo_text_mssg_remaining", this.numOfDemoTextRemaining);
            jsonObject.put("telnum_demo_text_mssg_allocated", this.numOfDemoTextAllocated);

            jsonObject.put("telnum_premium_text_mssg_sent", this.numOfPremiumTextSent);
            jsonObject.put("telnum_premium_text_mssg_remaining", this.numOfPremiumTextRemaining);
            jsonObject.put("telnum_premium_text_mssg_allocated", this.numOfPremiumTextAllocated);

        } catch (JSONException e) {
            appLogging.error("Exception while parsing Json : " + e.getMessage() + " - " + ExceptionHandler.getStackTrace(e));
        }
        return jsonObject;
    }

    @Override
    public String toString() {
        return "TextMessageUsageBean{" +
                "numOfDemoTextSent=" + numOfDemoTextSent +
                ", numOfDemoTextRemaining=" + numOfDemoTextRemaining +
                ", numOfDemoTextAllocated=" + numOfDemoTextAllocated +
                ", numOfPremiumTextSent=" + numOfPremiumTextSent +
                ", numOfPremiumTextRemaining=" + numOfPremiumTextRemaining +
                ", numOfPremiumTextAllocated=" + numOfPremiumTextAllocated +
                '}';
    }
}
