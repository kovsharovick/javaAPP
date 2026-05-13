<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>CINEMAX — Твой кинотеатр</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>

<%@ include file="nav.jsp" %>

<main>
  <section class="hero">
    <div class="wrapper">
      <p class="hero-label animate-up">Кинотеатр нового поколения</p>
      <h1 class="hero-title animate-up animate-delay-1">
        СМОТРИ<br>
        <span class="line2">БОЛЬШЕ</span>
      </h1>
      <p class="hero-desc animate-up animate-delay-2">
        Лучшие фильмы, комфортные залы и моментальное бронирование —
        всё в одном месте.
      </p>
      <div class="hero-actions animate-up animate-delay-3">
        <a href="${pageContext.request.contextPath}/films" class="btn btn-primary btn-lg">
          Все фильмы →
        </a>
        <a href="${pageContext.request.contextPath}/sessions" class="btn btn-secondary btn-lg">
          Расписание
        </a>
      </div>
    </div>
  </section>

  <section style="padding: 48px 0 64px">
    <div class="wrapper">
      <div style="display:grid;grid-template-columns:repeat(3,1fr);gap:20px;max-width:700px">
        <div class="card" style="padding:28px;text-align:center">
          <div style="font-size:32px;margin-bottom:12px">🎬</div>
          <div style="font-family:'Bebas Neue',cursive;font-size:40px;color:var(--accent);letter-spacing:2px">
            ${films != null ? films.size() : ''}
          </div>
          <div style="color:var(--text2);font-size:13px;margin-top:4px">Фильмов в прокате</div>
        </div>
        <div class="card" style="padding:28px;text-align:center">
          <div style="font-size:32px;margin-bottom:12px">🏟️</div>
          <div style="font-family:'Bebas Neue',cursive;font-size:40px;color:var(--accent3);letter-spacing:2px">IMAX</div>
          <div style="color:var(--text2);font-size:13px;margin-top:4px">Качество экрана</div>
        </div>
        <div class="card" style="padding:28px;text-align:center">
          <div style="font-size:32px;margin-bottom:12px">⚡</div>
          <div style="font-family:'Bebas Neue',cursive;font-size:40px;color:var(--accent2);letter-spacing:2px">2 МИН</div>
          <div style="color:var(--text2);font-size:13px;margin-top:4px">Бронирование</div>
        </div>
      </div>
    </div>
  </section>
</main>

<footer>
  <div class="wrapper">
    <div class="footer-logo">CINEMAX</div>
    <p>© 2025 CineMax. Все права защищены.</p>
  </div>
</footer>

<style>
  .hero-title { opacity: 0; animation: fadeUp 0.7s ease 0.1s forwards; }
  .hero-label { opacity: 0; animation: fadeUp 0.5s ease forwards; }
  .hero-desc { opacity: 0; animation: fadeUp 0.5s ease 0.2s forwards; }
  .hero-actions { opacity: 0; animation: fadeUp 0.5s ease 0.35s forwards; }
</style>

</body>
</html>
