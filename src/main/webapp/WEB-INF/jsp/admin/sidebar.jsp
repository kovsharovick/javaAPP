<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<div class="admin-sidebar">
  <div class="sidebar-title">Управление</div>
  <a href="${pageContext.request.contextPath}/admin/films" class="sidebar-link">
    <span class="icon">🎬</span> Фильмы
  </a>
  <a href="${pageContext.request.contextPath}/admin/sessions" class="sidebar-link">
    <span class="icon">🎭</span> Сеансы
  </a>
  <a href="${pageContext.request.contextPath}/admin/halls" class="sidebar-link">
    <span class="icon">🏟️</span> Залы
  </a>
  <a href="${pageContext.request.contextPath}/admin/places" class="sidebar-link">
    <span class="icon">💺</span> Места
  </a>
  <div class="sidebar-title" style="margin-top:16px">Продажи</div>
  <a href="${pageContext.request.contextPath}/admin/orders" class="sidebar-link">
    <span class="icon">📋</span> Заказы
  </a>
  <a href="${pageContext.request.contextPath}/admin/tickets" class="sidebar-link">
    <span class="icon">🎫</span> Билеты
  </a>
  <a href="${pageContext.request.contextPath}/admin/revenue" class="sidebar-link">
    <span class="icon">💰</span> Выручка
  </a>
  <div class="sidebar-title" style="margin-top:16px">Пользователи</div>
  <a href="${pageContext.request.contextPath}/admin/users" class="sidebar-link">
    <span class="icon">👥</span> Пользователи
  </a>
</div>
