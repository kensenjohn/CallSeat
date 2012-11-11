<%
com.gs.common.Configuration analyticsConfig = com.gs.common.Configuration.getInstance(com.gs.common.Constants.ANALYTICS_PROP);

String sGoogleTrackId = com.gs.common.ParseUtil.checkNull(analyticsConfig.get(com.gs.common.Constants.ANALYTICS_KEYS.GOOGLE_TRACKING_ID.getKey()));

if(!"".equals(sGoogleTrackId))
{
%>
	
	<script type="text/javascript">
	
		var varGoogleTrackId = '<%=sGoogleTrackId%>'
	  var _gaq = _gaq || [];
	  _gaq.push(['_setAccount', varGoogleTrackId]);
	  _gaq.push(['_trackPageview']);
	
	  (function() {
	    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
	    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
	    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
	  })();
	
	</script>
<%
}
%>
