<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Сеансы — SWAGAPLEX</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>

<%@ include file="nav.jsp" %>

<main>
  <div class="wrapper">
    <div class="page-header">
      <h1 class="page-title">СЕА<span>НСЫ</span></h1>
      <p class="page-subtitle">Ближайшие сеансы в наших залах</p>
    </div>

    <!-- Filter bar -->
    <form method="get" action="${pageContext.request.contextPath}/sessions" style="display:flex;gap:12px;margin-bottom:32px;flex-wrap:wrap">
      <select name="filmId" class="form-control" style="width:auto;min-width:200px">
        <option value="">Все фильмы</option>
        <c:forEach var="f" items="${filmMap}">
          <option value="${f.key}" ${param.filmId == f.key ? 'selected' : ''}>${f.value.name}</option>
        </c:forEach>
      </select>
      <input type="date" name="date" class="form-control" style="width:auto" value="${param.date}">
      <button type="submit" class="btn btn-primary">Применить</button>
      <a href="${pageContext.request.contextPath}/sessions" class="btn btn-ghost">Сбросить</a>
    </form>

    <c:choose>
      <c:when test="${empty sessions}">
        <div class="empty-state">
          <div class="empty-icon">🎭</div>
          <h3>Сеансов не найдено</h3>
          <p>Попробуй изменить фильтры</p>
        </div>
      </c:when>
      <c:otherwise>
        <div class="session-list">
          <c:forEach var="session" items="${sessions}">
            <div class="session-card">
              <div>
                <div class="session-film">
                  <c:if test="${filmMap != null && filmMap[session.filmId] != null}">
                    ${filmMap[session.filmId].name}
                  </c:if>
                </div>
                <div class="session-details">
                  <span>Зал #${session.hallId}</span>
                  <span>•</span>
                  <span class="session-time">${session.startTime.toString().replace('T', ' ')}</span>
                </div>
              </div>
              <div style="display:flex;gap:10px;align-items:center">
                <a href="${pageContext.request.contextPath}/sessionPlaces?sessionId=${session.id}" class="btn btn-primary">
                  Выбрать место
                </a>
                <a href="${pageContext.request.contextPath}/session?id=${session.id}" class="btn btn-ghost btn-sm">
                  Подробнее
                </a>
              </div>
            </div>
          </c:forEach>
        </div>
      </c:otherwise>
    </c:choose>
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
