<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>I got the token!</title>
</head>
<body>
	<p>Token info</p>
	<ul>
		<li>User Id: ${userId }</li>
		<li>Nick Name: ${nickName }</li>
		<li>Token Type: ${tokenType }</li>
		<li>Access Token: ${accessToken }</li>
		<li>Refresh Token: ${refreshToken }</li>
	</ul>
</body>
</html>