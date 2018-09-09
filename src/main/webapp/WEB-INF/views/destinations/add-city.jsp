<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set value="${pageContext.request.contextPath}" var="contextPath" />

<!DOCTYPE >
<html>

<jsp:include page="../_fragments/head.jsp">
    <jsp:param name="title" value="LogiWeb"/>
</jsp:include>

<body>

<div class="content-wrapper">

    <jsp:include page="../_fragments/header.jsp"/>

    <div class="row-wrapper">

        <jsp:include page="../_fragments/navigation.jsp"/>

        <main>
            <a href="/destinations">Назад к списку</a>
            <h3>Создание нового логистического узла</h3>
            <form method="POST" action="${contextPath}/destinations/add-city">
                <input type="text" name="name" placeholder="Название города" required>
                <input type="submit" value="Сохранить">
            </form>
        </main>

    </div>

</div>

<jsp:include page="../_fragments/footer.jsp"/>

</body>
</html>
