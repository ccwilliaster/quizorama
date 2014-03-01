<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="./css/bootstrap.css" rel="stylesheet">
	<title>New Message</title>
</head>
<body>
		<!-- TODO: check if there's an error attribute on request, display? -->
		<!-- display Quiz-generated HTML, submit results with 'Next' button -->		
		<div class="container">
			<form action="NewMessageServlet" method="post">
				<%= request.getAttribute("html")  %>
				<div class="row">
					<br>
					<input class="btn btn-default" type="submit" value="Submit">
				</div>
	 		</form>
		</div>
</body>
</html>