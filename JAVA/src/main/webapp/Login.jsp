<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<center>
		<form method='post' action='<%=response.encodeURL("j_security_check")%>'><br><br><br><br>
		
			<input class='textBox' type="text" name="j_username" placeholder="Log-in name"><br><br>
			<input class='textBox' type="password" name="j_password" placeholder="Password"><br><br>
			<button>Log in</button>
		</form>
		Don't have account? <a href="http://localhost:4200/add-users">sign up</a>
	</center>
	
	
</body>
</html>