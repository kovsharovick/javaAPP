<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${film.name} — CINEMAX</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>

<%@ include file="nav.jsp" %>

<main>
  <div class="wrapper" style="padding-top:40px;padding-bottom:60px">
    <div style="display:grid;grid-template-columns:280px 1fr;gap:48px;align-items:start">

      <!-- Poster -->
      <div>
        <c:choose>
          <c:when test="${not empty film.posterUrl}">
            <img src="${film.posterUrl}" alt="${film.name}"
              style="width:100%;border-radius:16px;box-shadow:0 24px 64px rgba(0,0,0,0.6)"
              onerror="this.outerHTML='<div style=\'width:100%;aspect-ratio:2/3;background:var(--surface);border-radius:16px;display:flex;align-items:center;justify-content:center;font-size:80px\'>🎬</div>'">
          </c:when>
          <c:otherwise>
            <div style="width:100%;aspect-ratio:2/3;background:var(--surface);border-radius:16px;display:flex;align-items:center;justify-content:center;font-size:80px">🎬</div>
          </c:otherwise>
        </c:choose>
      </div>

      <!-- Info -->
      <div>
        <div style="font-size:11px;letter-spacing:3px;color:var(--text3);text-transform:uppercase;margin-bottom:12px">Фильм</div>
        <h1 style="font-family:'Bebas Neue',cursive;font-size:64px;letter-spacing:4px;line-height:1;margin-bottom:24px">${film.name}</h1>

        <div style="display:flex;gap:24px;margin-bottom:32px;flex-wrap:wrap">
          <div style="background:var(--surface);border:1px solid var(--border);border-radius:10px;padding:16px 24px;text-align:center">
            <div style="font-size:11px;color:var(--text3);letter-spacing:1px;text-transform:uppercase;margin-bottom:4px">Длительность</div>
            <div style="font-family:'DM Mono',monospace;font-size:22px;color:var(--text);font-weight:500">${film.duration.toMinutes()} мин</div>
          </div>
          <div style="background:var(--surface);border:1px solid var(--border);border-radius:10px;padding:16px 24px;text-align:center">
            <div style="font-size:11px;color:var(--text3);letter-spacing:1px;text-transform:uppercase;margin-bottom:4px">Базовая цена</div>
            <div style="font-family:'DM Mono',monospace;font-size:22px;color:var(--accent);font-weight:500">от ${film.price} ₽</div>
          </div>
        </div>

        <c:if test="${not empty film.description}">
          <div style="margin-bottom:32px">
            <div style="font-size:11px;color:var(--text3);letter-spacing:1px;text-transform:uppercase;margin-bottom:12px">О фильме</div>
            <p style="color:var(--text2);line-height:1.8;font-size:16px">${film.description}</p>
          </div>
        </c:if>

        <a href="${pageContext.request.contextPath}/sessions?filmId=${film.id}" class="btn btn-primary btn-lg">
          Выбрать сеанс →
        </a>
      </div>
    </div>
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
