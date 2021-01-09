<%@ page language="java" pageEncoding="UTF-8" %>
<%@
        page import="com.bixuebihui.BeanFactory" %>
<%@
        page import="com.bixuebihui.util.Util" %>
<%@
        page import="com.bixuebihui.jmesa.BasicListService" %>
<%@
        page import="com.bixuebihui.jmesa.BasicWebUI" %>
<%@ page import="com.bixuebihui.util.ParameterUtils" %>
<%
    String tableid = request.getParameter("id");
    String[] params = ParameterUtils.getArray(request, "params");

    if (tableid == null) {
        Util.showHtmlAlert(out, "没有参数指定的视图");
        return;
    }
    BasicWebUI c = (BasicWebUI) BeanFactory.createObjectById(tableid);
    if (c == null) {
        Util.showHtmlAlert(out, "参数有误，请检查配置文件和参数。");
        return;
    }
    c.setId(tableid);
    c.setSuccessView("welcome");
    ((BasicListService) c.getListService()).setSqlParams(params);
    if (null == c.handleRequestInternal(request, response)) {
        return;
    }

%>
<html>
<head>
    <script type="text/javascript">
        function onInvokeAction(id, code) {

            $('#ac').val(code);
            if ('edit' == code) {
                $('#editable').val($('#editable').val() == "true" ? "false" : "true");
            }
            createHiddenInputFieldsForLimitAndSubmit(id);
        }

        function onInvokeAction1(id) {
            setExportToLimit(id, '');
            var parameterString = createParameterStringForLimit(id);
            var url = '<%=request.getRequestURI() %>?decorator=ajax&ajax=true&' + parameterString;
            $.get(url, function (data) {
                $("#<%=tableid%>").html(data)
            });
        }

        function onInvokeExportAction(id) {
            var parameterString = createParameterStringForLimit(id);
            location.href = '<%=request.getRequestURI() %>?id=<%=tableid %>&decorator=none&ajax=true&' + parameterString;
        }

        $(document).ready(function () {
            $('#selectAllChkBox').click(function () {
                ShiftCheck('chk', this.checked);
            });

        });
    </script>

</head>
<body>

<form id='<%=tableid %>Form' name="viewForm" action="<%=request.getRequestURI() %>">
    <%=request.getAttribute(tableid)%>
    <input id="editable" name="editable" type="hidden" value="<%=request.getParameter("editable") %>"/>
    <input id="ac" name="ac" type="hidden" value=""/>
    <input id="id" name="id" type="hidden" value="<%=tableid %>"/>
</form>
</body>
</html>
