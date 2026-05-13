<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Выбор места — SWAGAPLEX</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    <style>
        .seat-btn {
            width: 45px; height: 45px; margin: 4px; border: 1px solid #2a3050; background: #0e1118; color: #eef0f8;
            cursor: pointer; border-radius: 6px; transition: 0.2s; text-align: center; font-size: 14px; line-height: 45px; padding: 0;
        }
        .seat-btn:hover:not(:disabled) { background: #e8c84a; color: #000; border-color: #e8c84a; }
        .seat-btn.selected { background: #e8c84a; color: #000; border-color: #e8c84a; }
        .seat-btn.vip { background: rgba(232,200,74,0.15); color: #e8c84a; border-color: #e8c84a; }
        .seat-btn.vip.selected { background: #e8c84a; color: #000; }
        .seat-btn.taken { background: #3a1a1e; color: #ff4757; border-color: #ff4757; cursor: not-allowed; opacity: 0.7; }
        .selected-list { margin-top: 20px; padding: 15px; background: #1a1f2e; border-radius: 12px; border: 1px solid #2a3050; }
        .selected-item { display: flex; justify-content: space-between; align-items: center; padding: 8px 0; border-bottom: 1px solid #2a3050; font-size: 14px; }
        .selected-item:last-child { border-bottom: none; }
        .screen-label { text-align: center; margin-bottom: 24px; }
        .screen-bar { height: 4px; background: linear-gradient(90deg, transparent, #00d4ff, transparent); width: 80%; margin: 0 auto 8px; border-radius: 2px; }
        .screen-text { font-size: 11px; letter-spacing: 4px; color: #8b93b0; text-transform: uppercase; }
        .seat-row { margin-bottom: 10px; display: flex; align-items: center; gap: 6px; flex-wrap: wrap; }
        .row-label { width: 50px; font-size: 13px; color: #8b93b0; font-weight: bold; text-align: right; }
        .seat-legend { display: flex; gap: 20px; margin-top: 30px; justify-content: center; flex-wrap: wrap; }
        .legend-item { display: flex; align-items: center; gap: 8px; font-size: 13px; }
        .legend-box { width: 20px; height: 20px; border-radius: 4px; }
        .total-price { margin-top: 16px; padding: 12px; background: var(--bg2); border-radius: 8px; font-family: 'DM Mono', monospace; font-size: 18px; font-weight: bold; text-align: center; }
        .total-price span { color: var(--accent); font-size: 24px; }
    </style>
</head>
<body>
<%@ include file="nav.jsp" %>

<div class="wrapper" style="padding: 20px;">
    <a href="${pageContext.request.contextPath}/sessions" class="btn btn-ghost" style="margin-bottom:20px;display:inline-block">← Назад к сеансам</a>

    <div style="display: flex; gap: 40px; flex-wrap: wrap;">
        <!-- Схема зала -->
        <div style="flex: 2; min-width: 300px;">
            <div class="screen-label">
                <div class="screen-bar"></div>
                <div class="screen-text">экран</div>
            </div>

            <c:forEach var="rowEntry" items="${byRow}">
                <div class="seat-row">
                    <span class="row-label">Ряд ${rowEntry.key}</span>
                    <c:forEach var="place" items="${rowEntry.value}">
                        <c:set var="isTaken" value="${takenPlaceIds.contains(place.id)}" />
                        <c:set var="price" value="${basePrice * (place.typePlace == 'VIP' ? 1.5 : 1)}" />
                        <button type="button"
                                class="seat-btn <c:if test='${place.typePlace == "VIP"}'>vip</c:if> <c:if test='${isTaken}'>taken</c:if>"
                                data-id="${place.id}"
                                data-row="${place.rows}"
                                data-seat="${place.seat}"
                                data-type="${place.typePlace}"
                                data-price="${price}"
                                ${isTaken ? 'disabled title="Занято"' : ''}
                                onclick="toggleSeat(this)">
                            ${place.seat}
                        </button>
                    </c:forEach>
                </div>
            </c:forEach>

            <div class="seat-legend">
                <div class="legend-item"><div class="legend-box" style="background:#0e1118;border:1px solid #2a3050;"></div><span>Свободно</span></div>
                <div class="legend-item"><div class="legend-box" style="background:rgba(232,200,74,0.15);border:1px solid #e8c84a;"></div><span>VIP</span></div>
                <div class="legend-item"><div class="legend-box" style="background:#e8c84a;"></div><span>Выбрано</span></div>
                <div class="legend-item"><div class="legend-box" style="background:#3a1a1e;border:1px solid #ff4757;"></div><span>Занято</span></div>
            </div>
        </div>

        <!-- Панель выбранных мест -->
        <div style="flex: 1; min-width: 260px;">
            <form id="buyForm" method="post" action="${pageContext.request.contextPath}/buy" onsubmit="prepareForm(event)">
                <input type="hidden" name="sessionId" value="${sessionId}">
                <div id="hiddenInputs"></div>

                <div class="selected-list">
                    <strong style="color: #eef0f8;">Выбранные места:</strong>
                    <div id="selectedPlaces" style="margin-top: 10px; color: #8b93b0; font-size: 14px;">Нет выбранных мест</div>
                </div>

                <div class="total-price" id="totalPriceBlock" style="display: none;">
                    Итого: <span id="totalPrice">0</span> ₽
                </div>

                <div style="margin-top: 16px; padding: 12px; background: rgba(255,107,53,0.1); border: 1px solid rgba(255,107,53,0.3); border-radius: 8px; font-size: 13px; color: #ff6b35;">
                    ⏱ После бронирования у вас будет <strong>${reservationMinutes}</strong> мин для оплаты
                </div>

                <button type="submit" id="submitBtn" class="btn btn-primary" style="width:100%; margin-top:16px;" disabled>Забронировать</button>
            </form>
        </div>
    </div>
</div>

<script>
    // Простой и безопасный JavaScript без eval
    const selected = new Map();

    function toggleSeat(btn) {
        if (btn.disabled || btn.classList.contains('taken')) return;

        const id = btn.getAttribute('data-id');
        const row = btn.getAttribute('data-row');
        const seat = btn.getAttribute('data-seat');
        const type = btn.getAttribute('data-type');
        const price = parseFloat(btn.getAttribute('data-price'));

        if (selected.has(id)) {
            selected.delete(id);
            btn.classList.remove('selected');
        } else {
            selected.set(id, { row, seat, type, price });
            btn.classList.add('selected');
        }
        updateUI();
    }

    function updateUI() {
        const container = document.getElementById('selectedPlaces');
        const submitBtn = document.getElementById('submitBtn');
        const totalBlock = document.getElementById('totalPriceBlock');
        const totalSpan = document.getElementById('totalPrice');

        if (selected.size === 0) {
            container.innerHTML = 'Нет выбранных мест';
            submitBtn.disabled = true;
            totalBlock.style.display = 'none';
            return;
        }

        let html = '';
        let total = 0;
        for (const [id, info] of selected.entries()) {
            const typeColor = info.type === 'VIP' ? '#e8c84a' : '#8b93b0';
            const typeLabel = info.type === 'VIP' ? 'VIP' : 'Стандарт';
            html += '<div class="selected-item">' +
                        '<span>Ряд ' + info.row + ', место ' + info.seat + '</span>' +
                        '<span style="color: ' + typeColor + '; font-weight: 600;">' + typeLabel + '</span>' +
                        '<span style="color: var(--accent); font-family: \'DM Mono\', monospace;">' + info.price.toFixed(2) + ' ₽</span>' +
                    '</div>';
            total += info.price;
        }
        container.innerHTML = html;
        totalSpan.textContent = total.toFixed(2);
        totalBlock.style.display = 'block';
        submitBtn.disabled = false;
    }

    function prepareForm(event) {
        if (selected.size === 0) {
            event.preventDefault();
            return;
        }
        const hiddenContainer = document.getElementById('hiddenInputs');
        hiddenContainer.innerHTML = '';
        for (const [id] of selected.entries()) {
            const input = document.createElement('input');
            input.type = 'hidden';
            input.name = 'placeId';
            input.value = id;
            hiddenContainer.appendChild(input);
        }
    }
</script>
</body>
</html>