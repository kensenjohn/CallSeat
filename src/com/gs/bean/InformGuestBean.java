package com.gs.bean;

import com.gs.common.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 2/13/13
 * Time: 10:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class InformGuestBean {
    private String eventId = "";
    private String adminId = "";
    private String guestId = "";
    private Constants.EVENT_TASK eventTask;


    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public Constants.EVENT_TASK getEventTask() {
        return eventTask;
    }

    public void setEventTask(Constants.EVENT_TASK eventTask) {
        this.eventTask = eventTask;
    }

    @Override
    public String toString() {
        return "InformGuestBean{" +
                "eventId='" + eventId + '\'' +
                ", adminId='" + adminId + '\'' +
                ", guestId='" + guestId + '\'' +
                ", eventTask=" + eventTask!=null?eventTask.getTask():"TASK Unknown" +
                '}';
    }
}
