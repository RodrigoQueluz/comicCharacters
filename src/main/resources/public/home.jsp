<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Dados de Acesso</title>
</head>
<body>
	<div>
		<label>Dados de acesso</label> 
	</div>
	<form action="/avenuep/script"
		method="post">
		<div>
			<label>Private Key</label> <input name="private_key" type="text" />
		</div>
		<div>
			<label>Public Key</label> <input name="public_key" type="text" />
		</div>
		<button type="submit">Acessar</button>
	</form>
</body>
</html>