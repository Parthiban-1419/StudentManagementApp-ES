<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<%@page import="com.student.User"%>
	<%
		String user = request.getRemoteUser();
		User u = User.getObj();
		u.setUser(user);
		
		String site = new String("http://localhost:4200/StudentManagementApp/" + request.getRemoteUser());
        response.setStatus(response.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", site);
    %>
</body>
</html>