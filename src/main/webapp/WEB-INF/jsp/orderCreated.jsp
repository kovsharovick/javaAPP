<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Заказ создан — SWAGAPLEX</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>
<%@ include file="nav.jsp" %>
<main>
    <div class="wrapper" style="padding-top:60px;padding-bottom:60px;max-width:600px">
        <div style="text-align:center;margin-bottom:40px">
            <div style="font-size:72px;margin-bottom:16px">✅</div>
            <h1 style="font-family: 'Poppins', 'Montserrat', sans-serif;font-size:52px;letter-spacing:4px;margin-bottom:8px">
                Бронь <span style="color:var(--accent)">готова!</span>
            </h1>
            <p style="color:var(--text2);font-size:16px">
                Места зарезервированы. Оплати заказ, чтобы завершить покупку.
            </p>
        </div>

        <div style="background:var(--surface);border:1px solid var(--border);border-radius:16px;padding:32px;margin-bottom:24px">
            <div style="display:flex;justify-content:space-between;align-items:start;margin-bottom:24px">
                <div>
                    <div style="font-size:11px;color:var(--text3);letter-spacing:2px;text-transform:uppercase;margin-bottom:6px">Номер заказа</div>
                    <div style="font-family:'DM Mono',monospace;font-size:28px;color:var(--text)">
                        #<span style="color:var(--accent)">${order.id}</span>
                    </div>
                </div>
                <div style="text-align:right">
                    <div style="font-size:11px;color:var(--text3);letter-spacing:2px;text-transform:uppercase;margin-bottom:6px">Сумма</div>
                    <div style="font-family:'DM Mono',monospace;font-size:28px;color:var(--accent)">${order.amount} ₽</div>
                </div>
            </div>

            <!-- Таймер обратного отсчёта -->
            <div style="margin-bottom:24px; display:flex; align-items:center; gap:12px; background:rgba(255,107,53,0.1); padding:14px 20px; border-radius:8px; border:1px solid rgba(255,107,53,0.3);">
                <span style="font-size:20px">⏱</span>
                <div>
                    <div style="font-weight:600;margin-bottom:2px;color:var(--text)">Время на оплату истекает</div>
                    <div style="font-size:13px;color:var(--text2)">
                        Осталось: <span id="timer" style="font-family:'DM Mono',monospace;font-size:20px;font-weight:600;color:var(--accent2);">--:--</span>
                    </div>
                </div>
            </div>

            <form method="post" action="${pageContext.request.contextPath}/pay">
                <input type="hidden" name="orderId" value="${order.id}">
                <button type="submit" class="btn btn-primary w-full btn-lg">
                    💳 Оплатить ${order.amount} ₽
                </button>
            </form>
        </div>

        <div style="display:flex;gap:12px">
            <form method="post" action="${pageContext.request.contextPath}/cancelOrder" style="flex:1">
                <input type="hidden" name="orderId" value="${order.id}">
                <button type="submit" class="btn btn-danger w-full"
                        onclick="return confirm('Отменить бронь?')">
                    Отменить бронь
                </button>
            </form>
            <a href="${pageContext.request.contextPath}/myOrders" class="btn btn-ghost" style="flex:1;text-align:center">
                Мои заказы
            </a>
        </div>
    </div>
</main>

<footer>
    <div class="wrapper">
        <div class="footer-logo">SWAGAPLEX</div>
        <p>© 2026 Swagaplex</p>
    </div>
</footer>

<script>
    (function () {
        var timerEl = document.getElementById('timer');
        // reservationMinutes приходит как атрибут запроса (Integer)
        var minutes = parseInt('<c:out value="${reservationMinutes}" default="0"/>', 10);

        if (isNaN(minutes) || minutes <= 0) {
            timerEl.textContent = '00:00';
            timerEl.style.color = 'var(--danger)';
            return;
        }

        var totalSeconds = minutes * 60;

        function format(s) {
            var m = Math.floor(s / 60);
            var sec = s % 60;
            return String(m).padStart(2, '0') + ':' + String(sec).padStart(2, '0');
        }

        timerEl.textContent = format(totalSeconds);

        var interval = setInterval(function () {
            totalSeconds--;
            if (totalSeconds <= 0) {
                clearInterval(interval);
                timerEl.textContent = '00:00';
                timerEl.style.color = 'var(--danger)';
                return;
            }
            timerEl.textContent = format(totalSeconds);
            if (totalSeconds <= 30) {
                timerEl.style.color = 'var(--danger)';
            }
        }, 1000);
    })();
</script>
</body>
</html>
