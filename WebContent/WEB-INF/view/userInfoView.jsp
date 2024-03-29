<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html>
   <head>
      <meta charset="UTF-8">
   
      <title>Документы пользователя</title>
   </head>
<body>
<jsp:include page="_menu.jsp"></jsp:include>
<script type="text/javascript">
    <%@include file="../../javascript/changeView.js"%>
</script>

<div class="container">  
   <div class="row">
   	 <div class="col-xl-1"></div>
     <div class="col-xl-4">
     <h5>Документы пользователя ${loginedUser} ${loginedUserSecond} </h5>
     </div>
      <div class="col-xl-6">
	 <input class="form-control" id="myInput" type="text" placeholder="Поиск совпадений в списке документов..">
	 </div>
	 <div class="col-xl-1"></div>
  </div>
</div>
<br>
 
<a href="#" onclick="change('filter')" class="btn-sm btn-dark" role="button" >Доп фильтры</a>
<br/>
<div class="container" style="display:none" id="filter">
  <form method="GET" accept-charset="UTF-8"  action="${pageContext.request.contextPath}/UserInfoServlet?filter_docs=${filter_docs}">
    <div class="form-group">
      <label for="sel1"> Дополнительный фильтр:</label>
      <select class="form-control-mg" name="filter_docs">
        <option value="*">Без фильтрации</option>
        <option value="1" <c:if test="${filter_docs == '1'}"> selected </c:if> >Завершенные документы</option>
        <option value="0" <c:if test="${filter_docs == '0'}"> selected </c:if> >Незавершенные документы</option>
      </select>
      <button type="submit" class="btn-sm btn-dark">Применить фильтр</button>
    </div>
  </form>
</div>
    
<!-- 
<div class="container">
   <div class="dropdown">
    <button type="button" class="btn btn-dark dropdown-toggle" data-toggle="dropdown">
      Дополнительный фильтр
    </button>
    <div class="dropdown-menu">
      <a class="dropdown-item" href="#">Завершенные документы </a>
      <a class="dropdown-item" href="#">Link 2</a>
      <a class="dropdown-item" href="#">Link 3</a>
      <div class="dropdown-divider"></div>
      <a class="dropdown-item" href="#">Another link</a>
    </div>
  </div>
</div>

<c:if test="${filter_docs} == 1 "> selected </c:if> 
-->



<div class="container-fluid  px-3  margin: auto">
<div class="row justify-content-md-center"> <!-- делаем таблицу адаптивной table-responsive -->
<table border="1" class="table table-bordered table-hover table-responsive table-sm px-3">
<thead>
<tr  bgcolor="#C0C0C0" ><th>id</th><th>Номер договора</th><th>Тип</th><th>Контрагент</th><th>Название(Предмет)</th><!-- <th>Комментарии</th> --><th>Автор(Отв.)</th>
<th>Статус актуальности</th><th>Дата создания</th><th>Дата заключения</th><!-- <th>Закончен ли</th> --><!-- <th>Рек. дата</th> --><!-- <th>Получатели</th><th>Отправители</th> --><th>Текущий отдел</th>
<!-- <th>Дата реестр</th> --><!-- <th>ТРУ</th> --><!-- <th>Закон</th> --><!-- <th>Подразд.</th> --><th>Общая сумма</th><th>Сумма</th><th>Оплата</th><th>ИФО</th><!-- <th>Доп согл.</th><th>Сумма по доп согл.</th> -->
<th>Прикр.</th><th>Ред.</th><th>Отпр.</th>
</tr>
</thead>
<c:forEach var="doc" items="${docs}">
 <tbody id="myTable">
 <tr <c:if test="${doc.receiver_list[1]==null}"> class="bold" </c:if> <c:if test="${doc.status_finished==1}"> class="yellow" </c:if> >
 	<td><a href='<c:url value="/SendDocServlet?id=${doc.id}" />'> ${doc.id}</a></td>
 	<td>${doc.num}</td>
 	<td>${doc.id_type}</td>
	<td>${doc.id_contractor}</td>
    <td>${doc.name}</td>
<%--     <td>${doc.content}</td> --%>
    <td>${doc.creator_name} 
        ${doc.creator_second}</td>
    <td>${doc.urgency}</td>
    <td>${doc.date_cre}</td>
    <%-- <td>${doc.date_concluded} или ${doc.date_concluded.getDayOfMonth()}.${doc.date_concluded.getMonthValue()}.${doc.date_concluded.getYear()}</td> --%>
    <td>${doc.date_concluded.getDayOfMonth()}.${doc.date_concluded.getMonthValue()}.${doc.date_concluded.getYear()}</td>
<%--     <td>${doc.status_finished}</td> --%>
<%--     <td>${doc.rec_date}</td> --%>
<%--     <td >${doc.receiver_list}</td>
    <td>${doc.sender_list}</td> --%>
    <td>${doc.dep}</td>
    
<%-- 	<td>${doc.date_registry}</td> --%>
<%--     <td>${doc.tru}</td> --%>
<%--     <td>${doc.law}</td> --%>
<%--     <td>${doc.division}</td> --%>
    <td>${doc.price_total}</td>
    <td>${doc.price}</td>
    <td>
    <c:if test="${doc.paid==true}"> Оплачен </c:if>
    <c:if test="${doc.paid==false}"> Не оплачен</c:if>
    </td>
<%--     <td>${doc.ifo}</td> --%>
     <td>${doc.ifo_str}</td> 
    
<%--     <td>${doc.add_agr}</td>
    <td>${doc.price_add_agr}</td> --%>
    <td><c:if test="${empty doc.blob}"> Не Загружен</c:if>
    <c:if test="${doc.blob!=null}">Загружен</c:if></td>    
    <td><a href='<c:url value="/DocEditServlet?id=${doc.id}" />'  class="btn-sm btn-dark" role="button">Ред.</a> </td>
	<td><a href="<c:url value="/SendDocServlet?id=${doc.id}" />" class="btn-sm btn-dark" role="button">Отпр.</a></td>	
</tr>
</tbody>
</c:forEach>
</table>
</div>
</div>
<div class="container">
   <div class="row">
   	 <div class="col-xl-1"></div>
     <div class="col-xl-3">
     	<form method="POST" accept-charset="UTF-8" action="${pageContext.request.contextPath}/UserInfoServlet?path=${filepath}">
		<input type="submit" class="btn btn-dark" value="Выгрузить список документов в виде отчета" >
		<input type="text" name="filepath" placeholder="Путь выгрузки отчета"  title="Например D:\temp"/>
		</form> 
	 </div>
	<div class="col-xl-3">
	</div>		
	 <div class="col-xl-4">
	 <form method="GET" accept-charset="UTF-8" action="${pageContext.request.contextPath}/">
	 <input type="submit" class="btn-sm btn-dark" value="На главную страницу">  
	 </form> 
	 </div>
	 <div class="col-xl-1"></div>
  </div>  
</div>

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

&nbsp;
&nbsp;
 
   </body>
</html>