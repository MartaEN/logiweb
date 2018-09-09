<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<c:set value="${contextPath}/resources" var="resPath" />

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
            <main>Здесь что-то будет</main>
        </div>

    </div>

    <jsp:include page="../_fragments/footer.jsp"/>

</body>
</html>
