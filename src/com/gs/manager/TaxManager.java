package com.gs.manager;

import com.gs.bean.TaxBean;
import com.gs.common.Constants;
import com.gs.data.TaxData;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 1/8/13
 * Time: 12:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class TaxManager {

    public TaxBean getSalesTax(String sState , String sCountry)
    {
        TaxBean taxBean = new TaxBean();
        if(sState!=null && !"".equalsIgnoreCase(sState) && sCountry !=null && !"".equalsIgnoreCase(sCountry))
        {
            TaxData taxData = new TaxData();
            taxBean = taxData.getTaxBean(sState , sCountry , Constants.TAX_TYPE.SALES_TAX);
        }
         return taxBean;
    }
}
