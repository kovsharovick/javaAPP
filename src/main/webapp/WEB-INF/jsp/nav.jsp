<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<nav>
  <div class="wrapper nav-inner">
    <a href="${pageContext.request.contextPath}/" class="nav-logo">
        SWAGAPLEX
    </a>
    <ul class="nav-links">
      <li><a href="${pageContext.request.contextPath}/films">Фильмы</a></li>
      <li><a href="${pageContext.request.contextPath}/sessions">Сеансы</a></li>
      <c:if test="${sessionScope.currentUser != null}">
        <li><a href="${pageContext.request.contextPath}/myOrders">Заказы</a></li>
        <li><a href="${pageContext.request.contextPath}/myTickets">Билеты</a></li>
        <c:if test="${sessionScope.currentUser.admin}">
          <li><a href="${pageContext.request.contextPath}/admin/films">Админ</a></li>
        </c:if>
      </c:if>
    </ul>
    <div class="nav-user">
      <c:choose>
        <c:when test="${sessionScope.currentUser != null}">
          <span class="username">${sessionScope.currentUser.name}</span>
          <c:if test="${sessionScope.currentUser.admin}">
            <span class="badge-admin">admin</span>
          </c:if>
          <a href="${pageContext.request.contextPath}/profile" class="btn btn-ghost btn-sm">Профиль</a>
          <a href="${pageContext.request.contextPath}/logout" class="btn btn-secondary btn-sm">Выйти</a>
        </c:when>
        <c:otherwise>
          <a href="${pageContext.request.contextPath}/login" class="btn btn-ghost btn-sm">Войти</a>
          <a href="${pageContext.request.contextPath}/register" class="btn btn-primary btn-sm">Регистрация</a>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</nav>
