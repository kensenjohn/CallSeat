package com.gs.call;

import java.util.ArrayList;

import com.gs.bean.CallTransactionBean;
import com.gs.call.twilio.twiml.TwimlSupport;
import com.gs.common.CallTransaction;
import com.gs.common.ParseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.TelNumberBean;
import com.gs.bean.twilio.IncomingCallBean;
import com.gs.bean.twilio.TwilioIncomingCallBean;
import com.gs.common.Constants;
import com.gs.manager.event.TelNumberManager;
import com.gs.manager.event.TelNumberMetaData;
import com.gs.manager.event.TelNumberResponse;
import com.gs.call.task.DemoCallTask;
import com.gs.call.task.RsvpTask;
import com.gs.call.task.SeatingTask;
import com.gs.call.task.Task;

public abstract class ProcessCalls {
	protected IncomingCallBean incomingCallBean = null;
    Logger telephonyLogging = LoggerFactory.getLogger(Constants.TELEPHONY_LOGS);

	public ProcessCalls(IncomingCallBean incomingCallBean) {
		this.incomingCallBean = incomingCallBean;
	}

	protected Task identifyTask() {
		Task task = null;
        if(this.incomingCallBean != null )
        {
            CallTransaction callTransaction = CallTransaction.getInstance();
            if ((Constants.CALL_TYPE.DEMO_FIRST_REQUEST.equals(this.incomingCallBean.getCallType()))
                    || Constants.CALL_TYPE.DEMO_GATHER_EVENT_NUM.equals(this.incomingCallBean.getCallType())
                    || Constants.CALL_TYPE.DEMO_GATHER_SECRET_KEY.equals(this.incomingCallBean.getCallType()))
            {
                if(  Constants.CALL_TYPE.DEMO_GATHER_EVENT_NUM.equals(this.incomingCallBean.getCallType())
                ||  Constants.CALL_TYPE.DEMO_GATHER_SECRET_KEY.equals(this.incomingCallBean.getCallType()) )
                {
                    CallTransactionBean callTransactionBean = new CallTransactionBean();
                    callTransaction.updateTransaction(incomingCallBean,callTransactionBean );
                }
                telephonyLogging.info("Task => DemoCallTask(First Request) From :" + incomingCallBean.getFrom() + " call type : " + this.incomingCallBean.getCallType());
                task = new DemoCallTask("", "");
            }  else if (Constants.CALL_TYPE.DEMO_GATHER_RSVP_NUM.equals(this.incomingCallBean.getCallType())) {
                TwilioIncomingCallBean twilioIncomingCall = (TwilioIncomingCallBean) this.incomingCallBean;
                String sEventIdentifier = twilioIncomingCall.getCallerInputEventId();
                String sEventSecretKey = twilioIncomingCall.getCallerInputSecretKey();


                TelNumberMetaData telNumMetaData = new TelNumberMetaData();
                telNumMetaData.setSecretEventIdentifier(sEventIdentifier);
                telNumMetaData.setSecretEventSecretKey(sEventSecretKey);

                TelNumberManager telNumManager = new TelNumberManager();
                ArrayList<TelNumberBean> arrTelNumBean = telNumManager.getTelNumbersFromSecretEventNum(telNumMetaData);
                if (arrTelNumBean != null && !arrTelNumBean.isEmpty()) {
                    for (TelNumberBean telNumberBean : arrTelNumBean) {
                        if (telNumberBean != null
                                && telNumberBean.isTelNumBeanSet()) {
                            String sEventId = telNumberBean.getEventId();
                            String sAdminId = telNumberBean.getAdminId();


                            CallTransactionBean callTransactionBean = new CallTransactionBean();
                            callTransactionBean.setAdminId(sAdminId);
                            callTransactionBean.setEventId(sEventId);
                            callTransaction.updateTransaction(incomingCallBean,callTransactionBean );

                            task = new DemoCallTask(sEventId, sAdminId);
                            telephonyLogging.info("Task => DemoCallTask(Gathering RSVP) From :" + incomingCallBean.getFrom() + " call type : " + this.incomingCallBean.getCallType());

                        }
                    }
                } else {
                    telephonyLogging.debug("Task => DemoCallTask(Gathering RSVP) Could not find the associated Phone number From :" + incomingCallBean.getFrom() + " call type : " + this.incomingCallBean.getCallType());
                }
            }  else if (Constants.CALL_TYPE.DEMO_ERROR_HANGUP.equals(this.incomingCallBean.getCallType()) ||
                    Constants.CALL_TYPE.ERROR_HANGUP.equals(this.incomingCallBean.getCallType())) {

            } else if (this.incomingCallBean != null) {
                String sGuestTelNumber = ParseUtil.checkNull(this.incomingCallBean.getFrom());
                String sEventTelNumber = ParseUtil.checkNull(this.incomingCallBean.getTo());

                TelNumberMetaData telNumMetaData = new TelNumberMetaData();
                telNumMetaData.setGuestTelNumber(sGuestTelNumber);
                telNumMetaData.setEventTaskTelNumber(sEventTelNumber);

                TelNumberManager telNumManager = new TelNumberManager();
                TelNumberResponse telNumberResponse = telNumManager.getTelNumberDetails(telNumMetaData);
                if (telNumberResponse != null  && telNumberResponse.getTelNumberBean() != null) {
                    TelNumberBean telNumberBean = telNumberResponse.getTelNumberBean();
                    if (telNumberBean != null && telNumberBean.isTelNumBeanSet()) {
                        String sEventId = telNumberBean.getEventId();
                        String sAdminId = telNumberBean.getAdminId();

                        String sSeatingPlanMode = TwimlSupport.getSeatingPlanModeFromTelnumber(telNumberBean);
                        if (Constants.EVENT_SEATINGPLAN_MODE.RSVP.getMode().equalsIgnoreCase(sSeatingPlanMode))  {
                            task = new RsvpTask(sEventId, sAdminId);
                            telephonyLogging.info("Task => RsvpTask  From :" + sGuestTelNumber  + " To(Event Phone Number) : " + sEventTelNumber + " call type : " + this.incomingCallBean.getCallType());
                        } else if (Constants.EVENT_SEATINGPLAN_MODE.SEATING.getMode().equalsIgnoreCase(sSeatingPlanMode)) {
                            task = new SeatingTask(sEventId, sAdminId);
                            telephonyLogging.info("Task => SeatingTask From :" + sGuestTelNumber + " To(Event Phone Number) : " + sEventTelNumber + " call type : " + this.incomingCallBean.getCallType());
                        }
                    }

                } else {
                    telephonyLogging.info("Could not find the telephone number details From :" + sGuestTelNumber  + " To(Event Phone Number) : " + sEventTelNumber +  " call type : " + this.incomingCallBean.getCallType());
                }

            }
        }
		return task;
	}

	public abstract CallResponse process();
}
