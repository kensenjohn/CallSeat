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
import com.gs.response.AlterWebRsvpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
            emailLogging.info( "Invalid Email Schedule Bean. Unable to create the Email Object" );
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
            } else if( Constants.EMAIL_TEMPLATE.RSVPRESPONSE.getEmailTemplate().equalsIgnoreCase( emailTemplateBean.getTemplateName() ) ||
                    Constants.EMAIL_TEMPLATE.RSVPRESPONSEDEMO.getEmailTemplate().equalsIgnoreCase( emailTemplateBean.getTemplateName() )) {
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
            webRespRequest.setGuestWebResponseType(Constants.GUEST_WEB_RESPONSE_TYPE.RSVP);

            AlterWebRsvpResponse alterWebRsvpResponse = new AlterWebRsvpResponse();
            WebRespResponse webRespResponse = alterWebRsvpResponse.generateGuestWebResponseBean(webRespRequest);

            if(webRespResponse!=null && webRespResponse.isSuccess() ) {
                ArrayList<GuestWebResponseBean> arrGuestWebResponseBeans = webRespResponse.getArrGuestWebResponse();
                if(arrGuestWebResponseBeans!=null && !arrGuestWebResponseBeans.isEmpty()) {
                    AdminManager adminManager = new AdminManager();
                    EmailTemplateBean guestResponseEmailTemplate = adminManager.getFormattedRSVPResponseEmail( webRespRequest , emailTemplateBean );

                    for(GuestWebResponseBean guestWebResponseBean : arrGuestWebResponseBeans ) {
                        String sGuestId = guestWebResponseBean.getGuestId();
                        GuestBean guestBean = getGuestBean( sGuestId );

                        if(guestBean!=null && !Utility.isNullOrEmpty( sGuestId )) {
                            WebRespRequest guestWebRespRequest = new WebRespRequest();
                            guestWebRespRequest.setGuestBean( guestBean );
                            guestWebRespRequest.setGuestWebResponseBean( guestWebResponseBean );

                            EmailObject emailObject = new EmailQueueBean();
                            emailObject.setEmailSubject( replaceRSVPResponseTemplateWithGuestData( guestResponseEmailTemplate.getEmailSubject() , guestWebRespRequest ) );
                            emailObject.setHtmlBody( replaceRSVPResponseTemplateWithGuestData(guestResponseEmailTemplate.getHtmlBody(), guestWebRespRequest ));
                            emailObject.setTextBody( replaceRSVPResponseTemplateWithGuestData(guestResponseEmailTemplate.getTextBody(), guestWebRespRequest ) );

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
        emailLogging.info( "Final all Email Object  :  " + arrEmailObject );
        return arrEmailObject;
    }

    private EmailObject getRSVPEmailObject( EmailScheduleBean emailScheduleBean , EmailTemplateBean emailTemplateBean )
    {
        EmailObject emailObject = new EmailQueueBean();
        if( emailScheduleBean!=null && emailTemplateBean!=null )
        {
            String sEventID = ParseUtil.checkNull( emailScheduleBean.getEventId() );
            String sGuestId = ParseUtil.checkNull( emailScheduleBean.getGuestId() );
            String sAdminId = ParseUtil.checkNull( emailScheduleBean.getAdminId() );
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
                    if(iRsvpNumber>0) {
                        GuestManager guestManager = new GuestManager();
                        GuestBean guestBean = guestManager.getGuest( sGuestId) ;
                        String sGivenName = ParseUtil.checkNull(guestBean.getUserInfoBean().getFirstName()) + " " + ParseUtil.checkNull(guestBean.getUserInfoBean().getLastName());
                        if(sHtmlBody.contains("__GIVENNAME__")) {
                            sHtmlBody = sHtmlBody.replaceAll("__GIVENNAME__",sGivenName);
                        }
                        if(sTextBody.contains("__GIVENNAME__")) {
                            sTextBody = sTextBody.replaceAll("__GIVENNAME__",sGivenName);
                        }

                        EventManager eventManager = new EventManager();
                        EventBean eventBean = eventManager.getEvent(sEventID);

                        sHtmlBody = sHtmlBody.replaceAll("__SEATINGPLANNAME__",ParseUtil.checkNull( eventBean.getEventName() ));
                        sTextBody = sTextBody.replaceAll("__SEATINGPLANNAME__",ParseUtil.checkNull( eventBean.getEventName() ));

                        //__RSVPRESPONSE__
                        StringBuilder strRsvpResponse = new StringBuilder();
                        if(iRsvpNumber <= 0) {
                            strRsvpResponse.append("You responded that you will NOT be attending.");
                        } else if( iRsvpNumber == 1) {
                            strRsvpResponse.append("You responded that you will be attending.");
                        }  else if( iRsvpNumber >1) {
                            strRsvpResponse.append("You responded that you will attend along with ");
                            Integer iNumOfGuests =  (iRsvpNumber-1);
                            strRsvpResponse.append( iNumOfGuests );
                            if( iNumOfGuests == 1) {
                                strRsvpResponse.append( " guest." );
                            }else if(  iNumOfGuests > 1) {
                                strRsvpResponse.append( " guests." );
                            }

                        }



                        sHtmlBody = sHtmlBody.replaceAll("__RSVPRESPONSE__", strRsvpResponse.toString() );
                        sTextBody = sTextBody.replaceAll("__RSVPRESPONSE__", strRsvpResponse.toString() );

                        TelNumberMetaData telNumberMetaData = new TelNumberMetaData();

                        telNumberMetaData.setAdminId( sAdminId );
                        telNumberMetaData.setEventId( sEventID );
                        TelNumberManager telNumManager = new TelNumberManager();
                        ArrayList<TelNumberBean> arrTelNumberBean = telNumManager.getTelNumEventDetails(telNumberMetaData);

                        Constants.EMAIL_TEMPLATE emailTemplateType = Constants.EMAIL_TEMPLATE.RSVPRESPONSEDEMO;
                        TelNumberBean rsvpTelNumberBean = new TelNumberBean();
                        if(arrTelNumberBean!=null && !arrTelNumberBean.isEmpty()) {
                            for(TelNumberBean telNumberBean : arrTelNumberBean ){
                                if( Constants.EVENT_TASK.RSVP.getTask().equalsIgnoreCase(telNumberBean.getTelNumberType()))  {
                                    emailTemplateType =  Constants.EMAIL_TEMPLATE.RSVPRESPONSE ;
                                    rsvpTelNumberBean = telNumberBean;
                                } else if ( Constants.EVENT_TASK.DEMO_RSVP.getTask().equalsIgnoreCase(telNumberBean.getTelNumberType()) ) {
                                    emailTemplateType =  Constants.EMAIL_TEMPLATE.RSVPRESPONSEDEMO ;
                                    rsvpTelNumberBean = telNumberBean;
                                }
                            }
                        }

                        String sTelephoneNumber = ParseUtil.checkNull(rsvpTelNumberBean.getHumanTelNumber());
                        if ( Constants.EVENT_TASK.DEMO_RSVP.getTask().equalsIgnoreCase(rsvpTelNumberBean.getTelNumberType()) ) {
                            sTelephoneNumber = " Plan Id : " + ParseUtil.checkNull( rsvpTelNumberBean.getSecretEventIdentity() ) + " Extension : " +  ParseUtil.checkNull( rsvpTelNumberBean.getSecretEventKey() );
                        }
                        sHtmlBody = sHtmlBody.replaceAll("__RSVPPHONENUM__",ParseUtil.checkNull(sTelephoneNumber));
                        sTextBody = sTextBody.replaceAll("__RSVPPHONENUM__",ParseUtil.checkNull(sTelephoneNumber));


                        AdminManager adminManager = new AdminManager();
                        AdminBean adminBean = adminManager.getAdmin(sAdminId);
                        if(adminBean!=null && adminBean.getAdminId()!=null && !"".equalsIgnoreCase( adminBean.getAdminId() ) ) {

                            String  sAdminName =  ParseUtil.checkNull(adminBean.getAdminUserInfoBean().getFirstName()) + " " +   ParseUtil.checkNull(adminBean.getAdminUserInfoBean().getLastName());
                            sHtmlBody = sHtmlBody.replaceAll("__HOSTNAME__", sAdminName );
                            sTextBody = sTextBody.replaceAll("__HOSTNAME__", sAdminName );
                        }

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

    private ArrayList<String> getEventGuestId( EmailScheduleBean emailScheduleBean) {
        ArrayList<String> arrEventGuestId = new ArrayList<String>();
        if( emailScheduleBean!=null && !Utility.isNullOrEmpty(emailScheduleBean.getGuestId()) ) {
            arrEventGuestId.add(  ParseUtil.checkNull( emailScheduleBean.getGuestId() ) );
        } else {
            GuestTableManager guestTableManager = new GuestTableManager();
            HashMap<Integer, TableGuestsBean> hmTableGuests = guestTableManager.getTablesAndGuest(  ParseUtil.checkNull( emailScheduleBean.getEventId() ) );
            if(hmTableGuests!=null && !hmTableGuests.isEmpty()) {
                HashMap<String,String> hmGuestId = new HashMap<String, String>();
                for(Map.Entry<Integer,TableGuestsBean>mapTableGuestsBean : hmTableGuests.entrySet() ) {
                    TableGuestsBean tableGuestsBean = mapTableGuestsBean.getValue();

                    if(tableGuestsBean!=null && !Utility.isNullOrEmpty(tableGuestsBean.getGuestId())) {
                        hmGuestId.put(tableGuestsBean.getGuestId(),tableGuestsBean.getGuestId());
                    }
                }

                if( hmGuestId!=null && !hmGuestId.isEmpty() ) {
                    for(Map.Entry<String,String>mapGuestId : hmGuestId.entrySet() ) {
                        arrEventGuestId.add( mapGuestId.getKey() );
                    }
                }
            }

        }
        return arrEventGuestId;
    }

    private String getTableAssignmentFormattedText(  ArrayList<TableGuestsBean> arrTableGuestBean ) {
        String sTableFormattedText = Constants.EMPTY;
        boolean  isFirstTable = true;
        for( TableGuestsBean  tableGuestBean : arrTableGuestBean ) {
            int numOfSeats = ParseUtil.sToI(tableGuestBean.getGuestAssignedSeats());

            if( !isFirstTable) {
                sTableFormattedText = sTableFormattedText + ", ";
            }

            Integer iTableNum = ParseUtil.sToI( tableGuestBean.getTableNum() );
            if(iTableNum>0)  {
                sTableFormattedText = sTableFormattedText + " table " + iTableNum;
            }
            isFirstTable = false;
        }
        return sTableFormattedText;
    }

    private EmailObject getSeatingConfirmationEmailObject( EmailScheduleBean emailScheduleBean,EmailTemplateBean emailTemplateBean  )
    {
        EmailObject emailObject = new EmailQueueBean();
        if( emailScheduleBean!=null )
        {
            String sEventID = ParseUtil.checkNull( emailScheduleBean.getEventId() );
            String sAdminId = ParseUtil.checkNull( emailScheduleBean.getAdminId() );
            ArrayList<String> arrEventGuestId = getEventGuestId( emailScheduleBean );
            if( sEventID!=null && !"".equalsIgnoreCase(sEventID) && arrEventGuestId!=null
                    && !arrEventGuestId.isEmpty() )  {

                for(String sTmpGuestId : arrEventGuestId) {
                    GuestTableManager guestTableManager = new GuestTableManager();

                    GuestTableMetaData guestTableMetaData = new GuestTableMetaData();
                    guestTableMetaData.setGuestId( sTmpGuestId );
                    guestTableMetaData.setEventId( sEventID );


                    ArrayList<TableGuestsBean> arrTableGuestBean = guestTableManager.getGuestsEventTableAssignments( guestTableMetaData );

                    if(arrTableGuestBean!=null && !arrTableGuestBean.isEmpty()) {
                        String sTableText = getTableAssignmentFormattedText(arrTableGuestBean);
                        if( !Utility.isNullOrEmpty(sTableText) ) {

                            String sHtmlBody  =  ParseUtil.checkNull(emailTemplateBean.getHtmlBody());
                            String sTextBody  =  ParseUtil.checkNull(emailTemplateBean.getTextBody());

                            GuestManager guestManager = new GuestManager();
                            GuestBean guestBean = guestManager.getGuest( sTmpGuestId) ;
                            String sGivenName = ParseUtil.checkNull(guestBean.getUserInfoBean().getFirstName()) + " " + ParseUtil.checkNull(guestBean.getUserInfoBean().getLastName());
                            if(sHtmlBody.contains("__GIVENNAME__")) {
                                sHtmlBody = sHtmlBody.replaceAll("__GIVENNAME__",sGivenName);
                            }
                            if(sTextBody.contains("__GIVENNAME__")) {
                                sTextBody = sTextBody.replaceAll("__GIVENNAME__",sGivenName);
                            }

                            sHtmlBody = sHtmlBody.replaceAll("__SEATING_CONFIRMATION__",sTableText );
                            sTextBody = sTextBody.replaceAll("__SEATING_CONFIRMATION__",sTableText );


                            AdminManager adminManager = new AdminManager();
                            sHtmlBody = adminManager.replaceTemplateWithEventData( sHtmlBody , sEventID);
                            sHtmlBody = adminManager.replaceTemplateWithAdminData( sHtmlBody , sAdminId);

                            sTextBody = adminManager.replaceTemplateWithEventData( sTextBody , sEventID);
                            sTextBody = adminManager.replaceTemplateWithAdminData( sTextBody , sAdminId);



                            emailObject.setHtmlBody( sHtmlBody );
                            emailObject.setTextBody( sTextBody );

                            emailObject = updateEventData( emailObject , sEventID );

                            emailObject = updateToAddressData(emailObject , sTmpGuestId );
                            emailObject = updateFromAddress( emailObject , emailTemplateBean ) ;

                            emailObject.setStatus( Constants.EMAIL_STATUS.NEW.getStatus() );
                            emailObject.setEmailSubject( ParseUtil.checkNull(emailTemplateBean.getEmailSubject() ) );


                        } else {
                            emailLogging.info("This guest has no table assigned,");
                        }
                    } else {
                        emailLogging.info("This list of table guest bean is empty");
                    }
                }

            } else {
                emailLogging.info("Invalid Guest Id and event guest Id");
            }
        } else{
            emailLogging.info("Invalid request bean");
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





    private String replaceRSVPResponseTemplateWithGuestData(String sText , WebRespRequest guestWebRespRequest  ) {
        String srcText = ParseUtil.checkNull(sText);
        if( guestWebRespRequest!=null ) {
            GuestBean guestBean = guestWebRespRequest.getGuestBean();
            GuestWebResponseBean guestWebResponseBean = guestWebRespRequest.getGuestWebResponseBean();
            if(guestBean!=null && !Utility.isNullOrEmpty(sText) && !Utility.isNullOrEmpty(guestBean.getGuestId())
                    && guestWebResponseBean!=null && !Utility.isNullOrEmpty(guestWebResponseBean.getLinkId() ) ) {
                if(srcText.contains("__GIVENNAME__")) {
                    srcText = srcText.replaceAll("__GIVENNAME__",ParseUtil.checkNull(guestBean.getUserInfoBean().getFirstName()) + " " + ParseUtil.checkNull(guestBean.getUserInfoBean().getLastName()));
                }
                if(srcText.contains("__RSVPLINK__")) {
                    StringBuilder strRsvpLink = new StringBuilder("https://");
                    strRsvpLink.append( guestWebResponseBean.getLinkDomain()).append("/web/r/rsvp.jsp?")
                            .append(Constants.RSVP_WEB_PARAM.LINK_ID.getParam()).append("=").append(ParseUtil.checkNull( guestWebResponseBean.getLinkId()));
                    srcText = srcText.replaceAll("__RSVPLINK__",strRsvpLink.toString() );
                }
            }
        }
        return srcText;
    }
}
