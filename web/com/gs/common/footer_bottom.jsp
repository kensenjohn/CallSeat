<%
com.gs.common.Configuration applicationConfig = com.gs.common.Configuration.getInstance(com.gs.common.Constants.APPLICATION_PROP);

String sCopyrightYear = applicationConfig.get("copyright_year");
String sCopyrightCompany = applicationConfig.get("copyright_company");
%>
<div class="site_theme_footer_color">
    <div class="blank_scratch_area" >
        <div class="row">
            &nbsp;
        </div>
        <div class="row">
            <div class="offset1 span6">
                <div id="footer_nav">
                        <a id="footer_contact" class="bold_text nav_link links" href="/web/com/gs/common/contact.jsp">Contact</a>   <br>
                        <a id="footer_blog" class="bold_text nav_link links" href="http://blog.callseat.com" target="_blank">Blog</a>   <br>
                        <a id="footer_pricing_plan" class="bold_text nav_link links" href="/web/com/gs/common/pricing_plan_display.jsp">Pricing Plan</a>  <br>
                        <a id="footer_privacy" class="bold_text nav_link links" href="/web/com/gs/common/privacy.jsp">Privacy</a>
                </div>
            </div>
        </div>
        <div class="row">
            &nbsp;
        </div>
        <div class="row" style="margin: auto;text-align:right;">
            <a id="copyright_company" target="_blank" href="http://www.smarasoft.com" style="text-decoration: none;" class="nav_link links">
                   <span style="padding:5px;text-align:right; margin : 0px; color: #fdfff7 ;clear:both ;text-decoration: none;">
                       &copy<%=sCopyrightYear%> <%=sCopyrightCompany%>
                    </span>
            </a>
        </div>
        <div class="row">
            &nbsp;
        </div>
    </div>
</div>

</html>