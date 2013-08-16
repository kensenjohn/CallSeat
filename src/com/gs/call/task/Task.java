package com.gs.call.task;

import com.gs.bean.twilio.IncomingCallBean;
import com.gs.bean.usage.PhoneCallUsageBean;
import com.gs.call.CallResponse;
import com.gs.common.Constants;
import com.gs.common.usage.PhoneCallUsage;
import com.gs.common.usage.Usage;
import com.gs.common.usage.UsageMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Task
{
	protected String eventId = "";
	protected String adminId = "";

    protected Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);

	public Task(String eventId, String adminId)
	{
		this.eventId = eventId;
		this.adminId = adminId;
	}

	public abstract CallResponse processTask(IncomingCallBean incomingCallBean);

    public boolean canCallUsageFeatureContinue( CallResponse callResponse )  {
        boolean canCallFeatureUsageContinue = true;
        if(callResponse!=null)  {
            UsageMetaData usageMetaData = new UsageMetaData();
            usageMetaData.setEventId(this.eventId);
            usageMetaData.setAdminId(this.adminId);

            // Phone Call Usage Summary
            Usage phoneCallUsage = new PhoneCallUsage();
            PhoneCallUsageBean phoneCallUsageBean = (PhoneCallUsageBean)phoneCallUsage.getUsage(usageMetaData);
            appLogging.info("phoneCallUsageBean : " + phoneCallUsageBean );

            int iNumOfPremiumMinsRemaining = phoneCallUsageBean.getNumOfPremiumMinutesRemaining();
            int iNumOfDemoMinsRemaining = phoneCallUsageBean.getNumOfDemoMinutesRemaining();

            appLogging.info("iNumOfPremiumMinsRemaining : " + iNumOfPremiumMinsRemaining + "  iNumOfDemoMinsRemaining : " + iNumOfDemoMinsRemaining + " = " + (iNumOfPremiumMinsRemaining+iNumOfDemoMinsRemaining) );
            if( (iNumOfPremiumMinsRemaining+iNumOfDemoMinsRemaining) <= 0 ) {

                canCallFeatureUsageContinue = false;
            }
            appLogging.info(" canCallFeatureUsageContinue = " + canCallFeatureUsageContinue );
        }
        return canCallFeatureUsageContinue;
    }
}
