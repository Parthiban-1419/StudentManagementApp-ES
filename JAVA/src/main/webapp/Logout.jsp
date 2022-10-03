<%@ page import="org.json.simple.JSONObject" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Insert title here</title>
    <%@page import="com.es.LogManager"%>
    <%@ page import="java.util.Date" %>

    <%
        LogManager log = new LogManager("crud-log");
        JSONObject jLog = new JSONObject();
        jLog.put("userId", request.getRemoteUser());
        jLog.put("operation", "LOGOUT");
        jLog.put("studentId", "");
        jLog.put("time", new Date().getTime());
        log.putLog(jLog);
        request.getSession().invalidate();
    %>
</head>
<body>
    <script>
		window.location.href="secured/index.jsp";
	</script>
</body>
</html>