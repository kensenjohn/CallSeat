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
public class PhoneCallUsageBean extends UsageBean {

    private static final Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);

    private Integer numOfDemoMinutesUsed = 0;
    private Integer numOfDemoMinutesRemaining = 0;
    private Integer numOfDemoMinutesAllocated = 0;
    private Integer numOfPremiumMinutesUsed = 0;
    private Integer numOfPremiumMinutesRemaining = 0;
    private Integer numOfPremiumMinutesAllocated = 0;

    public Integer getNumOfDemoMinutesUsed() {
        return numOfDemoMinutesUsed;
    }

    public void setNumOfDemoMinutesUsed(Integer numOfDemoMinutesUsed) {
        this.numOfDemoMinutesUsed = numOfDemoMinutesUsed;
    }

    public Integer getNumOfDemoMinutesRemaining() {
        return numOfDemoMinutesRemaining;
    }

    public void setNumOfDemoMinutesRemaining(Integer numOfDemoMinutesRemaining) {
        this.numOfDemoMinutesRemaining = numOfDemoMinutesRemaining;
    }

    public Integer getNumOfPremiumMinutesUsed() {
        return numOfPremiumMinutesUsed;
    }

    public void setNumOfPremiumMinutesUsed(Integer numOfPremiumMinutesUsed) {
        this.numOfPremiumMinutesUsed = numOfPremiumMinutesUsed;
    }

    public Integer getNumOfPremiumMinutesRemaining() {
        return numOfPremiumMinutesRemaining;
    }

    public void setNumOfPremiumMinutesRemaining(Integer numOfPremiumMinutesRemaining) {
        this.numOfPremiumMinutesRemaining = numOfPremiumMinutesRemaining;
    }

    public Integer getNumOfDemoMinutesAllocated() {
        return numOfDemoMinutesAllocated;
    }

    public void setNumOfDemoMinutesAllocated(Integer numOfDemoMinutesAllocated) {
        this.numOfDemoMinutesAllocated = numOfDemoMinutesAllocated;
    }

    public Integer getNumOfPremiumMinutesAllocated() {
        return numOfPremiumMinutesAllocated;
    }

    public void setNumOfPremiumMinutesAllocated(Integer numOfPremiumMinutesAllocated) {
        this.numOfPremiumMinutesAllocated = numOfPremiumMinutesAllocated;
    }



    public JSONObject toJson() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("telnum_event_id", this.eventId);
            jsonObject.put("telnum_demo_mins_allocated", this.numOfDemoMinutesAllocated);
            jsonObject.put("telnum_demo_mins_used", this.numOfDemoMinutesUsed);
            jsonObject.put("telnum_demo_mins_remain", this.numOfDemoMinutesRemaining);

            jsonObject.put("telnum_premium_mins_allocated", this.numOfPremiumMinutesAllocated);
            jsonObject.put("telnum_premium_mins_used", this.numOfPremiumMinutesUsed);
            jsonObject.put("telnum_premium_mins_remain", this.numOfPremiumMinutesRemaining);

        } catch (JSONException e) {
            appLogging.error("Exception while parsing Json : " + e.getMessage() + " - " + ExceptionHandler.getStackTrace(e));
        }
        return jsonObject;
    }

    @Override
    public String toString() {
        return "PhoneCallUsageBean{" +
                "numOfDemoMinutesUsed=" + numOfDemoMinutesUsed +
                ", numOfDemoMinutesRemaining=" + numOfDemoMinutesRemaining +
                ", numOfDemoMinutesAllocated=" + numOfDemoMinutesAllocated +
                ", numOfPremiumMinutesUsed=" + numOfPremiumMinutesUsed +
                ", numOfPremiumMinutesRemaining=" + numOfPremiumMinutesRemaining +
                ", numOfPremiumMinutesAllocated=" + numOfPremiumMinutesAllocated +
                '}';
    }
}
