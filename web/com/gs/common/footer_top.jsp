<link rel="stylesheet" type="text/css" href="/web/js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
<script>
	!window.jQuery && document.write('<script src="/web/js/fancybox/jquery-1.4.3.min.js"><\/script>');
</script>
<script type="text/javascript" src="/web/js/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#login_name_display").fancybox({
			'width'				: '80%',
			'height'			: '80%',
			'autoScale'			: false,
			'transitionIn'		: 'none',
			'transitionOut'		: 'none',
			'type'				: 'iframe',
			'padding'			: 0,
			'margin'			: 0
		});
	 });
	
	function credentialSuccess(varFirstName,varSource)
	{
		//alert('credentialSuccess')
		$("#get_phone_num_div").hide();
		$("#login_name_display").text(varFirstName);
		$("#login_name_display").addClass("bold_text");
		

		//alert(varSource)
		if(varSource == 'phone_tab')
		{
			resetPhoneNumber();
		}
		
	}
	
</script>