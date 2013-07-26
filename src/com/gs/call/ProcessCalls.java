package com.gs.call;

import java.util.ArrayList;

import com.gs.bean.CallTransactionBean;
import com.gs.common.CallTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.TelNumberBean;
import com.gs.bean.twilio.IncomingCallBean;
import com.gs.bean.twilio.TwilioIncomingCallBean;
import com.gs.common.Constants;
import com.gs.manager.event.TelNumberManager;
import com.gs.manager.event.TelNumberMetaData;
import com.gs.manager.event.TelNumberResponse;
import com.gs.task.DemoCallTask;
import com.gs.task.RsvpTask;
import com.gs.task.SeatingTask;
import com.gs.task.Task;

public abstract class ProcessCalls {
	protected IncomingCallBean incomingCallBean = null;
	Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);

	public ProcessCalls(IncomingCallBean incomingCallBean) {
		this.incomingCallBean = incomingCallBean;
	}

	protected Task identifyTask() {
		Task task = null;
        if(this.incomingCallBean != null )
        {
            appLogging.info("Identify Task Incoming Call Type : " +  this.incomingCallBean.getCallType()  );

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
                appLogging.info("Identified Task as DemoCallTask : " + incomingCallBean.getFrom() + " - " + this.incomingCallBean.getCallType());
                task = new DemoCallTask("", "");
            }
            else if (Constants.CALL_TYPE.DEMO_GATHER_RSVP_NUM.equals(this.incomingCallBean.getCallType()))
            {
                TwilioIncomingCallBean twilioIncomingCall = (TwilioIncomingCallBean) this.incomingCallBean;
                String sEventIdentifier = twilioIncomingCall.getCallerInputEventId();
                String sEventSecretKey = twilioIncomingCall.getCallerInputSecretKey();


                TelNumberMetaData telNumMetaData = new TelNumberMetaData();
                telNumMetaData.setSecretEventIdentifier(sEventIdentifier);
                telNumMetaData.setSecretEventSecretKey(sEventSecretKey);

                TelNumberManager telNumManager = new TelNumberManager();
                ArrayList<TelNumberBean> arrTelNumBean = telNumManager
                        .getTelNumbersFromSecretEventNumAndKey(telNumMetaData);
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
                        }
                    }
                }

            } else if (this.incomingCallBean != null) {
                String sGuestTelNumber = this.incomingCallBean.getFrom();
                String sEventTelNumber = this.incomingCallBean.getTo();

                TelNumberMetaData telNumMetaData = new TelNumberMetaData();
                telNumMetaData.setGuestTelNumber(sGuestTelNumber);
                telNumMetaData.setEventTaskTelNumber(sEventTelNumber);

                TelNumberManager telNumManager = new TelNumberManager();
                TelNumberResponse telNumberResponse = telNumManager.getTelNumberDetails(telNumMetaData);
                appLogging.info("From Num (guest) : "+ sGuestTelNumber + " To Num (Event Num) : " + sEventTelNumber +" telNumberResponse : " +  telNumberResponse  );
                if (telNumberResponse != null  && telNumberResponse.getTelNumberBean() != null) {
                    TelNumberBean telNumberBean = telNumberResponse.getTelNumberBean();
                    appLogging.info("telNumberBean : " +  telNumberBean  );
                    if (telNumberBean != null && telNumberBean.isTelNumBeanSet()) {
                        String sEventId = telNumberBean.getEventId();
                        String sAdminId = telNumberBean.getAdminId();
                        // String sGuestTelNumber = telNumberBean.
                        if (Constants.EVENT_TASK.RSVP.getTask().equalsIgnoreCase(telNumberBean.getTelNumberType())) {
                            task = new RsvpTask(sEventId, sAdminId);
                        } else if (Constants.EVENT_TASK.SEATING.getTask().equalsIgnoreCase(telNumberBean.getTelNumberType())) {
                            task = new SeatingTask(sEventId, sAdminId);
                        }
                    }

                }

            }
        }
		return task;
	}

	public abstract CallResponse process();
}
