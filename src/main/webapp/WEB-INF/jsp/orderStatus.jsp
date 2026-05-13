<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Статус заказа — CINEMAX</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>

<%@ include file="nav.jsp" %>

<main>
  <div class="wrapper" style="padding-top:80px;padding-bottom:80px;text-align:center;max-width:500px">

    <c:choose>
      <c:when test="${not empty message}">
        <div style="font-size:72px;margin-bottom:16px">🎉</div>
        <h1 style="font-family:'Bebas Neue',cursive;font-size:52px;letter-spacing:4px;color:var(--success);margin-bottom:12px">
          Оплачено!
        </h1>
        <p style="color:var(--text2);font-size:16px;margin-bottom:32px">${message}</p>
        <div style="display:flex;gap:12px;justify-content:center">
          <a href="${pageContext.request.contextPath}/myTickets" class="btn btn-primary">Мои билеты</a>
          <a href="${pageContext.request.contextPath}/films" class="btn btn-ghost">Ещё фильмы</a>
        </div>
      </c:when>
      <c:otherwise>
        <div style="font-size:72px;margin-bottom:16px">❌</div>
        <h1 style="font-family:'Bebas Neue',cursive;font-size:52px;letter-spacing:4px;color:var(--danger);margin-bottom:12px">
          Ошибка
        </h1>
        <p style="color:var(--text2);font-size:16px;margin-bottom:32px">${error}</p>
        <a href="${pageContext.request.contextPath}/myOrders" class="btn btn-primary">Мои заказы</a>
      </c:otherwise>
    </c:choose>

  </div>
</main>

<footer>
  <div class="wrapper"><div class="footer-logo">CINEMAX</div><p>© 2025 CineMax</p></div>
</footer>

</body>
</html>
