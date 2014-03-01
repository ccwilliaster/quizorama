<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!-- TODO: Make the create new account and text vary based on who sent us to this page -->
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create Account</title>
</head>
<body>
<h1>Create New Account</h1>
<br><br>
Please enter proposed username and password.

<form action="LoginServlet" method="post">
	User Name: <input name="userName">
	<br><br>
	Password: <input name="password">
	<br><br>
	<button>Create Login</button>
	<input name="origin" type="hidden" value="CreateAccount" />
</form>

</body>
</html>