<%
com.gs.common.Configuration applicationConfig = com.gs.common.Configuration.getInstance(com.gs.common.Constants.APPLICATION_PROP);

String sCopyrightYear = applicationConfig.get("copyright_year");
String sCopyrightCompany = applicationConfig.get("copyright_company");
%>
<div style="background-color: RGBA(0,132,0,0.40);">
    <div class="blank_scratch_area" >
        <div class="row">
            &nbsp;
        </div>
        <div class="row">
            <div class="offset1 span6">
                <div id="footer_nav">
                        <a id="footer_about" class="bold_text" href="/web/com/gs/common/about.jsp">About Us</a><br>
                        <a id="footer_contact" class="bold_text nav_link" href="/web/com/gs/common/contact.jsp">Contact</a>
                    </ul>
                </div>
            </div>
        </div>
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

</html>