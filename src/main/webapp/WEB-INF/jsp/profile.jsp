<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Профиль — SWAGAPLEX</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>

<%@ include file="nav.jsp" %>

<main>
  <div class="wrapper" style="padding-top:40px;padding-bottom:60px;max-width:640px">

    <div class="profile-header animate-up">
      <div class="avatar">${user.name.substring(0,1).toUpperCase()}</div>
      <div>
        <h2 style="font-size:22px;font-weight:700;margin-bottom:4px">${user.name}</h2>
        <p style="color:var(--text2);font-size:14px">${user.email}</p>
        <c:if test="${user.admin}">
          <span class="badge-admin" style="margin-top:8px;display:inline-block">Администратор</span>
        </c:if>
      </div>
    </div>

    <c:if test="${not empty message}">
      <div class="alert alert-success">✓ ${message}</div>
    </c:if>
    <c:if test="${not empty error}">
      <div class="alert alert-error">⚠ ${error}</div>
    </c:if>

    <div style="background:var(--surface);border:1px solid var(--border);border-radius:16px;padding:32px">
      <h3 style="font-family: 'Poppins', 'Montserrat', sans-serif;font-size:28px;letter-spacing:2px;margin-bottom:24px">
        Редактировать <span style="color:var(--accent)">профиль</span>
      </h3>

      <form method="post" action="${pageContext.request.contextPath}/profile">
        <div class="form-group">
          <label class="form-label">Имя</label>
          <input class="form-control" type="text" name="name" value="${user.name}" placeholder="Твоё имя">
        </div>
        <div class="form-group">
          <label class="form-label">Email</label>
          <input class="form-control" type="email" name="email" value="${user.email}" placeholder="you@example.com">
        </div>
        <div class="form-group">
          <label class="form-label">Новый пароль <span style="color:var(--text3);font-weight:400">(оставь пустым, чтобы не менять)</span></label>
          <input class="form-control" type="password" name="password" placeholder="••••••••" minlength="6">
        </div>
        <button type="submit" class="btn btn-primary">Сохранить изменения</button>
      </form>
    </div>

  </div>
</main>

<footer>
  <div class="wrapper"><div class="footer-logo">SWAGAPLEX</div><p>© 2026 Swagaplex</p></div>
</footer>

</body>
</html>
