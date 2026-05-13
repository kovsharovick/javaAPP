<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Залы — Админ — CINEMAX</title>
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
          <h1 style="font-family:'Bebas Neue',cursive;font-size:42px;letter-spacing:3px">ЗАЛЫ</h1>
          <a href="${pageContext.request.contextPath}/admin/halls?action=create" class="btn btn-primary">+ Добавить зал</a>
        </div>

        <c:if test="${not empty error}">
          <div class="alert alert-error">⚠ ${error}</div>
        </c:if>

        <div style="display:grid;grid-template-columns:repeat(auto-fill,minmax(260px,1fr));gap:16px">
          <c:forEach var="hall" items="${halls}">
            <div class="card card-body">
              <div style="font-size:11px;color:var(--text3);letter-spacing:2px;text-transform:uppercase;margin-bottom:8px">Зал #${hall.id}</div>
              <div style="display:grid;grid-template-columns:1fr 1fr;gap:12px;margin-bottom:20px">
                <div>
                  <div style="font-size:11px;color:var(--text3);margin-bottom:4px">Рядов</div>
                  <div style="font-family:'DM Mono',monospace;font-size:20px;color:var(--text)">${hall.rows}</div>
                </div>
                <div>
                  <div style="font-size:11px;color:var(--text3);margin-bottom:4px">Мест в ряду</div>
                  <div style="font-family:'DM Mono',monospace;font-size:20px;color:var(--text)">${hall.seatsPerRow}</div>
                </div>
                <div>
                  <div style="font-size:11px;color:var(--text3);margin-bottom:4px">Наценка</div>
                  <div style="font-family:'DM Mono',monospace;font-size:20px;color:var(--accent)">${hall.price} ₽</div>
                </div>
                <div>
                  <div style="font-size:11px;color:var(--text3);margin-bottom:4px">Вместимость</div>
                  <div style="font-family:'DM Mono',monospace;font-size:20px;color:var(--accent3)">${hall.rows * hall.seatsPerRow}</div>
                </div>
              </div>
              <div style="display:flex;gap:8px;flex-wrap:wrap">
                <a href="${pageContext.request.contextPath}/admin/halls?action=edit&id=${hall.id}" class="btn btn-ghost btn-sm">✏ Изменить</a>
                <a href="${pageContext.request.contextPath}/admin/places?hallId=${hall.id}" class="btn btn-secondary btn-sm">💺 Места</a>
                <form method="post" action="${pageContext.request.contextPath}/admin/halls" style="display:inline" onsubmit="return confirm('Удалить зал?')">
                  <input type="hidden" name="action" value="delete">
                  <input type="hidden" name="id" value="${hall.id}">
                  <button type="submit" class="btn btn-danger btn-sm">🗑</button>
                </form>
              </div>
            </div>
          </c:forEach>
          <c:if test="${empty halls}">
            <div class="empty-state" style="grid-column:1/-1"><div class="empty-icon">🏟️</div><h3>Залов нет</h3></div>
          </c:if>
        </div>
      </div>
    </div>
  </div>
</main>

</body>
</html>
