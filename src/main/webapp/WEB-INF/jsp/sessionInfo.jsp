<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Сеанс — CINEMAX</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>

<%@ include file="nav.jsp" %>

<main>
  <div class="wrapper" style="padding-top:40px;padding-bottom:60px">
    <a href="${pageContext.request.contextPath}/sessions" style="color:var(--text2);text-decoration:none;font-size:14px;display:inline-flex;align-items:center;gap:8px;margin-bottom:24px">
      ← Все сеансы
    </a>

    <div style="background:var(--surface);border:1px solid var(--border);border-radius:16px;padding:40px;margin-bottom:24px">
      <div style="font-size:11px;letter-spacing:3px;color:var(--text3);text-transform:uppercase;margin-bottom:12px">Информация о сеансе #${session.id}</div>
      <h1 style="font-family:'Bebas Neue',cursive;font-size:52px;letter-spacing:4px;margin-bottom:32px">${film.name}</h1>

      <div style="display:grid;grid-template-columns:repeat(auto-fill,minmax(180px,1fr));gap:16px">
        <div style="background:var(--bg2);border-radius:12px;padding:20px">
          <div style="font-size:11px;color:var(--text3);text-transform:uppercase;letter-spacing:1px;margin-bottom:8px">Начало</div>
          <div style="font-family:'DM Mono',monospace;font-size:17px;color:var(--accent3)">${session.startTime}</div>
        </div>
        <div style="background:var(--bg2);border-radius:12px;padding:20px">
          <div style="font-size:11px;color:var(--text3);text-transform:uppercase;letter-spacing:1px;margin-bottom:8px">Окончание</div>
          <div style="font-family:'DM Mono',monospace;font-size:17px;color:var(--text2)">${session.finishTime}</div>
        </div>
        <div style="background:var(--bg2);border-radius:12px;padding:20px">
          <div style="font-size:11px;color:var(--text3);text-transform:uppercase;letter-spacing:1px;margin-bottom:8px">Зал</div>
          <div style="font-family:'DM Mono',monospace;font-size:17px;color:var(--text)">Зал #${hall.id}</div>
        </div>
        <div style="background:var(--bg2);border-radius:12px;padding:20px">
          <div style="font-size:11px;color:var(--text3);text-transform:uppercase;letter-spacing:1px;margin-bottom:8px">Вместимость</div>
          <div style="font-family:'DM Mono',monospace;font-size:17px;color:var(--text)">${hall.rows} × ${hall.seatsPerRow}</div>
        </div>
      </div>
    </div>

    <a href="${pageContext.request.contextPath}/sessionPlaces?sessionId=${session.id}" class="btn btn-primary btn-lg">
      Выбрать место →
    </a>
  </div>
</main>

<footer>
  <div class="wrapper"><div class="footer-logo">CINEMAX</div><p>© 2025 CineMax</p></div>
</footer>

</body>
</html>
