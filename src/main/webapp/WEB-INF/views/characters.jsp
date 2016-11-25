<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<style>
.pagingItem {
	font-weight: normal;
	text-decoration: none;
	color: #747474;
	margin: 0 2px;
	padding: 0 2px;
	background-color: #eeeeee;
	border: 1px solid #bababa;
	font-size: 0.9em;
	line-height: 1.5em;
}

.pagingItemCurrent {
	padding: 0 2px;
	margin: 0 2px;
	font-weight: normal;
	color: #FFFFFF;
	background-color: #bfbfbf;
	border: 1px solid #bfbfbf;
	font-size: 0.9em;
}

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
<body>
	<h2>Characters</h2>

	<%@ include file="../jsp/includes.jsp"%>
	<%@ taglib prefix="tg" tagdir="/WEB-INF/tags"%>
	<%-- // use our pagedListHolder --%>
	<jsp:useBean id="pagedListHolder" scope="request"
		type="org.springframework.beans.support.PagedListHolder" />
	<%-- // create link for pages, "~" will be replaced 
   later on with the proper page number --%>
	<c:url value="/characters" var="pagedLink">
		<c:param name="action" value="list" />
		<c:param name="p" value="~" />
	</c:url>
	<%-- // load our paging tag, pass pagedListHolder and the link --%>
	<%-- // show only current page worth of data --%>
	<table width="200px" border="1" id="t01">
		<tr>
			<th>Nome</th>
			<th>Descrição</th>
			<th>Ultima Atualização</th>
		</tr>
		<c:forEach items="${pagedListHolder.pageList}" var="listValue">
			<tr>
				<td><a href="/characters/${listValue.id}">${listValue.name}</a></td>
				<td width=1000>${listValue.description}</td>
				<c:set var="date" value="${listValue.lastUpdate}" />
				<td><fmt:formatDate pattern="dd-MM-yyyy" value="${date}" /></td>
			</tr>
		</c:forEach>
	</table>
	<%-- // load our paging tag, pass pagedListHolder and the link --%>
	<div id="divPaging"
		style="float: left; position: relative; left: +40%;">
		<tg:paging pagedListHolder="${pagedListHolder}"
			pagedLink="${pagedLink}" />
	</div>
</body>
</html>