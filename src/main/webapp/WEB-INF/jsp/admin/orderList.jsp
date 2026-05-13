<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <title>Заказы — Админ — CINEMAX</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>
<%@ include file="../nav.jsp" %>
<main>
  <div class="wrapper">
    <div class="admin-layout">
      <%@ include file="sidebar.jsp" %>
      <div>
        <h1 style="font-family:'Bebas Neue',cursive;font-size:42px;letter-spacing:3px;margin-bottom:24px">ЗАКАЗЫ</h1>
        <div style="background:var(--surface);border:1px solid var(--border);border-radius:var(--radius);overflow:hidden">
          <div class="table-wrap">
            <table>
              <thead><tr><th>ID</th><th>Пользователь</th><th>Сумма</th><th>Статус</th><th>Дата</th><th>Бронь до</th></tr></thead>
              <tbody>
                <c:forEach var="order" items="${orders}">
                  <tr>
                    <td class="text-mono" style="color:var(--text3)">${order.id}</td>
                    <td>${order.userId}</td>
                    <td class="text-mono text-accent">${order.amount} ₽</td>
                    <td>
                      <c:choose>
                        <c:when test="${order.orderStatus == 'WAIT_PAYMENT'}"><span class="status-badge status-wait-payment">Ожидает</span></c:when>
                        <c:when test="${order.orderStatus == 'COMPLETED'}"><span class="status-badge status-completed">Оплачен</span></c:when>
                        <c:when test="${order.orderStatus == 'CANCELED'}"><span class="status-badge status-canceled">Отменён</span></c:when>
                      </c:choose>
                    </td>
                    <td style="font-size:13px;color:var(--text2)">${order.dateTime}</td>
                    <td style="font-size:13px;color:var(--text3)">${order.reservedUntil}</td>
                  </tr>
                </c:forEach>
                <c:if test="${empty orders}"><tr><td colspan="6" style="text-align:center;padding:40px;color:var(--text3)">Заказов нет</td></tr></c:if>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</main>
</body>
</html>
