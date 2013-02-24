package com.gs.common.sms;

import com.gs.bean.sms.SmsQueueBean;
import com.gs.bean.sms.SmsScheduleBean;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 2/22/13
 * Time: 1:18 AM
 * To change this template use File | Settings | File Templates.
 */
public interface SmsSender {

    public Integer send( SmsQueueBean smsQueueBean );
    public void update( SmsQueueBean smsQueueBean );
}
