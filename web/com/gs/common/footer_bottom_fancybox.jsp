<%
com.gs.common.Configuration applicationConfig = com.gs.common.Configuration.getInstance(com.gs.common.Constants.APPLICATION_PROP);

String sCopyrightYear = applicationConfig.get("copyright_year");
String sCopyrightCompany = applicationConfig.get("copyright_company");
%>
<div>
    <div class="blank_scratch_area" >
        <div class="row">
            &nbsp;
        </div>
        <div class="row" style="margin: auto;text-align:right;">
            <div class="span11">
                <a id="copyright_company" target="_blank" href="http://www.smarasoft.com" style="text-decoration: none;">
                   <span style="padding:5px;text-align:right;font-size:75%; color:#37291C; margin : 0px; clear:both ;text-decoration: none;">
                       &copy<%=sCopyrightYear%> - <%=sCopyrightCompany%>
                    </span>
                </a>
            </div>
        </div>
        <div class="row">
            &nbsp;
        </div>
    </div>
</div>