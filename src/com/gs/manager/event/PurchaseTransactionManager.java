package com.gs.manager.event;

import com.gs.bean.PurchaseTransactionBean;
import com.gs.common.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 1/2/13
 * Time: 11:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class PurchaseTransactionManager {

    private static final Logger appLogging = LoggerFactory.getLogger("AppLogging");

    public PurchaseTransactionBean getPurchaseTransactionByEventAdmin(PurchaseTransactionBean purchaseTransactionBean) {
        if(purchaseTransactionBean!=null && !Utility.isNullOrEmpty(purchaseTransactionBean.getAdminId())
                && !Utility.isNullOrEmpty(purchaseTransactionBean.getEventId())) {
             PurchaseTransactionData purchaseTransactionData = new  PurchaseTransactionData();

             purchaseTransactionBean = purchaseTransactionData.getPurchaseTransaction(purchaseTransactionBean.getAdminId(),purchaseTransactionBean.getEventId() );

        } else {
            purchaseTransactionBean = new PurchaseTransactionBean();
        }
        return purchaseTransactionBean;
    }

    public PurchaseTransactionBean getPurchaseTransactionById(PurchaseTransactionBean purchaseTransactionBean)
    {
        if(purchaseTransactionBean!=null)
        {
            if(purchaseTransactionBean.getAdminId()!=null && !"".equalsIgnoreCase(purchaseTransactionBean.getAdminId())
                    && purchaseTransactionBean.getEventId()!=null && !"".equalsIgnoreCase(purchaseTransactionBean.getEventId()))
            {
                PurchaseTransactionData purchaseTransactionData = new  PurchaseTransactionData();

                purchaseTransactionBean = purchaseTransactionData.getPurchaseTransaction(purchaseTransactionBean.getPurchaseTransactionId() );
            }
        }
        else
        {
            purchaseTransactionBean = new PurchaseTransactionBean();
        }
        return purchaseTransactionBean;
    }

    public Integer createPurchaseTransaction(PurchaseTransactionBean purchaseTransactionBean)
    {
        Integer iNumOfRows = 0;
        if(purchaseTransactionBean!=null)
        {
            if(purchaseTransactionBean.getAdminId()!=null && !"".equalsIgnoreCase(purchaseTransactionBean.getAdminId())
                    && purchaseTransactionBean.getEventId()!=null && !"".equalsIgnoreCase(purchaseTransactionBean.getEventId()))
            {
                PurchaseTransactionData purchaseTransactionData = new  PurchaseTransactionData();
                iNumOfRows = purchaseTransactionData.insertPurchaseTransaction(purchaseTransactionBean);
            }
        }
        return iNumOfRows;
    }

    public Integer modifyPurchaseTransaction(PurchaseTransactionBean purchaseTransactionBean)
    {
        Integer iNumOfRows = 0;
        if(purchaseTransactionBean!=null)
        {
            PurchaseTransactionData purchaseTransactionData = new  PurchaseTransactionData();
            iNumOfRows = purchaseTransactionData.updatePurchaseTransaction(purchaseTransactionBean);
        }
        return iNumOfRows;
    }
}
