package com.gs.manager.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.gs.bean.*;
import com.gs.bean.usage.PhoneCallUsageBean;
import com.gs.bean.usage.TextMessageUsageBean;
import com.gs.common.usage.PhoneCallUsage;
import com.gs.common.usage.TextMessageUsage;
import com.gs.common.usage.Usage;
import com.gs.common.usage.UsageMetaData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.common.Constants;
import com.gs.common.DateSupport;
import com.gs.common.exception.ExceptionHandler;
import com.gs.common.ParseUtil;
import com.gs.common.Utility;
import com.gs.data.event.EventData;
import com.gs.manager.AdminManager;

public class EventManager {
	private static final Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);

	public EventBean createEvent(EventBean eventBean) {
		if (eventBean == null || (eventBean!=null && Utility.isNullOrEmpty(eventBean.getEventAdminId()))) {
			appLogging.error("There Admin to create an event for." + eventBean);
			eventBean = null;
		} else if (eventBean != null && !Utility.isNullOrEmpty(eventBean.getEventAdminId()))  {
            appLogging.error("Before trying to insert the eventBean : " + eventBean.getEventId());
			EventData eventData = new EventData();
			int iNumOfRecords = eventData.insertEvent(eventBean);

			if (iNumOfRecords > 0) {
				appLogging.info("Create Event : " + eventBean.getEventId());
			} else {
				appLogging.error("Error creating Event : " + eventBean);
				eventBean = null; // so that this will not be used again.
			}

		}
		return eventBean;
	}

	public EventBean createEvent(EventCreationMetaDataBean eventMeta) {
		EventBean eventBean = generateEventBean(eventMeta);

		eventBean = createEvent(eventBean);

		appLogging.info(" Create Event Bean : " + eventBean);
		return eventBean;
	}

	public EventBean generateEventBean(EventCreationMetaDataBean eventMeta) {
		EventBean eventBean = new EventBean();

		if (eventMeta != null && eventMeta.getAdminBean() != null
				&& !"".equalsIgnoreCase(eventMeta.getAdminBean().getAdminId())) {
            if(eventMeta.isUpdateEvent() && !Utility.isNullOrEmpty(eventMeta.getEventId())){
                eventBean.setEventId( eventMeta.getEventId() );
            } else if(eventMeta.isCreateEvent()) {
                eventBean.setEventId(Utility.getNewGuid());
            }

			eventBean.setEventFolderId(Constants.ROOT_FOLDER);
			eventBean.setEventAdminId(eventMeta.getAdminBean().getAdminId());
			eventBean.setEventName(Constants.DEFAULT_EVENT_NAME);
			if (eventMeta != null && eventMeta.getEventName() != null
					&& !"".equalsIgnoreCase(eventMeta.getEventName())) {
				eventBean.setEventName(eventMeta.getEventName());
			}
			eventBean.setEventCreateDate(DateSupport.getEpochMillis());
			eventBean.setHumanCreateDate(DateSupport.getUTCDateTime());
			eventBean.setIsTmp("1");
			eventBean.setDelRow("0");
            eventBean.setEventTimeZone( eventMeta.getEventTimeZone() );

            try{
                DateObject eventDate = DateSupport.convertTime(eventMeta.getEventDate(),DateSupport.getTimeZone( eventMeta.getEventTimeZone() ), eventMeta.getEventDatePattern() , DateSupport.getTimeZone(Constants.DEFAULT_TIMEZONE) , Constants.DATE_PATTERN_TZ  );

                eventBean.setEventDate(eventDate.getMillis());
                eventBean.setHumanEventDate(eventDate.getFormattedTime());

                DateObject rsvpDeadline = DateSupport.convertTime(eventMeta.getRsvpDeadlineDate() , DateSupport.getTimeZone( eventMeta.getEventTimeZone() ), eventMeta.getRsvpDeadlineDateDatePattern(), DateSupport.getTimeZone(Constants.DEFAULT_TIMEZONE) , Constants.DATE_PATTERN_TZ  );

                eventBean.setRsvpDeadlineDate( rsvpDeadline.getMillis() );
                eventBean.setHumanRsvpDeadlineDate( rsvpDeadline.getFormattedTime() );
            } catch (Exception e) {

            }

		}

		return eventBean;

	}

	public Integer updateEvent(EventCreationMetaDataBean eventCreateMetaBean) {

		Integer iNumOfRows = 0;
		if (eventCreateMetaBean != null) {
            EventManager eventManager = new  EventManager();
            EventBean eventBean = eventManager.generateEventBean(eventCreateMetaBean);
            appLogging.info( " eventBean : " + eventBean + " eventCreateMetaBean : " + eventCreateMetaBean);
			EventData eventData = new EventData();
			iNumOfRows = eventData.updateEvent(eventBean);
            appLogging.info( " num of rows updateed : " + iNumOfRows  );
		}
		return iNumOfRows;
	}

	public EventTableBean assignTableToEvent(String sEventId, String sTableId) {
		EventTableBean eventTableBean = new EventTableBean();
		if (sEventId != null && sTableId != null) {
			eventTableBean.setEventTableId(Utility.getNewGuid());
			eventTableBean.setTableId(sTableId);
			eventTableBean.setEventId(sEventId);
			eventTableBean.setIsTmp("1");
			eventTableBean.setDelRow("0");
			eventTableBean.setAssignToEvent("1");

			eventTableBean = assignTableToEvent(eventTableBean);

		}

		return eventTableBean;
	}

	public EventTableBean assignTableToEvent(EventTableBean eventTableBean) {
		EventData eventData = new EventData();
		int iNumOfRecords = eventData.insertEventTable(eventTableBean);

		if (iNumOfRecords > 0) {
			appLogging.info("Create Event : "
					+ eventTableBean.getEventTableId());
		} else {
			appLogging.error("Error creating Event Table  : " + eventTableBean);
			eventTableBean = null; // so that this will not be used again.
		}
		return eventTableBean;
	}

	public EventBean getEvent(String sEventId) {
		EventBean eventBean = new EventBean();

		if (sEventId != null && !"".equalsIgnoreCase(sEventId)) {
			EventData eventData = new EventData();
			eventBean = eventData.getEvent(sEventId);
		}
		return eventBean;
	}

	public ArrayList<EventBean> getAllEvents(String sAdminId) {
		ArrayList<EventBean> arrEventBean = new ArrayList<EventBean>();
		if (sAdminId != null && !"".equalsIgnoreCase(sAdminId)) {
			AdminManager adminManager = new AdminManager();

			AdminBean adminBean = adminManager.getAdmin(sAdminId);

			if (adminBean != null && adminBean.isAdminExists()) {
				UserInfoBean adminUserInfoBean = adminBean
						.getAdminUserInfoBean();

				if (adminUserInfoBean.isUserInfoExists()) {
					EventData eventData = new EventData();
					ArrayList<EventBean> tmpArrEventBean = eventData.getAllEventsByAdmin(sAdminId);

					if (tmpArrEventBean != null && !tmpArrEventBean.isEmpty()) {
						for (EventBean eventBean : tmpArrEventBean) {
							eventBean.setHumanEventDate(DateSupport.getTimeByZone(eventBean.getEventDate(),DateSupport.getTimeZone(eventBean.getEventTimeZone()).getID(),
                                    Constants.PRETTY_DATE_PATTERN_1 ));

							arrEventBean.add(eventBean);
						}
					}
				}
			}
		}
		return arrEventBean;
	}

	public JSONObject getEventJson(ArrayList<EventBean> arrEventBean) {
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonEventArray = new JSONArray();
		try {
            appLogging.info("EventBean to JSON invoked " );
			if (arrEventBean != null && !arrEventBean.isEmpty()) {
				Integer numOfEvent = 0;
				for (EventBean eventBean : arrEventBean) {
					jsonEventArray.put(numOfEvent, eventBean.toJson());
					numOfEvent++;
				}

				jsonObject.put("num_of_rows",
						ParseUtil.iToI(arrEventBean.size()));
				jsonObject.put("events", jsonEventArray);
			} else {
                jsonObject.put("num_of_rows", "0" );
            }
		} catch (JSONException e) {
			appLogging.error("Error converting EventBean to JSON "
					+ ExceptionHandler.getStackTrace(e));
		}
		return jsonObject;
	}

	public EventSummaryBean getEventSummary(String sEventId, String sAdminId) {

		EventSummaryBean eventSummaryBean = new EventSummaryBean();
		if (sEventId != null && !"".equalsIgnoreCase(sEventId)) {
			EventBean eventBean = getEvent(sEventId);

			if (eventBean != null) {
				eventSummaryBean.setEventId(eventBean.getEventId());
				eventSummaryBean.setEventName(eventBean.getEventName());

				AdminManager adminManager = new AdminManager();
				AdminBean adminBean = adminManager.getAdmin(sAdminId);

                EventDateObj eventDateObj = new EventDateObj();

                DateObject dateObject = DateSupport.getTimeDateObjectByZone( eventBean.getEventDate() , DateSupport.getTimeZone(eventBean.getEventTimeZone()).getID(),  Constants.PRETTY_DATE_PATTERN_2  );
                eventDateObj.setEventDate( dateObject.getFormattedTime() );
                eventDateObj.setEventHr(DateSupport.getTimeByZone(dateObject.getMillis() , DateSupport.getTimeZone(eventBean.getEventTimeZone()).getID(),  "hh"  ));
                eventDateObj.setEventMin(DateSupport.getTimeByZone(dateObject.getMillis() , DateSupport.getTimeZone(eventBean.getEventTimeZone()).getID(),  "mm"  ));
                eventDateObj.setEventAmPm(DateSupport.getTimeByZone( dateObject.getMillis() , DateSupport.getTimeZone(eventBean.getEventTimeZone()).getID(),  "a"  ));
                eventDateObj.setEventTimeZone( eventBean.getEventTimeZone() );
                eventSummaryBean.setEventDateObj( eventDateObj );

                DateObject dateObjectRsvpDeadline = DateSupport.getTimeDateObjectByZone( eventBean.getRsvpDeadlineDate() , DateSupport.getTimeZone(eventBean.getEventTimeZone()).getID(),  Constants.PRETTY_DATE_PATTERN_2  );
                eventSummaryBean.setRsvpDeadLineDate( dateObjectRsvpDeadline.getFormattedTime() ) ;
			}

			TableManager tableManager = new TableManager();
			HashMap<Integer, TableBean> hmTables = tableManager
					.retrieveEventTables(sEventId);

			if (hmTables != null && !hmTables.isEmpty()) {
				Set<Integer> setTableNum = hmTables.keySet();

				int totalSeats = 0;
				for (Integer tableNum : setTableNum) {
					TableBean tableBean = hmTables.get(tableNum);
					totalSeats = totalSeats
							+ ParseUtil.sToI(tableBean.getNumOfSeats());
				}

				eventSummaryBean.setTotalTable(hmTables.size());
				eventSummaryBean.setTotalSeats(totalSeats);
			}

			GuestTableManager guestTableManager = new GuestTableManager();
			HashMap<Integer, TableGuestsBean> hmTableGuests = guestTableManager
					.getTablesAndGuest(sEventId);

			if (hmTableGuests != null && !hmTableGuests.isEmpty()) {
				Set<Integer> setTableGuestNum = hmTableGuests.keySet();

				int assignedSeats = 0;
				for (Integer tableGuestNum : setTableGuestNum) {
					TableGuestsBean tableGuestBean = hmTableGuests
							.get(tableGuestNum);
					assignedSeats = assignedSeats
							+ ParseUtil.sToI(tableGuestBean
									.getGuestAssignedSeats());
				}
				eventSummaryBean.setAssignedSeats(assignedSeats);
			}

			EventGuestMetaData eventGuestMetaData = new EventGuestMetaData();
			eventGuestMetaData.setEventId(sEventId);
			EventGuestManager eventGuestManager = new EventGuestManager();
			ArrayList<EventGuestBean> arrEventGuestBean = eventGuestManager
					.getGuestsByEvent(eventGuestMetaData);

			if (arrEventGuestBean != null && !arrEventGuestBean.isEmpty()) {
				int totalGuestRsvp = 0;
				int totalGuestInvited = 0;
				for (EventGuestBean eventGuestBean : arrEventGuestBean) {
                    if( ParseUtil.sToI(eventGuestBean.getRsvpSeats()) >0 ) {
                        totalGuestRsvp = totalGuestRsvp + ParseUtil.sToI(eventGuestBean.getRsvpSeats());
                    }
					totalGuestInvited = totalGuestInvited + ParseUtil.sToI(eventGuestBean.getTotalNumberOfSeats());
				}
				eventSummaryBean.setTotalGuestRsvp(totalGuestRsvp);
				eventSummaryBean.setTotalGuestsInvited(totalGuestInvited);
			}

			TelNumberMetaData telNumberMetaData = new TelNumberMetaData();
			telNumberMetaData.setEventId(sEventId);
			telNumberMetaData.setAdminId(sAdminId);
			TelNumberManager telNumManager = new TelNumberManager();
			ArrayList<TelNumberBean> arrTelNumberBean = telNumManager.getTelNumbersByEvent(telNumberMetaData);
			if (arrTelNumberBean != null && !arrTelNumberBean.isEmpty()) {
				for (TelNumberBean telNumberBean : arrTelNumberBean) {
                    if (Constants.EVENT_TASK.DEMO_TELEPHONE_NUMBER.getTask().equalsIgnoreCase(telNumberBean.getTelNumberType())) {
                        eventSummaryBean.setTelephoneNumber(telNumberBean.getHumanTelNumber());
                        eventSummaryBean.setTelephonyEventNumber(telNumberBean.getSecretEventIdentity());
                        eventSummaryBean.setDemoMode(true);
                    }  else if (Constants.EVENT_TASK.PREMIUM_TELEPHONE_NUMBER.getTask().equalsIgnoreCase(telNumberBean.getTelNumberType())) {
                        eventSummaryBean.setTelephoneNumber(telNumberBean.getHumanTelNumber());
                        eventSummaryBean.setDemoMode(false);
                    }

				}
			}

            if(Constants.TELNUMBER_TYPE.PREMIUM.getType().equalsIgnoreCase(EventFeatureManager.getStringValueFromEventFeature(sEventId,Constants.EVENT_FEATURES.SEATINGPLAN_TELNUMBER_TYPE))) {
                eventSummaryBean.setDemoMode(false);
            } else if(Constants.TELNUMBER_TYPE.DEMO.getType().equalsIgnoreCase(EventFeatureManager.getStringValueFromEventFeature(sEventId,Constants.EVENT_FEATURES.SEATINGPLAN_TELNUMBER_TYPE))){
                eventSummaryBean.setDemoMode(true);
            }

            // Usage Summary
            {
                UsageMetaData usageMetaData = new UsageMetaData();
                usageMetaData.setEventId(sEventId);
                usageMetaData.setAdminId(sAdminId);

                // Phone Call Usage Summary
                Usage phoneCallUsage = new PhoneCallUsage();
                PhoneCallUsageBean phoneCallUsageBean = (PhoneCallUsageBean)phoneCallUsage.getUsage(usageMetaData);
                //appLogging.info("Phone Call Usage : "  + phoneCallUsageBean );

                eventSummaryBean.setPhoneCallUsageBean(phoneCallUsageBean);


                // Text Message Usage Summary
                Usage textMessageUsage = new TextMessageUsage();
                TextMessageUsageBean textMessageUsageBean = (TextMessageUsageBean)textMessageUsage.getUsage(usageMetaData);

                eventSummaryBean.setTextMessageUsageBean(textMessageUsageBean);
            }


            {
                // Seating plan mode
                Constants.EVENT_SEATINGPLAN_MODE eventSeatingplanMode = getEventSeatingMode( sEventId );
                if(eventSeatingplanMode!=null){
                    eventSummaryBean.setSeatingPlanMode(eventSeatingplanMode.getMode()) ;
                }

            }


		}
		return eventSummaryBean;

	}

    public Constants.EVENT_SEATINGPLAN_MODE getEventSeatingMode(String sEventId) {

        Constants.EVENT_SEATINGPLAN_MODE eventSeatingplanMode = null;
        String sValueFromEventFeature =   EventFeatureManager.getStringValueFromEventFeature(sEventId,Constants.EVENT_FEATURES.SEATINGPLAN_MODE);
        if( Constants.EVENT_SEATINGPLAN_MODE.RSVP.getMode().equalsIgnoreCase(sValueFromEventFeature)) {
            eventSeatingplanMode = Constants.EVENT_SEATINGPLAN_MODE.RSVP;
        } else  if( Constants.EVENT_SEATINGPLAN_MODE.SEATING.getMode().equalsIgnoreCase(sValueFromEventFeature)) {
            eventSeatingplanMode = Constants.EVENT_SEATINGPLAN_MODE.SEATING;
        }
        return eventSeatingplanMode;
    }
}
