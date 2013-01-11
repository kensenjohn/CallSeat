package com.gs.manager.event;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.gs.bean.CheckoutBean;
import com.gs.bean.PurchaseTransactionBean;
import com.gs.bean.TaxBean;
import com.gs.common.ParseUtil;
import com.gs.manager.TaxManager;
import org.json.JSONArray;
import org.json.JSONException;

import com.gs.bean.PricingGroupBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventPricingGroupManager {

    Logger appLogging = LoggerFactory.getLogger("AppLogging");

	public ArrayList<PricingGroupBean> getPricingGroups() {

		PricingGroupData pricingGroupData = new PricingGroupData();

		ArrayList<PricingGroupBean> arrPricingGroupBean = pricingGroupData
				.getAllPricingGroups();

		return arrPricingGroupBean;
	}

	public PricingGroupBean getPricingGroups(String sPricingGroupId) {
		PricingGroupBean pricingGroupBean = new PricingGroupBean();
		if (sPricingGroupId != null && !"".equalsIgnoreCase(sPricingGroupId)) {
			PricingGroupData pricingGroupData = new PricingGroupData();

			pricingGroupBean = pricingGroupData
					.getPricingGroup(sPricingGroupId);
		}

		return pricingGroupBean;
	}

	public JSONArray getPricingGroupJsonArray(
			ArrayList<PricingGroupBean> arrPricingGroupBean) {
		JSONArray jsonPricingGroupArray = new JSONArray();
		try {

			int numOfRows = 0;
			if (arrPricingGroupBean != null && !arrPricingGroupBean.isEmpty()) {
				int iIndex = 0;
				for (PricingGroupBean pricingGroupBean : arrPricingGroupBean) {
					jsonPricingGroupArray
							.put(iIndex, pricingGroupBean.toJson());
					iIndex++;
				}
				numOfRows++;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonPricingGroupArray;
	}

    public CheckoutBean getCheckoutBean(String sAdminId, String sEventId)
    {
        CheckoutBean checkoutBean = new CheckoutBean();
        if(sAdminId!=null && !"".equalsIgnoreCase(sAdminId) && sEventId!=null && !"".equalsIgnoreCase(sEventId))
        {
            PurchaseTransactionBean purchaseTransactionBean = new PurchaseTransactionBean();
            purchaseTransactionBean.setAdminId(sAdminId);
            purchaseTransactionBean.setEventId(sEventId);

            PurchaseTransactionManager purchaseTransactionManager = new PurchaseTransactionManager();
            PurchaseTransactionBean purchaseResponseTransactionBean = purchaseTransactionManager.getPurchaseTransactionByEventAdmin(purchaseTransactionBean);

            if(purchaseResponseTransactionBean!=null && !"".equalsIgnoreCase(purchaseResponseTransactionBean.getPurchaseTransactionId()))
            {
                EventPricingGroupManager eventPricingManager = new EventPricingGroupManager();
                PricingGroupBean pricingGroupBean = eventPricingManager.getPricingGroups(purchaseResponseTransactionBean.getPriceGroupId());

                if(pricingGroupBean!=null)
                {
                    String sItemName = ParseUtil.iToS(pricingGroupBean.getMaxMinutes()) + " minutes and " + ParseUtil.iToS(pricingGroupBean.getSmsCount()) + " texts";
                    checkoutBean.setItemName(sItemName);

                    Double dItemPrice = pricingGroupBean.getPrice();
                    checkoutBean.setItemPrice(dItemPrice);

                    if(dItemPrice>0)
                    {
                        DecimalFormat decimalFormat = new DecimalFormat("##.00");

                        BigDecimal bdItemPrice = new BigDecimal(Double.toString(dItemPrice));
                        bdItemPrice.setScale(2, BigDecimal.ROUND_HALF_UP);

                        BigDecimal bdDiscountAmount = new BigDecimal("0.00");
                        BigDecimal bdDiscountPercent = new BigDecimal("0.00");
                        Double dDiscountPercentage = 0.0;
                        Double dDiscountAmount = 0.0;
                        if(dDiscountPercentage>0)
                        {
                            dDiscountPercentage = (dDiscountPercentage/100);
                            bdDiscountPercent = new BigDecimal(Double.toString(dDiscountPercentage));
                            bdDiscountAmount = bdItemPrice.multiply(bdDiscountPercent);


                            checkoutBean.setFormattedDiscountPercentage(decimalFormat.format(new BigDecimal(Double.toString(dDiscountPercentage*100))));
                            checkoutBean.setDiscountPercentage( (dDiscountPercentage*100) );
                        }
                        else
                        {

                        }

                        BigDecimal bdSubTotal = bdItemPrice.subtract(bdDiscountAmount);
                        bdSubTotal.setScale(2,BigDecimal.ROUND_HALF_UP);



                        String sState =  ParseUtil.checkNull(purchaseResponseTransactionBean.getState());
                        String sCountry = ParseUtil.checkNull(purchaseResponseTransactionBean.getCountry());

                        appLogging.info("State - " + sState + " Country = " + sCountry );
                        TaxManager taxManager = new TaxManager();
                        TaxBean taxBean =taxManager.getSalesTax(sState,sCountry);

                        appLogging.info(" Tax bean = " + taxBean );

                        BigDecimal bdTaxAmount = new BigDecimal("0.00");
                        BigDecimal bdTaxPercentage = new BigDecimal("0.00");
                        if(taxBean!=null && taxBean.getStateTaxId()!=null && !"".equalsIgnoreCase(taxBean.getStateTaxId()))
                        {

                            Double dTaxPercentage = taxBean.getTaxPercentage();
                            if(dTaxPercentage>0)
                            {
                                dTaxPercentage = (dTaxPercentage/100);
                                bdTaxPercentage = new BigDecimal(Double.toString(dTaxPercentage));
                                bdTaxAmount = bdSubTotal.multiply(bdTaxPercentage);
                            }
                            checkoutBean.setFormattedTaxPercentage(decimalFormat.format(new BigDecimal(Double.toString(dTaxPercentage*100))));
                            checkoutBean.setTaxPercentage( (dTaxPercentage*100) );
                        }
                        appLogging.info(" Tax amount = " + bdTaxAmount );
                        BigDecimal bdGrandTotal = bdSubTotal.add(bdTaxAmount);
                        bdGrandTotal.setScale(2,BigDecimal.ROUND_HALF_UP);



                        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.US);
                        if("USA".equalsIgnoreCase(purchaseResponseTransactionBean.getCountry()))
                        {
                            numberFormat = NumberFormat.getCurrencyInstance(Locale.US);
                        }
                        checkoutBean.setFormattedItemPrice(numberFormat.format(bdItemPrice));
                        checkoutBean.setFormattedDiscountAmount(numberFormat.format(bdDiscountAmount));
                        checkoutBean.setFormattedBeforeTaxTotal(numberFormat.format(bdSubTotal));
                        checkoutBean.setFormattedTaxAmount(numberFormat.format(bdTaxAmount));
                        checkoutBean.setFormattedGrandTotal(numberFormat.format(bdGrandTotal));

                        // strip out the leading $ sign/
                        checkoutBean.setGrandTotal( bdGrandTotal.doubleValue() );

                    }
                }
            }
        }

        return checkoutBean;
    }
}
