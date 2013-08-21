package com.gs.bean.email;

import com.gs.common.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 8/18/13
 * Time: 11:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmailSchedulerRequest {

    private String guestId = Constants.EMPTY;
    private String eventId = Constants.EMPTY;
    private String adminId = Constants.EMPTY;
    private Constants.EMAIL_TEMPLATE emailTemplate;
    private Long scheduleTime = 0L;
    private String humanScheduleTime = Constants.EMPTY;
    private boolean updateScheduleIfExists = false;

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

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

    public Constants.EMAIL_TEMPLATE getEmailTemplate() {
        return emailTemplate;
    }

    public void setEmailTemplate(Constants.EMAIL_TEMPLATE emailTemplate) {
        this.emailTemplate = emailTemplate;
    }

    public Long getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(Long scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public String getHumanScheduleTime() {
        return humanScheduleTime;
    }

    public void setHumanScheduleTime(String humanScheduleTime) {
        this.humanScheduleTime = humanScheduleTime;
    }

    public boolean isUpdateScheduleIfExists() {
        return updateScheduleIfExists;
    }

    public void setUpdateScheduleIfExists(boolean updateScheduleIfExists) {
        this.updateScheduleIfExists = updateScheduleIfExists;
    }
}
