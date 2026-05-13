<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Выбор зала — Админ — CINEMAX</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>
<%@ include file="../nav.jsp" %>
<main>
    <div class="wrapper">
        <div class="admin-layout">
            <%@ include file="sidebar.jsp" %>
            <div>
                <h1 class="page-title">Управление местами</h1>
                <p class="text-muted">Выберите зал, чтобы просмотреть или изменить места</p>
                <div style="display:grid;grid-template-columns:repeat(auto-fill,minmax(260px,1fr));gap:16px;margin-top:32px">
                    <c:forEach var="hall" items="${halls}">
                        <a href="${pageContext.request.contextPath}/admin/places?hallId=${hall.id}" class="card card-body" style="text-decoration:none;display:block">
                            <div style="font-size:14px;color:var(--text3)">Зал #${hall.id}</div>
                            <div style="font-size:28px;font-family:'DM Mono',monospace;margin:8px 0">${hall.rows} × ${hall.seatsPerRow}</div>
                            <div class="text-accent">Наценка: ${hall.price} ₽</div>
                        </a>
                    </c:forEach>
                    <c:if test="${empty halls}">
                        <div class="empty-state" style="grid-column:1/-1">Нет залов. <a href="${pageContext.request.contextPath}/admin/halls?action=create">Создайте зал</a>.</div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</main>
<%@ include file="../footer.jsp" %>
</body>
</html>