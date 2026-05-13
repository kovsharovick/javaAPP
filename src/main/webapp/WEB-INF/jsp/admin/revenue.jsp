<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Выручка — Админ — SWAGAPLEX</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>
<%@ include file="../nav.jsp" %>
<main>
    <div class="wrapper">
        <div class="admin-layout">
            <%@ include file="sidebar.jsp" %>
            <div>
                <h1 class="page-title" style="padding:0;border:none;font-size:42px">ВЫРУЧКА</h1>
                <form method="get" action="${pageContext.request.contextPath}/admin/revenue" style="display:flex;gap:16px;align-items:flex-end">
                    <div class="form-group"><label>С даты</label><input type="date" name="from" class="form-control" value="${param.from}"></div>
                    <div class="form-group"><label>По дату</label><input type="date" name="to" class="form-control" value="${param.to}"></div>
                    <button type="submit" class="btn btn-primary">Рассчитать</button>
                </form>
                <c:if test="${not empty revenue}">
                    <div style="margin-top:32px;background:var(--surface);border-radius:16px;padding:24px">
                        <div style="font-size:14px;color:var(--text3)">Итого за период</div>
                        <div style="font-size:42px;font-family:'DM Mono',monospace;color:var(--accent)">${revenue} ₽</div>
                    </div>
                </c:if>
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