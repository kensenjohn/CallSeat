package com.gs.common.mail;

import com.gs.bean.*;
import com.gs.bean.email.EmailObject;
import com.gs.bean.email.EmailQueueBean;
import com.gs.bean.email.EmailScheduleBean;
import com.gs.bean.email.EmailTemplateBean;
import com.gs.bean.sms.SmsObject;
import com.gs.bean.sms.SmsScheduleBean;
import com.gs.bean.sms.SmsTemplateBean;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.DateSupport;
import com.gs.common.ParseUtil;
import com.gs.common.sms.SmsSchedulerData;
import com.gs.common.sms.SmsServiceData;
import com.gs.manager.GuestManager;
import com.gs.manager.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 2/26/13
 * Time: 11:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmailCreatorService {

    EmailCreator emailCreator;

    private static final Logger emailLogging = LoggerFactory.getLogger(Constants.EMAILER_LOGS);
    private Configuration emailConfig = Configuration.getInstance(Constants.EMAILER_PROP);

    public EmailCreatorService()
    {

    }

    public EmailCreatorService(  MailCreator emailCreator )
    {
        this.emailCreator = (EmailCreator) emailCreator;
    }

    public void invokeEmailCreator() {
        if(emailCreator!=null)
        {
            emailLogging.debug("Start execution of emailer creator.");
            Long lCurrentTime =  DateSupport.getEpochMillis();

            ArrayList<EmailScheduleBean> arrOldSchedulerBean = getOldEmailSchedules(lCurrentTime);
            if(arrOldSchedulerBean!=null && !arrOldSchedulerBean.isEmpty())
            {
                emailLogging.debug( " Old Email Scheudler Beans " + arrOldSchedulerBean);
                for(EmailScheduleBean emailOldScheduleBean : arrOldSchedulerBean )
                {
                    emailOldScheduleBean.setScheduleStatus( Constants.SCHEDULER_STATUS.ERROR.getSchedulerStatus()  );
                    this.emailCreator.update( emailOldScheduleBean );
                }
            }

            ArrayList<EmailScheduleBean> arrNewSchedulerBean = getNewEmailSchedules(lCurrentTime);
            if(arrNewSchedulerBean!=null && !arrNewSchedulerBean.isEmpty())
            {
                emailLogging.debug( " New Email Scheduler Beans " + arrNewSchedulerBean);
                for(EmailScheduleBean emailNewScheduleBean : arrNewSchedulerBean )
                {
                    EmailObject emailObject = createEmailObject(emailNewScheduleBean);
                    this.emailCreator.create( emailObject , emailNewScheduleBean );
                }
            }
            emailLogging.debug("End execution of Email creator.");

        }
    }

    public ArrayList<EmailScheduleBean> getOldEmailSchedules(Long lCurrentTime )
    {

        EmailSchedulerData emailSchedulerData = new EmailSchedulerData();
        Long lScheduleTime = DateSupport.subtractTime( lCurrentTime , ParseUtil.sToI(emailConfig.get(Constants.PROP_EMAIL_SCHEDULE_PICKUPTIME_PADDING)), Constants.TIME_UNIT.MINUTES );
        ArrayList<EmailScheduleBean> arrSchedulerBean = emailSchedulerData.getArrSchedule(lScheduleTime, lCurrentTime,  Constants.SCHEDULER_STATUS.NEW_SCHEDULE,Constants.SCHEDULE_PICKUP_TYPE.OLD_RECORDS);

        return arrSchedulerBean;
    }

    public ArrayList<EmailScheduleBean> getNewEmailSchedules( Long lCurrentTime )
    {
        EmailSchedulerData emailSchedulerData = new EmailSchedulerData();
        Long lScheduleTime = DateSupport.subtractTime( lCurrentTime , ParseUtil.sToI( emailConfig.get(Constants.PROP_EMAIL_SCHEDULE_PICKUPTIME_PADDING) ) , Constants.TIME_UNIT.MINUTES );
        ArrayList<EmailScheduleBean> arrSchedulerBean = emailSchedulerData.getArrSchedule(lScheduleTime, lCurrentTime,  Constants.SCHEDULER_STATUS.NEW_SCHEDULE,Constants.SCHEDULE_PICKUP_TYPE.NEW_RECORDS);

        return arrSchedulerBean;
    }

    public EmailObject createEmailObject( EmailScheduleBean emailNewScheduleBean )
    {
        EmailObject emailObject = new EmailQueueBean();
        if(emailNewScheduleBean != null )
        {
            String sGuestId = ParseUtil.checkNull( emailNewScheduleBean.getGuestId() );

            GuestManager guestManager = new GuestManager();
            GuestBean guestBean = guestManager.getGuest(sGuestId);

            if(guestBean!=null && !"".equalsIgnoreCase(guestBean.getGuestId()))
            {
                // Getting template data
                String sTemplateId = ParseUtil.checkNull( emailNewScheduleBean.getEmailTemplateId() ) ;

                if( sTemplateId!= null && !"".equalsIgnoreCase(sTemplateId))
                {
                    MailingServiceData emailServiceData = new MailingServiceData();
                    EmailTemplateBean emailTemplateBean = emailServiceData.getEmailTemplateById(sTemplateId);

                    emailLogging.debug( "Email Template Bean :  " + emailTemplateBean );
                    if( emailTemplateBean!=null )
                    {
                        if( Constants.EMAIL_TEMPLATE.RSVP_CONFIRMATION_EMAIL.getEmailTemplate().equalsIgnoreCase( emailTemplateBean.getTemplateName() ))
                        {
                            emailObject = getRSVPEmailObject(emailNewScheduleBean, emailTemplateBean);
                        }
                        else if( Constants.EMAIL_TEMPLATE.SEATING_CONFIRMATION_EMAIL.getEmailTemplate().equalsIgnoreCase( emailTemplateBean.getTemplateName() ))
                        {
                            emailObject = getSeatingConfirmationEmailObject( emailNewScheduleBean , emailTemplateBean );
                        }
                    }

                    emailLogging.debug( "Guest bean from new schedule  " + guestBean );
                    emailObject.setToAddress( ParseUtil.checkNull( guestBean.getUserInfoBean().getEmail() ));   // set TO Email
                    emailObject.setToAddressName( ParseUtil.checkNull( guestBean.getUserInfoBean().getEmail() ) );

                    emailObject.setFromAddress( ParseUtil.checkNull( emailTemplateBean.getFromAddress() ) );
                    emailObject.setFromAddressName( ParseUtil.checkNull( emailTemplateBean.getFromAddressName() ) );
                    emailObject.setStatus( Constants.EMAIL_STATUS.NEW.getStatus() );
                    emailObject.setEmailSubject( ParseUtil.checkNull(emailTemplateBean.getEmailSubject() ) );
                    // Getting telephonyData data
                   /* String sEventId = ParseUtil.checkNull( emailNewScheduleBean.getEventId() );
                    String sAdminId = ParseUtil.checkNull( emailNewScheduleBean.getAdminId() );

                    TelNumberMetaData telNumberMetaData = new TelNumberMetaData();
                    telNumberMetaData.setEventId(sEventId);
                    telNumberMetaData.setAdminId(sAdminId);
                    TelNumberManager telNumManager = new TelNumberManager();
                    ArrayList<TelNumberBean> arrTelNumberBean = telNumManager.getTelNumbersByEvent(telNumberMetaData);

                    emailLogging.debug( "Arr Telnumber Bean :  " + arrTelNumberBean );
                    if(arrTelNumberBean!=null && !arrTelNumberBean.isEmpty() )
                    {
                        for( TelNumberBean telNumberBean : arrTelNumberBean )
                        {
                            if (Constants.SMS_TEMPLATE.SMS_RSVP_CONFIRMATION.getSmsTemplate().equalsIgnoreCase( smsTemplateBean.getSmsTemplateName() )   &&
                                    ( Constants.EVENT_TASK.RSVP.getTask().equalsIgnoreCase(telNumberBean.getTelNumberType())
                                            || Constants.EVENT_TASK.DEMO_RSVP.getTask().equalsIgnoreCase(telNumberBean.getTelNumberType()) ) )
                            {
                                emailObject.set.setFromPhoneNumber(ParseUtil.checkNull(telNumberBean.getTelNumber()));   // set FROM Number
                            }
                            else if (Constants.SMS_TEMPLATE.SMS_SEATING_CONFIRMATION.getSmsTemplate().equalsIgnoreCase( smsTemplateBean.getSmsTemplateName() ) &&
                                    (Constants.EVENT_TASK.SEATING.getTask().equalsIgnoreCase(telNumberBean.getTelNumberType())
                                            || Constants.EVENT_TASK.DEMO_SEATING.getTask().equalsIgnoreCase(telNumberBean.getTelNumberType()) ) )
                            {
                                emailObject.setFromPhoneNumber( ParseUtil.checkNull(telNumberBean.getTelNumber()) );    // set FROM Number
                            }
                        }
                    }*/
                }
                else
                {
                    emailLogging.info( " Invalid template ID :  " + emailNewScheduleBean );
                }
            }
            else
            {
                emailLogging.info( "Unable to retrieve a valid Guest Bean :  " + emailNewScheduleBean );
            }

            emailLogging.info( "Email Object after creation  :  " + emailObject );
        }
        else
        {
            emailLogging.info( "Invalid Email Schedule Bean. Unable to create the SMS Object" );
        }
        return emailObject;
    }

    private EmailObject getRSVPEmailObject( EmailScheduleBean emailScheduleBean , EmailTemplateBean emailTemplateBean )
    {
        EmailObject emailObject = new EmailQueueBean();
        if( emailScheduleBean!=null && emailTemplateBean!=null )
        {
            String sEventID = ParseUtil.checkNull( emailScheduleBean.getEventId() );
            String sGuestId = ParseUtil.checkNull( emailScheduleBean.getGuestId() );

            if( sEventID!=null && !"".equalsIgnoreCase(sEventID) && sGuestId!=null
                    && !"".equalsIgnoreCase(sGuestId) )
            {
                EventGuestMetaData eventGuestMetaData = new EventGuestMetaData();
                eventGuestMetaData.setEventId(sEventID);

                ArrayList<String> arrGuestId = new ArrayList<String>();
                arrGuestId.add(sGuestId);
                eventGuestMetaData.setArrGuestId(arrGuestId);

                EventGuestManager eventGuestManager = new EventGuestManager();
                EventGuestBean eventGuestBean = eventGuestManager.getGuest(eventGuestMetaData);

                if(eventGuestBean!=null )
                {
                    String sHtmlBody  =  ParseUtil.checkNull(emailTemplateBean.getHtmlBody());
                    String sTextBody  =  ParseUtil.checkNull(emailTemplateBean.getTextBody());

                    Integer iRsvpNumber = ParseUtil.sToI( eventGuestBean.getRsvpSeats() );

                    boolean isRSVPDataExists = false;
                    if(iRsvpNumber>0)
                    {
                        sHtmlBody = sHtmlBody.replaceAll("__RSVP_RESPONSE_CONFIRMATION__", ParseUtil.iToS(iRsvpNumber) );
                        sTextBody = sTextBody.replaceAll("__RSVP_RESPONSE_CONFIRMATION__", ParseUtil.iToS(iRsvpNumber) );

                        isRSVPDataExists = true;

                        //emailObject.setSmsObjectExist( true );
                        emailObject.setHtmlBody(sHtmlBody);
                        emailObject.setTextBody( sTextBody );

                        emailObject = updateEventData( emailObject , sEventID );
                    }
                }
            }
        }
        return emailObject;
    }

    private EmailObject getSeatingConfirmationEmailObject( EmailScheduleBean emailScheduleBean,EmailTemplateBean emailTemplateBean  )
    {
        EmailObject emailObject = new EmailQueueBean();
        if( emailScheduleBean!=null )
        {
            String sEventID = ParseUtil.checkNull( emailScheduleBean.getEventId() );
            String sGuestId = ParseUtil.checkNull( emailScheduleBean.getGuestId() );

            if( sEventID!=null && !"".equalsIgnoreCase(sEventID) && sGuestId!=null
                    && !"".equalsIgnoreCase(sGuestId) )
            {
                GuestTableManager guestTableManager = new GuestTableManager();
                //ArrayList<String> arrTableId = guestTableManager.getEventGuestTables(sGuestId, sEventID);

                GuestTableMetaData guestTableMetaData = new GuestTableMetaData();
                guestTableMetaData.setGuestId( sGuestId );
                guestTableMetaData.setEventId( sEventID );


                ArrayList<TableGuestsBean> arrTableGuestBean = guestTableManager.getGuestsEventTableAssignments( guestTableMetaData );

                if(arrTableGuestBean!=null && !arrTableGuestBean.isEmpty())
                {
                    String sHtmlBody  =  ParseUtil.checkNull(emailTemplateBean.getHtmlBody());
                    String sTextBody  =  ParseUtil.checkNull(emailTemplateBean.getTextBody());

                    TableManager tableManager = new TableManager();
                    boolean isFirstTable = true;
                    boolean isTableExists = false;
                    String sTableText = "";
                    for( TableGuestsBean  tableGuestBean : arrTableGuestBean )
                    {
                        int numOfSeats = ParseUtil.sToI(tableGuestBean.getGuestAssignedSeats());

                        if( !isFirstTable)
                        {
                            sTableText = sTableText + ", ";
                        }

                        Integer iTableNum = ParseUtil.sToI( tableGuestBean.getTableNum() );
                        if(iTableNum>0)
                        {
                            sTableText = sTableText + " table " + iTableNum;
                            isTableExists = true;
                        }
                        isFirstTable = false;
                    }

                    if( isTableExists )
                    {
                        sHtmlBody = sHtmlBody.replaceAll("__SEATING_CONFIRMATION__",sTableText );
                        sTextBody = sTextBody.replaceAll("__SEATING_CONFIRMATION__",sTableText );

                        emailObject.setHtmlBody( sHtmlBody );
                        emailObject.setTextBody( sTextBody );

                        emailObject = updateEventData( emailObject , sEventID );
                    }
                }
            }
        }
        return emailObject;
    }

    public EmailObject updateEventData(EmailObject emailObject, String sEventId)
    {
        if(emailObject!=null)
        {
            String sHtmlBody = ParseUtil.checkNull( emailObject.getHtmlBody() );
            String sTextBody = ParseUtil.checkNull( emailObject.getTextBody() );

            EventManager eventManager = new EventManager();
            EventBean eventBean = eventManager.getEvent( sEventId );

            if( eventBean!=null )
            {
                sHtmlBody = sHtmlBody.replaceAll( "__EVENT_NAME__", ParseUtil.checkNull( eventBean.getEventName() ) );
                sTextBody = sTextBody.replaceAll( "__EVENT_NAME__", ParseUtil.checkNull( eventBean.getEventName() ) );

                emailObject.setHtmlBody(sHtmlBody);
                emailObject.setTextBody(sTextBody);
            }
        }
        return emailObject;
    }
}
