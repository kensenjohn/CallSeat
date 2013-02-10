package com.gs.bean.usage;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 2/8/13
 * Time: 5:56 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class UsageBean {
    protected String eventId = "";

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
