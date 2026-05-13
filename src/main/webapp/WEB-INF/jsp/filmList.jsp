<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Фильмы — CINEMAX</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>

<%@ include file="nav.jsp" %>

<main>
  <div class="wrapper">
    <div class="page-header">
      <h1 class="page-title">ФИЛ<span>ЬМЫ</span></h1>
      <p class="page-subtitle">Выбери фильм и забронируй место прямо сейчас</p>
    </div>

    <form method="get" action="${pageContext.request.contextPath}/films" class="search-bar">
      <input
        class="search-input"
        type="text"
        name="search"
        placeholder="🔍  Поиск по названию..."
        value="${param.search}"
      >
      <button type="submit" class="btn btn-primary">Найти</button>
      <c:if test="${param.search != null && param.search != ''}">
        <a href="${pageContext.request.contextPath}/films" class="btn btn-ghost">Сбросить</a>
      </c:if>
    </form>

    <c:choose>
      <c:when test="${empty films}">
        <div class="empty-state">
          <div class="empty-icon">🎬</div>
          <h3>Фильмов не найдено</h3>
          <p>Попробуй изменить поисковый запрос</p>
        </div>
      </c:when>
      <c:otherwise>
        <div class="film-grid">
          <c:forEach var="film" items="${films}">
            <a href="${pageContext.request.contextPath}/film?id=${film.id}" style="text-decoration:none;display:block">
              <div class="film-card">
                <c:choose>
                  <c:when test="${not empty film.posterUrl}">
                    <img class="film-poster" src="${film.posterUrl}" alt="${film.name}" onerror="this.style.display='none';this.nextElementSibling.style.display='flex'">
                    <div class="film-poster-placeholder" style="display:none">🎬</div>
                  </c:when>
                  <c:otherwise>
                    <div class="film-poster-placeholder">🎬</div>
                  </c:otherwise>
                </c:choose>
                <div class="film-info">
                  <div class="film-title">${film.name}</div>
                  <div class="film-meta">
                    <span>${film.duration.toMinutes()} мин</span>
                    <span class="film-price">от ${film.price} ₽</span>
                  </div>
                </div>
              </div>
            </a>
          </c:forEach>
        </div>
      </c:otherwise>
    </c:choose>
  </div>
</main>

<footer>
  <div class="wrapper">
    <div class="footer-logo">CINEMAX</div>
    <p>© 2025 CineMax</p>
  </div>
</footer>

</body>
</html>
