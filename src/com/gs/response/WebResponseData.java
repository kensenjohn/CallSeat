package com.gs.response;

import com.gs.bean.response.GuestWebResponseBean;
import com.gs.bean.response.WebRespRequest;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.Utility;
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
        }
        return guestWebResponseBean;
    }

    public Integer createGuestWebResponse( GuestWebResponseBean guestWebResponseBean ) {
        int iNumOfRows = 0 ;
        if(guestWebResponseBean!=null && !Utility.isNullOrEmpty(guestWebResponseBean.getGuestWebResponseId()))  {
            String sQuery = "INSERT INTO GTGUESTWEBRESPONSE (GUESTWEBRESPONSEID,WEB_RESPONSE_TYPE,LINKID, LINK_DOMAIN,FK_GUESTID,FK_EVENTID, " +
                    " CREATEDATE,HUMANCREATEDATE,RESPONSE_STATUS, FK_ADMINID ) VALUES(?,?,? ,?,?,? ,?,?,? ,? )";

                ArrayList<Object> aParams = DBDAO.createConstraint( guestWebResponseBean.getGuestWebResponseId(),guestWebResponseBean.getGuestWebResponseType().name(),
                        guestWebResponseBean.getLinkId(),guestWebResponseBean.getLinkDomain(), guestWebResponseBean.getGuestId(), guestWebResponseBean.getEventId(),
                        guestWebResponseBean.getCreateDate(),guestWebResponseBean.getHumanCreateDate(), guestWebResponseBean.getResponseStatus() ,guestWebResponseBean.getAdminId());
            iNumOfRows = DBDAO.putRowsQuery(sQuery,aParams,DB_TYPE,"WebResponseData.java","createGuestWebResponse()") ;

            appLogging.info("Num Of inserts : " + iNumOfRows );
        }
        return iNumOfRows ;
    }

    public Integer updateGuestWebResponse( GuestWebResponseBean guestWebResponseBean ) {
        int iNumOfRows = 0 ;
        if(guestWebResponseBean!=null && !Utility.isNullOrEmpty(guestWebResponseBean.getEventId())  && !Utility.isNullOrEmpty(guestWebResponseBean.getGuestId())
                && !Utility.isNullOrEmpty(guestWebResponseBean.getLinkId()) )  {
            String sQuery = "UPDATE GTGUESTWEBRESPONSE SET  RESPONSEDATE = ? , HUMANRESPONSEDATE = ? , RESPONSE_STATUS = ? WHERE FK_GUESTID = ? AND FK_EVENTID = ? AND " +
                    " LINKID = ? ";
            ArrayList<Object> aParams = DBDAO.createConstraint( guestWebResponseBean.getResponseDate(), guestWebResponseBean.getHumanResponseDate(), guestWebResponseBean.getResponseStatus(),
                    guestWebResponseBean.getGuestId(), guestWebResponseBean.getEventId(), guestWebResponseBean.getLinkId() );
            iNumOfRows = DBDAO.putRowsQuery(sQuery, aParams, DB_TYPE, "WebResponseData.java","updateGuestWebResponse()");
        }
        return iNumOfRows ;
    }
}
