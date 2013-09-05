package com.gs.bean;

import com.gs.common.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 9/5/13
 * Time: 3:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class PaymentChannelRequest {
    private Constants.API_KEY_TYPE apiKeyType = Constants.API_KEY_TYPE.LIVE_KEY;

    public Constants.API_KEY_TYPE getApiKeyType() {
        return apiKeyType;
    }

    public void setApiKeyType(Constants.API_KEY_TYPE apiKeyType) {
        this.apiKeyType = apiKeyType;
    }
}
