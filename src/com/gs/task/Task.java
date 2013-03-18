package com.gs.task;

import com.gs.bean.twilio.IncomingCallBean;
import com.gs.bean.usage.PhoneCallUsageBean;
import com.gs.call.CallResponse;
import com.gs.common.usage.PhoneCallUsage;
import com.gs.common.usage.Usage;
import com.gs.common.usage.UsageMetaData;

public abstract class Task
{
	protected String eventId = "";
	protected String adminId = "";

	public Task(String eventId, String adminId)
	{
		this.eventId = eventId;
		this.adminId = adminId;
	}

	public abstract CallResponse processTask(IncomingCallBean incomingCallBean);

    public boolean isCallUsageLimitReached( CallResponse callResponse )
    {
        boolean isUsageLimitReached = true;
        if(callResponse!=null)
        {
            UsageMetaData usageMetaData = new UsageMetaData();
            usageMetaData.setEventId(this.eventId);
            usageMetaData.setAdminId(this.adminId);

            // Phone Call Usage Summary
            Usage phoneCallUsage = new PhoneCallUsage();
            PhoneCallUsageBean phoneCallUsageBean = (PhoneCallUsageBean)phoneCallUsage.getUsage(usageMetaData);


            int iNumOfPremiumMinsRemaining = phoneCallUsageBean.getNumOfPremiumMinutesRemaining();
            int iNumOfDemoMinsRemaining = phoneCallUsageBean.getNumOfDemoMinutesRemaining();

            if( (iNumOfPremiumMinsRemaining+iNumOfDemoMinsRemaining) <= 0 )
            {
                isUsageLimitReached = false;
            }
        }
        return isUsageLimitReached;
    }
}
