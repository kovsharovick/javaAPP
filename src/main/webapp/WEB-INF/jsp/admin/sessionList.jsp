<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Сеансы — Админ — CINEMAX</title>
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
          <h1 style="font-family:'Bebas Neue',cursive;font-size:42px;letter-spacing:3px">СЕАНСЫ</h1>
          <a href="${pageContext.request.contextPath}/admin/sessions?action=create" class="btn btn-primary">+ Создать сеанс</a>
        </div>

        <c:if test="${not empty error}"><div class="alert alert-error">⚠ ${error}</div></c:if>

        <div style="background:var(--surface);border:1px solid var(--border);border-radius:var(--radius);overflow:hidden">
          <div class="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Фильм</th>
                  <th>Зал</th>
                  <th>Начало</th>
                  <th>Окончание</th>
                  <th>Действия</th>
                </tr>
              </thead>
              <tbody>
                <c:forEach var="session" items="${sessions}">
                  <tr>
                    <td class="text-mono" style="color:var(--text3)">${session.id}</td>
                    <td style="color:var(--text);font-weight:500">
                      <c:forEach var="f" items="${films}">
                        <c:if test="${f.id == session.filmId}">${f.name}</c:if>
                      </c:forEach>
                    </td>
                    <td>Зал #${session.hallId}</td>
                    <td class="text-mono" style="color:var(--accent3);font-size:13px">${session.startTime}</td>
                    <td class="text-mono" style="color:var(--text3);font-size:13px">${session.finishTime}</td>
                    <td>
                      <div style="display:flex;gap:8px">
                        <a href="${pageContext.request.contextPath}/admin/sessions?action=edit&id=${session.id}" class="btn btn-ghost btn-sm">✏</a>
                        <form method="post" action="${pageContext.request.contextPath}/admin/sessions" style="display:inline" onsubmit="return confirm('Удалить сеанс?')">
                          <input type="hidden" name="action" value="delete">
                          <input type="hidden" name="id" value="${session.id}">
                          <button type="submit" class="btn btn-danger btn-sm">🗑</button>
                        </form>
                      </div>
                    </td>
                  </tr>
                </c:forEach>
                <c:if test="${empty sessions}">
                  <tr><td colspan="6" style="text-align:center;padding:40px;color:var(--text3)">Сеансов нет</td></tr>
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
