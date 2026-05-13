<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Билеты — Админ — SWAGAPLEX</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>
<%@ include file="../nav.jsp" %>
<main>
    <div class="wrapper">
        <div class="admin-layout">
            <%@ include file="sidebar.jsp" %>
            <div>
                <h1 class="page-title" style="padding:0;border:none;font-size:42px">БИЛЕТЫ</h1>
                <div class="table-wrap">
                    <table>
                        <thead>
                            <tr><th>ID</th><th>Заказ</th><th>Сеанс</th><th>Место</th><th>Цена</th><th>Статус</th></tr>
                        </thead>
                        <tbody>
                            <c:forEach var="t" items="${tickets}">
                                <tr>
                                    <td class="text-mono">${t.id}</td>
                                    <td>${t.ordersId}</td>
                                    <td>${t.sessionId}</td>
                                    <td>${t.placeId}</td>
                                    <td class="text-accent">${t.price} ₽</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${t.ticketStatus == 'RESERVED'}"><span class="status-badge status-reserved">ЗАРЕЗЕРВИРОВАН</span></c:when>
                                            <c:when test="${t.ticketStatus == 'SOLD'}"><span class="status-badge status-sold">ПРОДАН</span></c:when>
                                            <c:when test="${t.ticketStatus == 'USED'}"><span class="status-badge status-used">ИСПОЛЬЗОВАН</span></c:when>
                                            <c:when test="${t.ticketStatus == 'CANCELED'}"><span class="status-badge status-canceled">ОТМЕНЁН</span></c:when>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</main>
<footer>
  <div class="wrapper">
    <div class="footer-logo">SWAGAPLEX</div>
    <p>© 2026 Swagaplex</p>
  </div>
</footer>
</body>
</html>