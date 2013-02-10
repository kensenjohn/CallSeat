package com.gs.common.usage;

import com.gs.bean.usage.TextMessageUsageBean;
import com.gs.bean.usage.UsageBean;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 2/8/13
 * Time: 5:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class TextMessageUsage implements Usage {
    @Override
    public TextMessageUsageBean getUsage(UsageMetaData usageMetaData) {
        TextMessageUsageBean textMessageUsageBean = new TextMessageUsageBean();
        textMessageUsageBean.setEventId("2_TEXT_EVent");
        return textMessageUsageBean;
    }

    @Override
    public UsageBean getDemoUsage(UsageMetaData usageMetaData) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public UsageBean getPremiumUsage(UsageMetaData usageMetaData) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
