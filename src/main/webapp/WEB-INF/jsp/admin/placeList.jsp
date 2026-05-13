<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Места зала #${hallId} — Админ — CINEMAX</title>
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
            <h1 style="font-family:'Bebas Neue',cursive;font-size:42px;letter-spacing:3px">МЕСТА ЗАЛА #${hallId}</h1>
            <c:if test="${not empty hall}">
              <p class="text-muted" style="margin-top:8px">Рядов: ${hall.rows}, мест в ряду: ${hall.seatsPerRow}</p>
            </c:if>
          </div>
          <div style="display:flex;gap:12px">
            <form method="post" action="${pageContext.request.contextPath}/admin/places" onsubmit="return confirm('Сгенерировать места? Это удалит текущие места и создаст новые по схеме зала.')">
              <input type="hidden" name="action" value="generate">
              <input type="hidden" name="hallId" value="${hallId}">
              <button type="submit" class="btn btn-primary">♻️ Сгенерировать места</button>
            </form>
            <a href="${pageContext.request.contextPath}/admin/halls" class="btn btn-ghost">← Все залы</a>
          </div>
        </div>

        <c:if test="${not empty error}">
          <div class="alert alert-error">⚠ ${error}</div>
        </c:if>
        <c:if test="${not empty message}">
          <div class="alert alert-success">✓ ${message}</div>
        </c:if>

        <div style="background:var(--surface);border:1px solid var(--border);border-radius:var(--radius);overflow-x:auto;padding:16px">
          <table style="min-width:800px">
            <thead>
              <tr>
                <th>ID</th>
                <th>Ряд</th>
                <th>Место</th>
                <th>Тип</th>
                <th>Действия</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="place" items="${places}">
                <tr>
                  <td class="text-mono" style="color:var(--text3)">${place.id}</td>
                  <td>${place.rows}</td>
                  <td>${place.seat}</td>
                  <td>
                    <span class="status-badge ${place.typePlace == 'VIP' ? 'status-reserved' : 'status-used'}">
                      ${place.typePlace}
                    </span>
                  </td>
                  <td>
                    <form method="post" action="${pageContext.request.contextPath}/admin/places" style="display:inline">
                      <input type="hidden" name="action" value="updateType">
                      <input type="hidden" name="placeId" value="${place.id}">
                      <input type="hidden" name="hallId" value="${hallId}">
                      <select name="type" class="form-control" style="width:110px;display:inline-block;margin-right:8px">
                        <option value="STANDARD" ${place.typePlace == 'STANDARD' ? 'selected' : ''}>STANDARD</option>
                        <option value="VIP" ${place.typePlace == 'VIP' ? 'selected' : ''}>VIP</option>
                      </select>
                      <button type="submit" class="btn btn-sm btn-primary">Обновить</button>
                    </form>
                  </td>
                </tr>
              </c:forEach>
              <c:if test="${empty places}">
                <tr><td colspan="5" style="text-align:center;padding:40px;color:var(--text3)">Места не сгенерированы. Нажмите «Сгенерировать места».</td></tr>
              </c:if>
            </tbody>
          </table>
        </div>

        <!-- Дополнительная схематичная карта зала (опционально) -->
        <div style="margin-top:40px;background:var(--surface2);border-radius:var(--radius);padding:24px">
          <div style="font-size:14px;color:var(--text2);margin-bottom:16px">Схема зала (ряды / места)</div>
          <div style="display:flex;flex-direction:column;gap:8px">
            <c:forEach var="row" items="${rowsMap}">
              <div style="display:flex;align-items:center;gap:8px">
                <span style="width:32px;font-size:12px;color:var(--text3)">Ряд ${row.key}</span>
                <div style="display:flex;flex-wrap:wrap;gap:4px">
                  <c:forEach var="seat" items="${row.value}">
                    <div style="width:28px;height:24px;background:${seat.typePlace == 'VIP' ? 'rgba(232,200,74,0.2)' : 'var(--surface)'};border:1px solid var(--border);border-radius:4px;text-align:center;font-size:11px;line-height:22px;color:var(--text2)">${seat.seat}</div>
                  </c:forEach>
                </div>
              </div>
            </c:forEach>
          </div>
        </div>

      </div>
    </div>
  </div>
</main>

<footer>
  <div class="wrapper"><div class="footer-logo">CINEMAX</div><p>© 2025 CineMax</p></div>
</footer>

</body>
</html>