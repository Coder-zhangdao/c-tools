<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@
page import="com.bixuebihui.generated.tablegen.business.T_metacolumnManager"%><%@
page import="com.bixuebihui.generated.tablegen.web.T_metacolumnWebUI"%><%
String tableid="t_metacolumn";
	T_metacolumnWebUI c = new T_metacolumnWebUI();

	c.setService(new T_metacolumnManager());
	c.setId(tableid);
	c.setSuccessView("welcome");
	if(null==c.handleRequestInternal(request, response)){ return;}
%>
<html>
<head>
<script type="text/javascript">
function onInvokeAction(id, code) {
	$('#ac').val(code);
	if('edit'==code){
   $('#editable').val($('#editable').val()=="true"?"false":"true");	}
	createHiddenInputFieldsForLimitAndSubmit(id);
}
function onInvokeAction1(id) {
    setExportToLimit(id, '');
    var parameterString = createParameterStringForLimit(id);
    var url = 't_metacolumn_list.jsp?decorator=ajax&ajax=true&' + parameterString;
   // alert(url);
    $.get(url, function(data) {
        $("#<%=tableid%>").html(data)
    });
}

function onInvokeExportAction(id) {
	var parameterString = createParameterStringForLimit(id);
	location.href = 't_metacolumn_list.jsp?decorator=none&ajax=true&' + parameterString;
}
$(document).ready(function() {$('#selectAllChkBox').click(function(){ShiftCheck('chk', this.checked);})});</script>
</head>
<body>	<form id='<%=tableid %>Form' name="t_metacolumnForm" action="t_metacolumn_list.jsp">
<%=request.getAttribute(tableid)%>
	<input id="editable" name="editable" type="hidden" value="<%=request.getParameter("editable") %>" />
	<input id="ac" name="ac" type="hidden" value="" />
</form>
</body>
</html>
