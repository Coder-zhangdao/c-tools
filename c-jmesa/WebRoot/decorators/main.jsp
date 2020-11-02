<%@ page import="com.bixuebihui.util.Config" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="sitemesh-decorator" prefix="decorator" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<title><decorator:title/>_<%=Config.getProperty("main.name") %></title>
		<link rel="stylesheet" type="text/css" href="../css/jmesa.css"></link>
		<script type="text/javascript" src="../js/jquery-1.3.2.min.js"></script>
		<script type="text/javascript" src="../js/jquery.bgiframe.pack.js"></script>
		<script type="text/javascript" src="../js/jquery.jmesa.min.js"></script>
		<script type="text/javascript" src="../js/jmesa.min.js"></script>
		<style type="text/css">.dd a {font-size: xx-small;color: #CC0000;}
			 a:visited {color: #3399CC;}
			 a:hover {color: #990000;}
			 a:active {color: #00CCCC;}
		</style>
		<decorator:head/>
	</head>

	<body>

		<div id="title" style="height:88px;background-image: url('../images/bk.gif');">
			<a href="../index.jsp">
				<img id="header" src="../images/logo.gif" alt="logo" />
			</a><img id="header" src="../images/title.gif" alt="myty" height="75" />
		</div>

		<div id="content">
			<decorator:body/>
		</div>

	</body>

</html>

