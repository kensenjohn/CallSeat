package com.gs.call;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gs.common.Configuration;
import com.gs.common.Constants;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 1/26/13
 * Time: 8:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class DisplayIncomingParameters extends HttpServlet {

    Logger appLogging = LoggerFactory.getLogger("AppLogging");
    Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);
    private String SELECTED_CALL_SERVICE = applicationConfig.get(Constants.PROP_CALL_SERVICE);

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sResponse = performTask(request);
        if (Constants.CALL_SERVICE.TWILIO.getCallService().equalsIgnoreCase(SELECTED_CALL_SERVICE)) {
            response.setContentType("text/xml");
            PrintWriter out = response.getWriter();
            out.println(sResponse);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sResponse = performTask(request);
        if (Constants.CALL_SERVICE.TWILIO.getCallService().equalsIgnoreCase(SELECTED_CALL_SERVICE)) {
            response.setContentType("text/xml");
            PrintWriter out = response.getWriter();
            out.println(sResponse);
        }
    }

    private String performTask(HttpServletRequest request) {
        String sResponse = "";
        if(request!=null)
        {
            String sFinalRequest = "*********************************************************\n";
            Map<String,String[]> mapParameterMap = request.getParameterMap();
            for(Map.Entry<String,String[]> tmpMapParameters : mapParameterMap.entrySet() )
            {
                String sMapKey = tmpMapParameters.getKey();

                String[] arrMapValue = tmpMapParameters.getValue();

                String sMapValue = "";
                if(arrMapValue!=null && arrMapValue.length>0)
                {
                    for(String tmpArrValue : arrMapValue )
                    {
                        sMapValue = sMapValue + tmpArrValue + "*|*";
                    }
                }
                sFinalRequest = sFinalRequest + sMapKey + "-->" + sMapValue + "\n";
            }
            appLogging.info(sFinalRequest);
        }

        if (Constants.CALL_SERVICE.TWILIO.getCallService().equalsIgnoreCase(SELECTED_CALL_SERVICE)) {
            DateTime localServerTime = new DateTime();
            sResponse = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";
            sResponse = "<Response>\n" +
                    "    <Say>Hello This is test "  + localServerTime.getDayOfYear() + " " + localServerTime.getMonthOfYear() + " " + localServerTime.getSecondOfDay() + " </Say>\n" +
                    "    <Pause length=\"77\"/>" +
                    "    <Say>I just paused 70 seconds. Thank You</Say> " +
                    "</Response>";
        }
        return sResponse;
    }
}
