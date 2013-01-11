package com.gs.data;

import com.gs.bean.TaxBean;
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
 * Date: 1/8/13
 * Time: 12:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class TaxData {

    private static final Logger appLogging = LoggerFactory
            .getLogger(Constants.APP_LOGS);

    Configuration applicationConfig = Configuration
            .getInstance(Constants.APPLICATION_PROP);

    private String ADMIN_DB = applicationConfig.get(Constants.ADMIN_DB);

    public TaxBean getTaxBean(String sState, String sCountry, Constants.TAX_TYPE taxType)
    {
        TaxBean taxBean = new TaxBean();
        if(sState!=null && !"".equalsIgnoreCase(sState) && sCountry !=null && !"".equalsIgnoreCase(sCountry)
                && taxType!=null)
        {
            String sQuery = "SELECT STATETAXID,TAXTYPE,STATE,COUNTRY ,TAXPERCENTAGE FROM GTSTATETAX WHERE STATE = ?" +
                    " AND COUNTRY = ? AND TAXTYPE = ?";

            ArrayList<Object> aParams = DBDAO.createConstraint(sState , sCountry , taxType.getTaxType() );

            ArrayList<HashMap<String, String>> arrResult = DBDAO.getDBData(ADMIN_DB, sQuery,aParams, true, "TaxData.java", "getTaxBean()" );
            if(arrResult!=null && !arrResult.isEmpty())
            {
                for(HashMap<String, String> hmResult : arrResult )
                {
                    taxBean = new TaxBean(hmResult) ;
                }
            }

        }
        return taxBean;
    }
}
