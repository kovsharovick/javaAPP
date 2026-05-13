<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Мои билеты — CINEMAX</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>

<%@ include file="nav.jsp" %>

<main>
  <div class="wrapper">
    <div class="page-header">
      <h1 class="page-title">МОИ <span>БИЛЕТЫ</span></h1>
      <p class="page-subtitle">Все купленные и активные билеты</p>
    </div>

    <c:choose>
      <c:when test="${empty tickets}">
        <div class="empty-state">
          <div class="empty-icon">🎫</div>
          <h3>Билетов пока нет</h3>
          <p style="margin-bottom:24px">Купи билет на ближайший сеанс!</p>
          <a href="${pageContext.request.contextPath}/sessions" class="btn btn-primary">К сеансам</a>
        </div>
      </c:when>
      <c:otherwise>
        <div style="display:flex;flex-direction:column;gap:12px;padding:24px 0">
          <c:forEach var="ticket" items="${tickets}" varStatus="status">
            <div class="ticket-card">
              <div class="ticket-num">${status.count}</div>
              <div class="ticket-info">
                <div class="ticket-session">Сеанс #${ticket.sessionId}</div>
                <div class="ticket-seat">
                  <c:if test="${placeMap != null && placeMap[ticket.placeId] != null}">
                    Ряд ${placeMap[ticket.placeId].rows}, место ${placeMap[ticket.placeId].seat}
                    — ${placeMap[ticket.placeId].typePlace}
                  </c:if>
                </div>
                <div style="margin-top:8px">
                  <c:choose>
                    <c:when test="${ticket.ticketStatus == 'RESERVED'}">
                      <span class="status-badge status-reserved">RESERVED</span>
                    </c:when>
                    <c:when test="${ticket.ticketStatus == 'SOLD'}">
                      <span class="status-badge status-sold">ОПЛАЧЕН</span>
                    </c:when>
                    <c:when test="${ticket.ticketStatus == 'USED'}">
                      <span class="status-badge status-used">ИСПОЛЬЗОВАН</span>
                    </c:when>
                    <c:when test="${ticket.ticketStatus == 'CANCELED'}">
                      <span class="status-badge status-canceled">ОТМЕНЁН</span>
                    </c:when>
                  </c:choose>
                </div>
              </div>
              <div class="ticket-price">${ticket.price} ₽</div>
            </div>
          </c:forEach>
        </div>
      </c:otherwise>
    </c:choose>
  </div>
</main>

<footer>
  <div class="wrapper"><div class="footer-logo">CINEMAX</div><p>© 2025 CineMax</p></div>
</footer>

</body>
</html>
