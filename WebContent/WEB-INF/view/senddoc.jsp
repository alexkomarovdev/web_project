<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Отправка документа ${id}</title>
</head>
<body>
<jsp:include page="_menu.jsp"></jsp:include>

<div class="container"> 
	<h3>Отправка документа ${id}. Выберите пользователя</h3>
		<div class="col-xl-1"></div>
		
		<div class="col-xl-10">
			<table border = "1">
			<tr><th>Номер</th><th>Имя</th><th>Фамилия</th></tr>
			
			<c:forEach var="user" items="${users}">
			 <tr>
			<td>${user.id}   </td>
			<td>${user.name} </td>
			<td>${user.second} </td>
			<td>    <form method="post" action='<c:url value="/SendDocServlet?id=${id}&id_user=${user.id}" />' style="display:inline;">
			        <input type="submit" class="btn-sm btn-dark" value="Отправить">
			    </form>
			</td>    
			</tr>
			</c:forEach>
			</table>
			<br>			
			<form method="GET" accept-charset="UTF-8" action="${pageContext.request.contextPath}/UserInfoServlet">
			<input type="submit" class="btn-sm btn-dark" value="Назад к документам">  
			</form>		
		</div>
</div>
<br><br>

<!-- 
<table border = "1">
<tr><th>Номер</th><th>Наименование отдела</th></tr>

<c:forEach var="department" items="${departments}">
 <tr>
<td>${department.id}   </td>
<td>${department.name} </td>
<td>    <form method="post" action='<c:url value="/SendDocServlet?id=${id}&id_dep=${department.id}" />' style="display:inline;">
        <input type="hidden" name="id" value="${department.id}">
        <input type="submit" class="btn btn-dark" value="Отправить">
    </form>
</td>    
</tr>
</c:forEach>
</table>

 -->
</body>
</html>