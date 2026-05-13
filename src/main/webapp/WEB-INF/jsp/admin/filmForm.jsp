<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${film != null ? 'Редактирование' : 'Добавление'} фильма — SWAGAPLEX</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>

<%@ include file="../nav.jsp" %>

<main>
  <div class="wrapper">
    <div class="admin-layout">
      <%@ include file="sidebar.jsp" %>

      <div style="max-width:540px">
        <a href="${pageContext.request.contextPath}/admin/films" style="color:var(--text2);text-decoration:none;font-size:14px;display:inline-flex;align-items:center;gap:6px;margin-bottom:24px">
          ← Все фильмы
        </a>

        <h1 class="page-title" style="padding:0;border:none;font-size:42px">
            <c:choose>
                <c:when test="${film != null}">Редактировать <span style="color:var(--accent)">фильм</span></c:when>
                <c:otherwise>Новый <span style="color:var(--accent)">фильм</span></c:otherwise>
            </c:choose>
        </h1>

        <c:if test="${not empty error}">
          <div class="alert alert-error">⚠ ${error}</div>
        </c:if>

        <div style="background:var(--surface);border:1px solid var(--border);border-radius:16px;padding:32px">
          <form method="post" action="${pageContext.request.contextPath}/admin/films">
            <input type="hidden" name="action" value="${film != null ? 'edit' : 'create'}">
            <c:if test="${film != null}">
              <input type="hidden" name="id" value="${film.id}">
            </c:if>

            <div class="form-group">
              <label class="form-label">Название</label>
              <input class="form-control" type="text" name="name" value="${film.name}" placeholder="Название фильма" required>
            </div>

            <div class="form-group">
              <label class="form-label">Длительность (мин)</label>
              <input class="form-control" type="number" name="duration" value="${film.duration.toMinutes()}" placeholder="120" min="1" required>
            </div>

            <div class="form-group">
              <label class="form-label">Базовая цена (₽)</label>
              <input class="form-control" type="number" name="price" step="0.01" value="${film.price}" placeholder="500.00" min="0" required>
            </div>

            <div class="form-group">
              <label class="form-label">Описание</label>
              <textarea class="form-control" name="description" rows="4" placeholder="Краткое описание фильма...">${film.description}</textarea>
            </div>

            <div class="form-group">
              <label class="form-label">URL постера</label>
              <input class="form-control" type="text" name="posterUrl" value="${film.posterUrl}" placeholder="https://...">
            </div>

            <div style="display:flex;gap:12px;margin-top:8px">
              <button type="submit" class="btn btn-primary">
                ${film != null ? 'Сохранить' : 'Создать фильм'}
              </button>
              <a href="${pageContext.request.contextPath}/admin/films" class="btn btn-ghost">Отмена</a>
            </div>
          </form>
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
