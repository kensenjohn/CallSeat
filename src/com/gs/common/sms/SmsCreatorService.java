package com.gs.common.sms;


import com.gs.bean.*;
import com.gs.bean.sms.SmsObject;
import com.gs.bean.sms.SmsScheduleBean;
import com.gs.bean.sms.SmsTemplateBean;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.DateSupport;
import com.gs.common.ParseUtil;
import com.gs.manager.GuestManager;
import com.gs.manager.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 2/16/13
 * Time: 2:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class SmsCreatorService {
    private SmsCreator smsCreator;


    private static final Logger smsLogging = LoggerFactory.getLogger(Constants.SMS_LOGS);
    private Configuration smsConfig = Configuration.getInstance(Constants.SMS_PROP);

    public SmsCreatorService( SmsCreator smsCreator )
    {
        this.smsCreator = smsCreator;
    }

    public void invokeSmsCreator() {

        if(this.smsCreator!=null)
        {
            smsLogging.debug("Start execution of sms creator.");

            Long lCurrentTime =  DateSupport.getEpochMillis();
            ArrayList<SmsScheduleBean> arrOldSchedulerBean = getOldSmsSchedules(lCurrentTime);

            if(arrOldSchedulerBean!=null && !arrOldSchedulerBean.isEmpty())
            {
                smsLogging.debug( " Old Sms Scheudler Beans " + arrOldSchedulerBean);
                for(SmsScheduleBean smsOldScheduleBean : arrOldSchedulerBean )
                {
                    smsOldScheduleBean.setScheduleStatus( Constants.SCHEDULER_STATUS.ERROR.getSchedulerStatus()  );
                    this.smsCreator.update( smsOldScheduleBean );
                }
            }

            ArrayList<SmsScheduleBean> arrNewSchedulerBean = getNewSmsSchedules(lCurrentTime);
            if(arrNewSchedulerBean!=null && !arrNewSchedulerBean.isEmpty())
            {
                smsLogging.debug( " New Sms Scheduler Beans " + arrNewSchedulerBean);
                for(SmsScheduleBean smsNewScheduleBean : arrNewSchedulerBean )
                {
                    SmsObject smsObject = createSmsObject( smsNewScheduleBean );
                    this.smsCreator.create( smsObject , smsNewScheduleBean);
                }
            }
            smsLogging.debug("End execution of sms creator.");
        }
    }

    public ArrayList<SmsScheduleBean> getOldSmsSchedules(Long lCurrentTime )
    {

        SmsSchedulerData smsSchedulerData = new SmsSchedulerData();
        Long lScheduleTime = DateSupport.subtractTime( lCurrentTime , ParseUtil.sToI( smsConfig.get(Constants.PROP_SMS_SCHEDULE_PICKUPTIME_PADDING) ), Constants.TIME_UNIT.MINUTES );
        ArrayList<SmsScheduleBean> arrSchedulerBean = smsSchedulerData.getArrSchedule(lScheduleTime, lCurrentTime,  Constants.SCHEDULER_STATUS.NEW_SCHEDULE,Constants.SCHEDULE_PICKUP_TYPE.OLD_RECORDS);

        return arrSchedulerBean;
    }

    public ArrayList<SmsScheduleBean> getNewSmsSchedules( Long lCurrentTime )
    {
        SmsSchedulerData smsSchedulerData = new SmsSchedulerData();
        Long lScheduleTime = DateSupport.subtractTime( lCurrentTime , ParseUtil.sToI( smsConfig.get(Constants.PROP_SMS_SCHEDULE_PICKUPTIME_PADDING) ) , Constants.TIME_UNIT.MINUTES );
        ArrayList<SmsScheduleBean> arrSchedulerBean = smsSchedulerData.getArrSchedule(lScheduleTime, lCurrentTime,  Constants.SCHEDULER_STATUS.NEW_SCHEDULE,Constants.SCHEDULE_PICKUP_TYPE.NEW_RECORDS);

        return arrSchedulerBean;
    }

    public SmsObject createSmsObject( SmsScheduleBean smsNewScheduleBean )
    {
        SmsObject smsObject = new SmsObject();
        if(smsNewScheduleBean != null )
        {
            String sGuestId = ParseUtil.checkNull( smsNewScheduleBean.getGuestId() );

            GuestManager guestManager = new GuestManager();
            GuestBean guestBean = guestManager.getGuest(sGuestId);

            if(guestBean!=null && !"".equalsIgnoreCase(guestBean.getGuestId()))
            {
                // Getting template data
                String sTemplateId = ParseUtil.checkNull( smsNewScheduleBean.getSmsTemplateId() ) ;

                if( sTemplateId!= null && !"".equalsIgnoreCase(sTemplateId))
                {
                    SmsServiceData smsServiceData = new SmsServiceData();
                    SmsTemplateBean smsTemplateBean = smsServiceData.getSmsTemplateById( sTemplateId );

                    smsLogging.debug( "Sms Template Bean :  " + smsTemplateBean );
                    if( smsTemplateBean!=null )
                    {
                        if( Constants.SMS_TEMPLATE.SMS_RSVP_CONFIRMATION.getSmsTemplate().equalsIgnoreCase( smsTemplateBean.getSmsTemplateName() ))
                        {
                            smsObject = getRSVPSmsObject(  smsNewScheduleBean , smsTemplateBean  );
                        }
                        else if( Constants.SMS_TEMPLATE.SMS_SEATING_CONFIRMATION.getSmsTemplate().equalsIgnoreCase( smsTemplateBean.getSmsTemplateName() ))
                        {
                            smsObject = getSeatingConfirmationSmsObject( smsNewScheduleBean , smsTemplateBean );
                        }
                    }

                    smsLogging.debug( "Guest bean from new schedule  " + guestBean );
                    smsObject.setToPhoneNumber( ParseUtil.checkNull( guestBean.getUserInfoBean().getCellPhone() ));   // set TO Number

                    // Getting telephonyData data
                    String sEventId = ParseUtil.checkNull( smsNewScheduleBean.getEventId() );
                    String sAdminId = ParseUtil.checkNull( smsNewScheduleBean.getAdminId() );

                    TelNumberMetaData telNumberMetaData = new TelNumberMetaData();
                    telNumberMetaData.setEventId(sEventId);
                    telNumberMetaData.setAdminId(sAdminId);
                    TelNumberManager telNumManager = new TelNumberManager();
                    ArrayList<TelNumberBean> arrTelNumberBean = telNumManager.getTelNumbersByEvent(telNumberMetaData);

                    smsLogging.debug( "Arr Telnumber Bean :  " + arrTelNumberBean );
                    if(arrTelNumberBean!=null && !arrTelNumberBean.isEmpty() )
                    {
                        for( TelNumberBean telNumberBean : arrTelNumberBean )
                        {
                            smsObject.setFromPhoneNumber( ParseUtil.checkNull(telNumberBean.getTelNumber()) );   // set FROM Number
                        }
                    }
                }
                else
                {
                    smsLogging.info( " Invalid template ID :  " + smsNewScheduleBean );
                }
            }
            else
            {
                smsLogging.info( "Unable to retrieve a valid Guest Bean :  " + smsNewScheduleBean );
            }

            smsLogging.info( "Sms Object after creation  :  " + smsObject );
        }
        else
        {
            smsLogging.info( "Invalid Sms Schedule Bean. Unable to create the SMS Object" );
        }
        return smsObject;
    }

    private SmsObject getRSVPSmsObject( SmsScheduleBean smsScheduleBean , SmsTemplateBean smsTemplateBean )
    {
        SmsObject smsObject = new SmsObject();
        if( smsScheduleBean!=null && smsTemplateBean!=null )
        {
            String sEventID = ParseUtil.checkNull( smsScheduleBean.getEventId() );
            String sGuestId = ParseUtil.checkNull( smsScheduleBean.getGuestId() );

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
                    String sSmsBody  =  ParseUtil.checkNull(smsTemplateBean.getSmsBody());

                    Integer iRsvpNumber = ParseUtil.sToI( eventGuestBean.getRsvpSeats() );

                    boolean isRSVPDataExists = false;
                    if(iRsvpNumber>0)
                    {
                        sSmsBody = sSmsBody.replaceAll("__NO_OF_SEAT__", ParseUtil.iToS(iRsvpNumber) );
                        isRSVPDataExists = true;

                        smsObject.setSmsObjectExist( true );
                        smsObject.setMessage( sSmsBody );

                        smsObject = updateEventData( smsObject , sEventID );
                    }
                }
            }
        }
        return smsObject;
    }

    private SmsObject getSeatingConfirmationSmsObject( SmsScheduleBean smsScheduleBean,SmsTemplateBean smsTemplateBean  )
    {
        SmsObject smsObject = new SmsObject();
        if( smsScheduleBean!=null )
        {
            String sEventID = ParseUtil.checkNull( smsScheduleBean.getEventId() );
            String sGuestId = ParseUtil.checkNull( smsScheduleBean.getGuestId() );

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
                    String sSmsBody  =  ParseUtil.checkNull(smsTemplateBean.getSmsBody());
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
                        sSmsBody = sSmsBody.replaceAll("__TABLE_LIST__",sTableText );

                        smsObject.setMessage(sSmsBody);
                        smsObject.setSmsObjectExist( true );

                        smsObject = updateEventData( smsObject , sEventID );
                    }
                }
            }
        }
        return smsObject;
    }

    public SmsObject updateEventData(SmsObject smsObject, String sEventId)
    {
        if(smsObject!=null)
        {
            String smsBody = ParseUtil.checkNull( smsObject.getMessage() );
            EventManager eventManager = new EventManager();
            EventBean eventBean = eventManager.getEvent( sEventId );

            if( eventBean!=null )
            {
                smsBody = smsBody.replaceAll( "__EVENT_NAME__", ParseUtil.checkNull( eventBean.getEventName() ) );
                smsObject.setMessage( smsBody );
            }
        }
        return smsObject;
    }

}
