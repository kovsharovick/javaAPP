<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${session != null ? 'Редактирование' : 'Создание'} сеанса — CINEMAX</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>

<%@ include file="../nav.jsp" %>

<main>
  <div class="wrapper">
    <div class="admin-layout">
      <%@ include file="sidebar.jsp" %>
      <div style="max-width:480px">
        <a href="${pageContext.request.contextPath}/admin/sessions" style="color:var(--text2);text-decoration:none;font-size:14px;display:inline-flex;gap:6px;margin-bottom:24px">← Все сеансы</a>
        <h1 style="font-family:'Bebas Neue',cursive;font-size:42px;letter-spacing:3px;margin-bottom:32px">
          <c:choose><c:when test="${session != null}">Изменить <span style="color:var(--accent)">сеанс</span></c:when><c:otherwise>Новый <span style="color:var(--accent)">сеанс</span></c:otherwise></c:choose>
        </h1>
        <c:if test="${not empty error}"><div class="alert alert-error">⚠ ${error}</div></c:if>
        <div style="background:var(--surface);border:1px solid var(--border);border-radius:16px;padding:32px">
          <form method="post" action="${pageContext.request.contextPath}/admin/sessions">
            <input type="hidden" name="action" value="${session != null ? 'edit' : 'create'}">
            <c:if test="${session != null}"><input type="hidden" name="id" value="${session.id}"></c:if>

            <div class="form-group">
              <label class="form-label">Фильм</label>
              <select class="form-control" name="filmId" required>
                <option value="">Выберите фильм</option>
                <c:forEach var="film" items="${films}">
                  <option value="${film.id}" ${session != null && session.filmId == film.id ? 'selected' : ''}>${film.name}</option>
                </c:forEach>
              </select>
            </div>
            <div class="form-group">
              <label class="form-label">Зал</label>
              <select class="form-control" name="hallId" required>
                <option value="">Выберите зал</option>
                <c:forEach var="hall" items="${halls}">
                  <option value="${hall.id}" ${session != null && session.hallId == hall.id ? 'selected' : ''}>Зал #${hall.id} (${hall.rows}×${hall.seatsPerRow})</option>
                </c:forEach>
              </select>
            </div>
            <div class="form-group">
              <label class="form-label">Начало сеанса</label>
              <input class="form-control" type="datetime-local" name="startTime" value="${session.startTime}" required>
            </div>
            <div style="display:flex;gap:12px">
              <button type="submit" class="btn btn-primary">${session != null ? 'Сохранить' : 'Создать'}</button>
              <a href="${pageContext.request.contextPath}/admin/sessions" class="btn btn-ghost">Отмена</a>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</main>

</body>
</html>
