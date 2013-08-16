package com.gs.response;

import com.gs.bean.response.GuestWebResponseBean;
import com.gs.bean.response.WebRespRequest;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.db.DBDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 8/15/13
 * Time: 7:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class WebResponseData {
    private static final Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);

    Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);
    private String DB_TYPE = applicationConfig.get(Constants.ADMIN_DB);

    public GuestWebResponseBean getGuestWebResponse (WebRespRequest webRequestBean){
        GuestWebResponseBean guestWebResponseBean = new GuestWebResponseBean();
        if(webRequestBean!=null) {
            String sQuery = "SELECT * FROM GTGUESTWEBRESPONSE GTWR WHERE GTWR.LINKID = ? AND  GTWR.WEB_RESPONSE_TYPE = ?";

            ArrayList<Object> aParams = DBDAO.createConstraint(webRequestBean.getLinkId(), webRequestBean.getGuestWebResponseType().name());

            ArrayList<HashMap<String, String>> arrWebResponse = DBDAO .getDBData(DB_TYPE, sQuery, aParams, true, "WebResponseData.java", "getGuestWebResponse()");

            if(arrWebResponse!=null && !arrWebResponse.isEmpty()) {
                for(HashMap<String, String> hmWebResponse : arrWebResponse ){
                    guestWebResponseBean = new GuestWebResponseBean(hmWebResponse);
                }
            }
            appLogging.info("Aparmas: " + aParams + " Query : " + sQuery + "\nguestWebResponseBean : " + guestWebResponseBean);
        }
        return guestWebResponseBean;
    }
}
