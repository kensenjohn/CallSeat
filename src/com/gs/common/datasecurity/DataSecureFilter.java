package com.gs.common.datasecurity;

import com.gs.bean.DataSecurityRequestBean;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.ParseUtil;
import com.gs.common.exception.ExceptionHandler;
import com.gs.common.exception.PropertyFileException;
import org.owasp.validator.html.Policy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

public class DataSecureFilter implements Filter {

	private static final Logger appLogging = LoggerFactory.getLogger("AppLogging");
	Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);
	private static final Logger breadCrumbLogging = LoggerFactory.getLogger("BreadcrumbLogging");

	private String sSecurityPolicy = "";
	private boolean isSecureFilterEnabled = false;
	private Policy owsapiPolicy = null;
	private boolean trackBreadcrumb = false;
    private DataSecurityRequestBean dataSecurityRequestBean = new DataSecurityRequestBean();

	@Override
	public void destroy() {
		appLogging.info("Killing the Security Filter");
		owsapiPolicy = null;
        dataSecurityRequestBean = null;

	}

	@Override
	public void doFilter(ServletRequest servReq, ServletResponse servResp,
			FilterChain filterChain) throws IOException, ServletException {

		if(trackBreadcrumb)
		{
			HttpServletRequest httpRequest = (HttpServletRequest)servReq;
			String sReferer = httpRequest.getHeader("referer");
			StringBuffer sDestination = httpRequest.getRequestURL();
			
			String sUserId = "";
			String sJSessionId = "";
			
			Cookie[] cookies = httpRequest.getCookies();

			if(cookies!=null)
			{
				for(int cookieCount = 0; cookieCount < cookies.length; cookieCount++) 
				{ 
					Cookie cookie1 = cookies[cookieCount];
			        if (Constants.COOKIE_APP_USERID.equals(cookie1.getName())) {
			        	sUserId = cookie1.getValue();
			        }
			        if ("JSESSIONID".equals(cookie1.getName())) {
			        	sJSessionId = cookie1.getValue();
			        }
				}
			}
			
			breadCrumbLogging.info("\"ui\":\""+sUserId+"\",\"sess\":\""+sJSessionId+"\"\"src\":\""+sReferer+"\",\"dest\":\""+sDestination+"\"");
		}

        boolean isUnsafeParametersDetected = true;
        Map<String, String[]> reqParams = servReq.getParameterMap();
		if( dataSecurityRequestBean!=null) {
            if( reqParams!=null && !reqParams.isEmpty() ) {
                try {
                    if( DataSecurityChecker.isDataSecurityRequestBeanOld(dataSecurityRequestBean) ) {

                        appLogging.info("The DataSecurityRequestBean was reloaded");
                        appLogging.debug("After reload : "  + dataSecurityRequestBean);
                        dataSecurityRequestBean = DataSecurityChecker.loadDataSecurityRequestBean();
                    }
                    if( dataSecurityRequestBean.isFilterEnabled() ) {
                        for( Map.Entry<String,String[]> mapRequestParams : reqParams.entrySet() ) {
                            String[] sRequestValueArray = mapRequestParams.getValue();
                            if( sRequestValueArray!=null && sRequestValueArray.length > 0) {
                                for( String sInputData : sRequestValueArray ) {
                                    isUnsafeParametersDetected = DataSecurityChecker.isStringSafe(sInputData,dataSecurityRequestBean);
                                    if(isUnsafeParametersDetected) {
                                        break;
                                    }
                                }
                            }
                        }
                    } else {
                        isUnsafeParametersDetected = false; //
                        appLogging.info("Warning : Security Filter has been disabled.");
                    }
                } catch (PropertyFileException e) {
                    dataSecurityRequestBean.setFilterEnabled(true);
                    appLogging.error("PropertyFileException - " + e.getMessage() + " - " + ExceptionHandler.getStackTrace(e));
                } catch (Exception e) {
                    isUnsafeParametersDetected = true;
                    dataSecurityRequestBean.setFilterEnabled(true);
                    appLogging.error("Exception - " + e.getMessage() + " - " + ExceptionHandler.getStackTrace(e));
                }
            } else {
                isUnsafeParametersDetected = false;   // There are no parameters, so no exception was/
            }

        }

        if(isUnsafeParametersDetected) {
            servReq.setAttribute("INSECURE_PARAMS_ERROR","true");
        }
		filterChain.doFilter(servReq, servResp);
	}

	ServletContext servletContext = null;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		servletContext = filterConfig.getServletContext();
        appLogging.info("Initializing the DataSecurityRequestBean");
        try {
            dataSecurityRequestBean = DataSecurityChecker.bootstrapDataSecurityRequestBean();
            appLogging.info("After Bootstrapping : " + dataSecurityRequestBean);
        } catch (PropertyFileException e) {
            dataSecurityRequestBean.setFilterEnabled(true);
            appLogging.error(e.getMessage() + " - " + ExceptionHandler.getStackTrace(e));
        }

        trackBreadcrumb = ParseUtil.sTob(applicationConfig.get("track_user_breadcrumb"));
	}
}
