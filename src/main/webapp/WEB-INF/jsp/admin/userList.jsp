<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Пользователи — Админ — SWAGAPLEX</title>
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
                    <h1 class="page-title" style="padding:0;border:none;font-size:42px">ПОЛЬЗОВАТЕЛИ</h1>
                </div>
                <c:if test="${not empty error}"><div class="alert alert-error">${error}</div></c:if>
                <div class="table-wrap">
                    <table>
                        <thead>
                            <tr><th>ID</th><th>Имя</th><th>Email</th><th>Администратор</th><th>Действия</th></tr>
                        </thead>
                        <tbody>
                            <c:forEach var="u" items="${users}">
                                <tr>
                                    <td class="text-mono">${u.id}</td>
                                    <td>${u.name}</td>
                                    <td>${u.email}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${u.admin}"><span class="status-badge status-completed">Да</span></c:when>
                                            <c:otherwise><span class="status-badge status-canceled">Нет</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <form method="post" style="display:inline" onsubmit="return confirm('Изменить права?')">
                                            <input type="hidden" name="userId" value="${u.id}">
                                            <c:choose>
                                                <c:when test="${u.admin}">
                                                    <input type="hidden" name="action" value="revoke">
                                                    <button class="btn btn-danger btn-sm">Снять права</button>
                                                </c:when>
                                                <c:otherwise>
                                                    <input type="hidden" name="action" value="grant">
                                                    <button class="btn btn-primary btn-sm">Назначить админом</button>
                                                </c:otherwise>
                                            </c:choose>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
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