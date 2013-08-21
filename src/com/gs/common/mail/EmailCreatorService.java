package com.gs.common.mail;

import com.gs.bean.*;
import com.gs.bean.email.EmailObject;
import com.gs.bean.email.EmailQueueBean;
import com.gs.bean.email.EmailScheduleBean;
import com.gs.bean.email.EmailTemplateBean;
import com.gs.bean.response.GuestWebResponseBean;
import com.gs.bean.response.WebRespRequest;
import com.gs.bean.response.WebRespResponse;
import com.gs.bean.sms.SmsObject;
import com.gs.bean.sms.SmsScheduleBean;
import com.gs.bean.sms.SmsTemplateBean;
import com.gs.common.*;
import com.gs.common.sms.SmsSchedulerData;
import com.gs.common.sms.SmsServiceData;
import com.gs.manager.AdminManager;
import com.gs.manager.GuestManager;
import com.gs.manager.event.*;
import com.gs.response.CreateWebRsvpResponse;
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
                    ArrayList<EmailObject> arrEmailObject = createEmailObject(emailNewScheduleBean);
                    if(arrEmailObject!=null && !arrEmailObject.isEmpty() ) {
                        for( EmailObject emailObject : arrEmailObject  ) {
                            this.emailCreator.create( emailObject , emailNewScheduleBean );
                        }
                    }
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

    public ArrayList<EmailObject> createEmailObject( EmailScheduleBean emailNewScheduleBean )
    {
        ArrayList<EmailObject> arrEmailObject = new ArrayList<EmailObject>();
        if(emailNewScheduleBean != null ) {
            EmailTemplateBean emailTemplateBean = getEmailTemplateBean(emailNewScheduleBean.getEmailTemplateId()) ;
            if(emailTemplateBean!=null && !Utility.isNullOrEmpty(emailTemplateBean.getEmailTemplateId())) {
                arrEmailObject = getEmailObjects( emailNewScheduleBean , emailTemplateBean );
            } else {
                emailLogging.info( " Invalid template ID :  " + emailNewScheduleBean );
            }
        } else  {
            emailLogging.info( "Invalid Email Schedule Bean. Unable to create the SMS Object" );
        }

        return arrEmailObject;
    }

    private  EmailTemplateBean getEmailTemplateBean(String sTemplateId) {
        EmailTemplateBean emailTemplateBean = new EmailTemplateBean();
        if( !Utility.isNullOrEmpty( sTemplateId ) ) {
            MailingServiceData emailServiceData = new MailingServiceData();
            emailTemplateBean = emailServiceData.getEmailTemplateById(sTemplateId);
        }
        return emailTemplateBean;
    }

    private ArrayList<EmailObject> getEmailObjects( EmailScheduleBean emailScheduleBean , EmailTemplateBean emailTemplateBean ) {
        ArrayList<EmailObject> arrEmailObject = new ArrayList<EmailObject>();
        if( emailScheduleBean!=null && emailTemplateBean!=null ) {
            if( Constants.EMAIL_TEMPLATE.RSVP_CONFIRMATION_EMAIL.getEmailTemplate().equalsIgnoreCase( emailTemplateBean.getTemplateName() )) {
                arrEmailObject.add( getRSVPEmailObject(emailScheduleBean, emailTemplateBean) );
            } else if( Constants.EMAIL_TEMPLATE.SEATING_CONFIRMATION_EMAIL.getEmailTemplate().equalsIgnoreCase( emailTemplateBean.getTemplateName() )) {
                arrEmailObject.add(getSeatingConfirmationEmailObject(emailScheduleBean, emailTemplateBean));
            } else if( Constants.EMAIL_TEMPLATE.RSVPRESPONSE.getEmailTemplate().equalsIgnoreCase( emailTemplateBean.getTemplateName() )) {
                arrEmailObject = getRsvpResponseEmail(emailScheduleBean , emailTemplateBean );
            }
        }
        return arrEmailObject;
    }

    private GuestBean getGuestBean( String sGuestId) {
        GuestBean guestBean = new GuestBean();
        if(!Utility.isNullOrEmpty(sGuestId)) {
            GuestManager guestManager = new GuestManager();
            guestBean = guestManager.getGuest(sGuestId);
        }
        return  guestBean;
    }

    private ArrayList<EmailObject> getRsvpResponseEmail( EmailScheduleBean emailScheduleBean , EmailTemplateBean emailTemplateBean ) {
        ArrayList<EmailObject> arrEmailObject = new ArrayList<EmailObject>();
        if( emailScheduleBean!=null && emailTemplateBean!=null ) {
            WebRespRequest webRespRequest = new WebRespRequest();
            webRespRequest.setEventId( ParseUtil.checkNull(emailScheduleBean.getEventId()) );
            webRespRequest.setAdminId( ParseUtil.checkNull(emailScheduleBean.getAdminId()) );

            CreateWebRsvpResponse createWebRsvpResponse = new CreateWebRsvpResponse();
            WebRespResponse webRespResponse = createWebRsvpResponse.generateGuestWebResponseBean(webRespRequest);

            if(webRespResponse!=null && webRespResponse.isSuccess() ) {
                ArrayList<GuestWebResponseBean> arrGuestWebResponseBeans = webRespResponse.getArrGuestWebResponse();
                if(arrGuestWebResponseBeans!=null && !arrGuestWebResponseBeans.isEmpty()) {
                    AdminManager adminManager = new AdminManager();
                    EmailTemplateBean guestResponseEmailTemplate = adminManager.getFormattedRSVPResponseEmail( webRespRequest , emailTemplateBean );

                    for(GuestWebResponseBean guestWebResponseBean : arrGuestWebResponseBeans ) {
                        String sGuestId = guestWebResponseBean.getGuestId();
                        GuestBean guestBean = getGuestBean( sGuestId );

                        if(guestBean!=null && !Utility.isNullOrEmpty(guestBean.getGuestId())) {
                            EmailObject emailObject = new EmailQueueBean();
                            emailObject.setEmailSubject( replaceRSVPResponseTemplateWithGuestData( guestResponseEmailTemplate.getEmailSubject() , guestBean ) );
                            emailObject.setHtmlBody( replaceRSVPResponseTemplateWithGuestData(guestResponseEmailTemplate.getHtmlBody(), guestBean ));
                            emailObject.setTextBody( replaceRSVPResponseTemplateWithGuestData(guestResponseEmailTemplate.getTextBody(), guestBean ) );

                            emailObject = updateToAddressData(emailObject , sGuestId );
                            emailObject = updateFromAddress( emailObject , emailTemplateBean ) ;

                            emailObject.setStatus( Constants.EMAIL_STATUS.NEW.getStatus() );

                            arrEmailObject.add( emailObject );
                        } else {
                            emailLogging.info( "Unable to retrieve a valid Guest Bean :  " + ParseUtil.checkNull(guestWebResponseBean.getGuestId()) );
                        }

                    }
                } else {
                    emailLogging.info( "There are no guest RSVP link to be emailed : " + ParseUtil.checkNullObject( emailScheduleBean ) );
                }
            } else {
                emailLogging.info( "Failed to generate the RSVP Links for guests : " + ParseUtil.checkNullObject( emailScheduleBean ) );
            }
        }
        return arrEmailObject;
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

                        emailObject = updateToAddressData(emailObject , sGuestId );
                        emailObject = updateFromAddress( emailObject , emailTemplateBean ) ;

                        emailObject.setStatus( Constants.EMAIL_STATUS.NEW.getStatus() );
                        emailObject.setEmailSubject( ParseUtil.checkNull(emailTemplateBean.getEmailSubject() ) );
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

                        emailObject = updateToAddressData(emailObject , sGuestId );
                        emailObject = updateFromAddress( emailObject , emailTemplateBean ) ;

                        emailObject.setStatus( Constants.EMAIL_STATUS.NEW.getStatus() );
                        emailObject.setEmailSubject( ParseUtil.checkNull(emailTemplateBean.getEmailSubject() ) );
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



    private EmailObject updateToAddressData( EmailObject emailObject , String sGuestId ) {
        if(emailObject!=null && !Utility.isNullOrEmpty(sGuestId)) {
            GuestBean guestBean = getGuestBean( sGuestId );
            if(guestBean != null && !Utility.isNullOrEmpty(guestBean.getGuestId()) ) {

                emailObject.setToAddress( ParseUtil.checkNull( guestBean.getUserInfoBean().getEmail() ));   // set TO Email
                emailObject.setToAddressName( ParseUtil.checkNull( guestBean.getUserInfoBean().getEmail() ) );
            }
        }
        return emailObject;
    }

    private EmailObject updateFromAddress ( EmailObject emailObject , EmailTemplateBean emailTemplateBean ) {

        if(emailObject!=null && emailTemplateBean!=null ) {
            emailObject.setFromAddress( ParseUtil.checkNull( emailTemplateBean.getFromAddress() ) );
            emailObject.setFromAddressName( ParseUtil.checkNull( emailTemplateBean.getFromAddressName() ) );
        }
        return emailObject;
    }





    private String replaceRSVPResponseTemplateWithGuestData(String sText , GuestBean guestBean  ) {
        String srcText = ParseUtil.checkNull(sText);
        if(guestBean!=null && !Utility.isNullOrEmpty(sText) && !Utility.isNullOrEmpty(guestBean.getGuestId()) ) {
            srcText = srcText.replaceAll("__GIVENNAME__",ParseUtil.checkNull(guestBean.getUserInfoBean().getFirstName()) + " " + ParseUtil.checkNull(guestBean.getUserInfoBean().getLastName()));
        }
        return srcText;
    }

}
