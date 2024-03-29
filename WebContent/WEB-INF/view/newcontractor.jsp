<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Создание нового контрагента</title>
</head>
<body>
     <jsp:include page="_menu.jsp"></jsp:include>
<br>

<div class="container">
<h3>Создание нового контрагента</h3>
		<div class="col-xl-1"></div>		
		<div class="col-xl-10">
			<table border = "1">
			<tr><th>Контрагент</th>  <th> <input class="form-control" id="myInput" type="text" placeholder="Поиск совпадений в списке документов.."> </th> </tr>
			 <tbody id="myTable">
			<c:forEach var="contractor2" items="${contractors}">
			 <tr>
				<td>${contractor2}</td>
			</tr>
			</c:forEach>
			 </tbody>
			</table>
			<br>
			<br>
			<form method="POST" accept-charset="UTF-8" action="${pageContext.request.contextPath}/NewContractorServlet">
			<input type="hidden" value="${contractor.id}" name="id" />
			<label>Имя нового контрагента</label><br>
			<input name="name" value="${contractor.name}" /><br><br>
			<label>Комментарий</label><br>
			<input name="comment" value="${contractor.comment}" /><br><br>
			<input type="submit" class="btn btn-dark" value="Создать" />
			</form>
<%-- 			<form method="GET" accept-charset="UTF-8" action="${pageContext.request.contextPath}/EmployeeTaskServlet">
			<input type="submit" class="btn btn-dark" value="Назад">  
			</form> --%>
			<input type="submit" class="btn-sm btn-dark" value="Назад" onCLick="history.back()"> 
			<br>
			<br>
			<form method="GET" accept-charset="UTF-8" action="${pageContext.request.contextPath}/">
			<input type="submit" class="btn-sm btn-dark" value="На главную страницу">  
			</form> 		
		</div>
</div>		 
<br>
<script>
$(document).ready(function(){
  $("#myInput").on("keyup", function() {
    var value = $(this).val().toLowerCase();
    $("#myTable tr").filter(function() {
      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
    });
  });
});
</script>

<br>
</body>
</html>