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
                        <!--<a id="footer_about" class="bold_text" href="/web/com/gs/common/about.jsp">About Callseat.com</a><br> -->
                        <a id="footer_contact" class="bold_text nav_link" href="/web/com/gs/common/contact.jsp">Contact</a>
                    </ul>
                </div>
            </div>
        </div>
        <div class="row">
            &nbsp;
        </div>
        <div class="row" style="margin: auto;text-align:right;">
            <a id="copyright_company" target="_blank" href="http://www.smarasoft.com" style="text-decoration: none;" class="nav_link">
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