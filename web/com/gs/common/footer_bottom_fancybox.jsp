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
            <span style="padding:5px;text-align:right;font-size:75%; color:#37291C; margin : 0px; clear:both ">
                &copy<%=sCopyrightYear%> - <%=sCopyrightCompany%>
            </span>
        </div>
        <div class="row">
            &nbsp;
        </div>
    </div>
</div>