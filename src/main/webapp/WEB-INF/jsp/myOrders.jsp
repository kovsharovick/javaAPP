<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Мои заказы — SWAGAPLEX</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>

<%@ include file="nav.jsp" %>

<main>
  <div class="wrapper">
    <div class="page-header">
      <h1 class="page-title">МОИ <span>ЗАКАЗЫ</span></h1>
      <p class="page-subtitle">История покупок и активные бронирования</p>
    </div>

    <c:choose>
      <c:when test="${empty orders}">
        <div class="empty-state">
          <div class="empty-icon">🎟️</div>
          <h3>Заказов пока нет</h3>
          <p style="margin-bottom:24px">Выбери фильм и купи билет!</p>
          <a href="${pageContext.request.contextPath}/films" class="btn btn-primary">Перейти к фильмам</a>
        </div>
      </c:when>
      <c:otherwise>
        <c:forEach var="order" items="${orders}">
          <div class="order-card">
            <div>
              <div class="order-id">ЗАКАЗ #${order.id}</div>
              <div class="order-amount">${order.amount} ₽</div>
              <div class="order-date">${order.dateTime.toString().replace('T', ' ').substring(0, 19)}</div>
            </div>
            <div style="display:flex;flex-direction:column;align-items:flex-end;gap:12px">
              <c:choose>
                <c:when test="${order.orderStatus == 'WAIT_PAYMENT'}">
                  <span class="status-badge status-wait-payment">⏳ Ожидает оплаты</span>
                </c:when>
                <c:when test="${order.orderStatus == 'COMPLETED'}">
                  <span class="status-badge status-completed">✓ Оплачен</span>
                </c:when>
                <c:when test="${order.orderStatus == 'CANCELED'}">
                  <span class="status-badge status-canceled">✗ Отменён</span>
                </c:when>
              </c:choose>

              <div style="display:flex;gap:8px">
                <c:if test="${order.orderStatus == 'WAIT_PAYMENT'}">
                  <form method="post" action="${pageContext.request.contextPath}/pay" style="display:inline">
                    <input type="hidden" name="orderId" value="${order.id}">
                    <button type="submit" class="btn btn-primary btn-sm">Оплатить</button>
                  </form>
                  <form method="post" action="${pageContext.request.contextPath}/cancelOrder" style="display:inline">
                    <input type="hidden" name="orderId" value="${order.id}">
                    <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Отменить заказ?')">Отменить</button>
                  </form>
                </c:if>
              </div>
            </div>
          </div>
        </c:forEach>
      </c:otherwise>
    </c:choose>
  </div>
</main>

<footer>
  <div class="wrapper"><div class="footer-logo">SWAGAPLEX</div><p>© 2026 Swagaplex</p></div>
</footer>

</body>
</html>
