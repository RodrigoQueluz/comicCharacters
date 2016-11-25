<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style>
table {
	width: 100%;
}

table, th, td {
	border: 1px solid black;
	border-collapse: collapse;
}

th, td {
	padding: 5px;
	text-align: left;
}

table#t01 tr:nth-child(even) {
	background-color: #eee;
}

table#t01 tr:nth-child(odd) {
	background-color: #fff;
}

table#t01 th {
	background-color: black;
	color: white;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<title>Dados de Acesso</title>
</head>
<body>
	<form>
		<div style="padding: 115px">
			<div style="width: 30%; display: inline-table;">
				<img src="${comicCharacter.thumb}" class="img-thumbnail"
					alt="Cinque Terre" width="204" height="136">
			</div>
			<div style="width: 30%; display: inline-table;">
				<input style="width: 350px;" name="name" type="text"
					value="${comicCharacter.name}" /> <br>
				<br>
				<br>
				<textarea style="width: 350px;" rows="8" cols="50"
					name="description">${comicCharacter.description}</textarea>
			</div>
		</div>
		<br><br><br>
		<h2>Fascículos</h2>
		<br><br><br>
		<c:if test="${fn:length(comics)> 0}">
			<div>
				<table width="200px" border="1" id="t01">
				<tr>
					<td></td>
					<td>Titulo</td>
					<td>Número de Capa</td>
				</tr>
					<c:forEach items="${comics}" var="listValue">
						<tr>
							<td width="300px"><img src="${listValue.thumb}" class="img-thumbnail"
						alt="Cinque Terre" width="204" height="136"></td>
							<td width=1000><label>${listValue.title}</label></td>
							<td width=150><label>${listValue.issue}</label></td>
						</tr>
					</c:forEach>
				</table>
				<br><br><br>
			</div>
		</c:if>
	</form>
</body>
</html>