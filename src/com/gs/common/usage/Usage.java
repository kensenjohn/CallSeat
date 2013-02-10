package com.gs.common.usage;

import com.gs.bean.usage.UsageBean;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 2/8/13
 * Time: 5:51 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Usage {

    public enum USAGE_CALL_TYPE
    {
        DEMO("DEMO"),
        PREMIUM("PREMIUM");

        private String usageCallType = "";
        USAGE_CALL_TYPE(String usageCallType)
        {
            this.usageCallType = usageCallType;
        }

        public String getUsageCallType()
        {
            return this.usageCallType;
        }

    }

    public UsageBean getUsage(UsageMetaData usageMetaData);
    public UsageBean getDemoUsage(UsageMetaData usageMetaData);
    public UsageBean getPremiumUsage(UsageMetaData usageMetaData);
}
