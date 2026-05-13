<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Заказ создан — CINEMAX</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>
<%@ include file="nav.jsp" %>
<main>
    <div class="wrapper" style="padding-top:60px;padding-bottom:60px;max-width:600px">
        <div style="text-align:center;margin-bottom:40px">
            <div style="font-size:72px;margin-bottom:16px">✅</div>
            <h1 style="font-family:'Bebas Neue',cursive;font-size:52px;letter-spacing:4px;margin-bottom:8px">Бронь <span style="color:var(--accent)">готова!</span></h1>
            <p style="color:var(--text2);font-size:16px">Места зарезервированы. Оплати заказ, чтобы завершить покупку.</p>
        </div>
        <div style="background:var(--surface);border:1px solid var(--border);border-radius:16px;padding:32px;margin-bottom:24px">
            <div style="display:flex;justify-content:space-between;align-items:start;margin-bottom:24px">
                <div>
                    <div style="font-size:11px;color:var(--text3);letter-spacing:2px;text-transform:uppercase;margin-bottom:6px">Номер заказа</div>
                    <div style="font-family:'DM Mono',monospace;font-size:28px;color:var(--text)">#<span style="color:var(--accent)">${order.id}</span></div>
                </div>
                <div style="text-align:right">
                    <div style="font-size:11px;color:var(--text3);letter-spacing:2px;text-transform:uppercase;margin-bottom:6px">Сумма</div>
                    <div style="font-family:'DM Mono',monospace;font-size:28px;color:var(--accent)">${order.amount} ₽</div>
                </div>
            </div>
            <div class="reservation-timer" style="margin-bottom:24px; display:flex; align-items:center; gap:12px; background:rgba(255,107,53,0.1); padding:14px 20px; border-radius:8px;">
                <span style="font-size:20px">⏱</span>
                <div>
                    <div style="font-weight:600;margin-bottom:2px">Время на оплату</div>
                    <div style="font-size:13px;color:var(--text2)">Бронь истечёт через <span id="timer" class="timer-value" style="font-family:'DM Mono',monospace;font-size:20px;font-weight:500;">--:--</span></div>
                </div>
            </div>
            <form method="post" action="${pageContext.request.contextPath}/pay">
                <input type="hidden" name="orderId" value="${order.id}">
                <button type="submit" class="btn btn-primary w-full btn-lg">💳 Оплатить ${order.amount} ₽</button>
            </form>
        </div>
        <div style="display:flex;gap:12px">
            <form method="post" action="${pageContext.request.contextPath}/cancelOrder" style="flex:1">
                <input type="hidden" name="orderId" value="${order.id}">
                <button type="submit" class="btn btn-danger w-full" onclick="return confirm('Отменить бронь?')">Отменить бронь</button>
            </form>
            <a href="${pageContext.request.contextPath}/myOrders" class="btn btn-ghost" style="flex:1;text-align:center">Мои заказы</a>
        </div>
    </div>
</main>
<footer><div class="wrapper"><div class="footer-logo">CINEMAX</div><p>© 2025 CineMax</p></div></footer>
<script>
    console.log("Order created page, reservation minutes = ${reservationMinutes}");
    let seconds = ${reservationMinutes} * 60;
    const timerEl = document.getElementById('timer');
    if (timerEl && !isNaN(seconds) && seconds > 0) {
        const interval = setInterval(() => {
            seconds--;
            if (seconds <= 0) {
                clearInterval(interval);
                timerEl.textContent = '00:00';
                timerEl.style.color = 'var(--danger)';
                return;
            }
            const m = Math.floor(seconds / 60).toString().padStart(2, '0');
            const s = (seconds % 60).toString().padStart(2, '0');
            timerEl.textContent = `${m}:${s}`;
            if (seconds < 30) timerEl.style.color = 'var(--danger)';
        }, 1000);
    } else if (timerEl && !isNaN(seconds) && seconds === 0) {
        timerEl.textContent = '00:00';
        timerEl.style.color = 'var(--danger)';
    } else {
        console.error("Timer not initialized, seconds=", seconds);
    }
</script>
</body>
</html>