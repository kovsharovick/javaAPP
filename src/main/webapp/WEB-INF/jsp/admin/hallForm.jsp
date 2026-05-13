<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${hall != null ? 'Редактирование' : 'Создание'} зала — CINEMAX</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>

<%@ include file="../nav.jsp" %>

<main>
    <div class="wrapper">
        <div class="admin-layout">
            <%@ include file="sidebar.jsp" %>
            <div style="max-width:480px">
                <a href="${pageContext.request.contextPath}/admin/halls" style="color:var(--text2);text-decoration:none;font-size:14px;display:inline-flex;gap:6px;margin-bottom:24px">← Все залы</a>
                <h1 style="font-family:'Bebas Neue',cursive;font-size:42px;letter-spacing:3px;margin-bottom:32px">
                    <c:choose><c:when test="${hall != null}">Изменить <span style="color:var(--accent)">зал</span></c:when><c:otherwise>Новый <span style="color:var(--accent)">зал</span></c:otherwise></c:choose>
                </h1>
                <c:if test="${not empty error}"><div class="alert alert-error">⚠ ${error}</div></c:if>
                <div style="background:var(--surface);border:1px solid var(--border);border-radius:16px;padding:32px">
                    <form method="post" action="${pageContext.request.contextPath}/admin/halls">
                        <input type="hidden" name="action" value="${hall != null ? 'edit' : 'create'}">
                        <c:if test="${hall != null}"><input type="hidden" name="id" value="${hall.id}"></c:if>

                        <div class="form-group">
                            <label class="form-label">Рядов</label>
                            <c:choose>
                                <c:when test="${hall != null}">
                                    <input class="form-control" type="number" value="${hall.rows}" disabled>
                                    <input type="hidden" name="rows" value="${hall.rows}">
                                </c:when>
                                <c:otherwise>
                                    <input class="form-control" type="number" name="rows" value="${hall.rows}" min="1" required>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div class="form-group">
                            <label class="form-label">Мест в ряду</label>
                            <c:choose>
                                <c:when test="${hall != null}">
                                    <input class="form-control" type="number" value="${hall.seatsPerRow}" disabled>
                                    <input type="hidden" name="seatsPerRow" value="${hall.seatsPerRow}">
                                </c:when>
                                <c:otherwise>
                                    <input class="form-control" type="number" name="seatsPerRow" value="${hall.seatsPerRow}" min="1" required>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div class="form-group">
                            <label class="form-label">Наценка зала (₽)</label>
                            <input class="form-control" type="number" name="price" step="0.01" value="${hall.price}" min="0" required>
                        </div>

                        <div style="display:flex;gap:12px">
                            <button type="submit" class="btn btn-primary">${hall != null ? 'Сохранить' : 'Создать зал'}</button>
                            <a href="${pageContext.request.contextPath}/admin/halls" class="btn btn-ghost">Отмена</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</main>

<%@ include file="../footer.jsp" %>
</body>
</html>