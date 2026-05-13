<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Регистрация — SWAGAPLEX</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body style="display:flex;min-height:100vh;flex-direction:column">

<%@ include file="nav.jsp" %>

<main style="flex:1;display:flex;align-items:center;padding:60px 0">
  <div class="wrapper" style="width:100%">

    <div class="form-card animate-up">
      <div style="text-align:center;margin-bottom:32px">
        <a href="${pageContext.request.contextPath}/" style="font-family: 'Poppins', 'Montserrat', sans-serif;font-size:32px;letter-spacing:4px;color:var(--accent);text-decoration:none">
          SWAGAPLEX
        </a>
      </div>

      <h1 class="form-title">Регистрация</h1>
      <p class="form-subtitle">Создай аккаунт и начни смотреть</p>

      <c:if test="${not empty error}">
        <div class="alert alert-error">⚠ ${error}</div>
      </c:if>

      <form method="post" action="${pageContext.request.contextPath}/register">
        <div class="form-group">
          <label class="form-label">Имя</label>
          <input class="form-control" type="text" name="name" placeholder="Твоё имя" required autofocus>
        </div>
        <div class="form-group">
          <label class="form-label">Email</label>
          <input class="form-control" type="email" name="email" placeholder="you@example.com" required>
        </div>
        <div class="form-group">
          <label class="form-label">Пароль</label>
          <input class="form-control" type="password" name="password" placeholder="Минимум 6 символов" required minlength="6">
        </div>
        <button type="submit" class="btn btn-primary w-full btn-lg" style="margin-top:8px">
          Создать аккаунт →
        </button>
      </form>

      <div class="divider"></div>

      <p style="text-align:center;color:var(--text2);font-size:14px">
        Уже есть аккаунт?
        <a href="${pageContext.request.contextPath}/login" style="color:var(--accent);text-decoration:none;font-weight:600">
          Войти
        </a>
      </p>
    </div>

  </div>
</main>

<footer>
  <div class="wrapper"><div class="footer-logo">SWAGAPLEX</div><p>© 2026 Swagaplex</p></div>
</footer>

</body>
</html>
