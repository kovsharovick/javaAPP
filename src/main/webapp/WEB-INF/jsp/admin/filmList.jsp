<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Фильмы — Админ — CINEMAX</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>

<%@ include file="../nav.jsp" %>

<main>
  <div class="wrapper">
    <div class="admin-layout">
      <%@ include file="sidebar.jsp" %>

      <div>
        <div class="flex-between mb-3">
          <div>
            <h1 class="page-title" style="padding:0;border:none;font-size:42px">ФИЛЬМЫ</h1>
          </div>
          <a href="${pageContext.request.contextPath}/admin/films?action=create" class="btn btn-primary">
            + Добавить фильм
          </a>
        </div>

        <c:if test="${not empty error}">
          <div class="alert alert-error">⚠ ${error}</div>
        </c:if>

        <div style="background:var(--surface);border:1px solid var(--border);border-radius:var(--radius);overflow:hidden">
          <div class="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Название</th>
                  <th>Длительность</th>
                  <th>Цена</th>
                  <th>Действия</th>
                </tr>
              </thead>
              <tbody>
                <c:forEach var="film" items="${films}">
                  <tr>
                    <td class="text-mono" style="color:var(--text3)">${film.id}</td>
                    <td>
                      <div style="display:flex;align-items:center;gap:12px">
                        <c:if test="${not empty film.posterUrl}">
                          <img src="${film.posterUrl}" style="width:36px;height:54px;object-fit:cover;border-radius:4px;flex-shrink:0" onerror="this.style.display='none'">
                        </c:if>
                        <span style="font-weight:600;color:var(--text)">${film.name}</span>
                      </div>
                    </td>
                    <td>${film.duration.toMinutes()} мин</td>
                    <td class="text-mono text-accent">${film.price} ₽</td>
                    <td>
                      <div style="display:flex;gap:8px">
                        <a href="${pageContext.request.contextPath}/admin/films?action=edit&id=${film.id}" class="btn btn-ghost btn-sm">✏ Изменить</a>
                        <form method="post" action="${pageContext.request.contextPath}/admin/films" style="display:inline" onsubmit="return confirm('Удалить фильм?')">
                          <input type="hidden" name="action" value="delete">
                          <input type="hidden" name="id" value="${film.id}">
                          <button type="submit" class="btn btn-danger btn-sm">🗑 Удалить</button>
                        </form>
                      </div>
                    </td>
                  </tr>
                </c:forEach>
                <c:if test="${empty films}">
                  <tr><td colspan="5" style="text-align:center;padding:40px;color:var(--text3)">Фильмов нет</td></tr>
                </c:if>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</main>

</body>
</html>
