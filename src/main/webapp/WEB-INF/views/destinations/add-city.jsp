<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<c:set value="${contextPath}/resources" var="resourcesPath" />
<jsp:include page="../_fragments/page_template_before_main.jsp"/>


    <h3>Создание нового логистического узла</h3>
    <form method="POST" action="${contextPath}/destinations/add-city">
        <input type="text" name="name" placeholder="Название города" required>
        <input type="submit" class="btn btn-warning" value="Сохранить">
        <a class="btn btn-secondary" href="${contextPath}/destinations" role="button">Вернуться</a>
    </form>


<jsp:include page="../_fragments/page_template_after_main.jsp"/>
