<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Выбор места — CINEMAX</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    <style>
        .seat-btn {
            width: 45px;
            height: 45px;
            margin: 4px;
            border: 1px solid #2a3050;
            background: #0e1118;
            color: #eef0f8;
            cursor: pointer;
            border-radius: 6px;
            transition: 0.2s;
            text-align: center;
            font-size: 14px;
        }
        .seat-btn.selected {
            background: #e8c84a;
            color: #000;
            border-color: #e8c84a;
        }
        .seat-btn.vip {
            background: rgba(232,200,74,0.15);
            color: #e8c84a;
            border-color: #e8c84a;
        }
        .seat-btn.vip.selected {
            background: #e8c84a;
            color: #000;
        }
        .seat-btn.taken {
            background: #3a1a1e;
            color: #ff4757;
            border-color: #ff4757;
            cursor: not-allowed;
            opacity: 0.7;
            text-decoration: line-through;
        }
        .selected-list {
            margin-top: 20px;
            padding: 15px;
            background: #1a1f2e;
            border-radius: 12px;
            border: 1px solid #2a3050;
        }
        .selected-item {
            display: flex;
            justify-content: space-between;
            padding: 8px 0;
            border-bottom: 1px solid #2a3050;
        }
        .screen-label { text-align: center; margin-bottom: 24px; }
        .screen-bar { height: 4px; background: linear-gradient(90deg, transparent, #00d4ff, transparent); width: 80%; margin: 0 auto 8px; border-radius: 2px; }
        .screen-text { font-size: 11px; letter-spacing: 4px; color: #8b93b0; text-transform: uppercase; }
        .seat-row { margin-bottom: 15px; display: flex; align-items: center; gap: 10px; flex-wrap: wrap; }
        .row-label { width: 50px; font-size: 14px; color: #8b93b0; font-weight: bold; }
        .seat-legend { display: flex; gap: 20px; margin-top: 30px; justify-content: center; flex-wrap: wrap; }
        .legend-item { display: flex; align-items: center; gap: 8px; }
        .legend-box { width: 20px; height: 20px; border-radius: 4px; }
    </style>
</head>
<body>
<%@ include file="nav.jsp" %>

<div class="wrapper" style="padding: 20px;">
    <a href="${pageContext.request.contextPath}/sessions" class="btn btn-secondary" style="margin-bottom:20px;display:inline-block">← Назад к сеансам</a>

    <div style="display: flex; gap: 40px; flex-wrap: wrap;">
        <div style="flex: 2;">
            <div class="screen-label">
                <div class="screen-bar"></div>
                <div class="screen-text">экран</div>
            </div>
            <c:forEach var="rowEntry" items="${byRow}">
                <div class="seat-row">
                    <span class="row-label">Ряд ${rowEntry.key}</span>
                    <c:forEach var="place" items="${rowEntry.value}">
                        <c:set var="isTaken" value="${takenPlaceIds.contains(place.id)}" />
                        <button type="button"
                                class="seat-btn ${place.typePlace == 'VIP' ? 'vip' : ''} ${isTaken ? 'taken' : ''}"
                                data-id="${place.id}"
                                data-row="${place.rows}"
                                data-seat="${place.seat}"
                                data-type="${place.typePlace}"
                                ${isTaken ? 'disabled' : ''}
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

        <div style="flex: 1; min-width: 260px;">
            <form id="buyForm" method="post" action="${pageContext.request.contextPath}/buy">
                <input type="hidden" name="sessionId" value="${sessionId}">
                <div id="hiddenInputs"></div>
                <div class="selected-list">
                    <strong>Выбранные места:</strong>
                    <div id="selectedPlaces" style="margin-top: 10px; color: #8b93b0;">Нет выбранных мест</div>
                </div>
                <button type="submit" id="submitBtn" class="btn btn-primary" style="width:100%; margin-top:20px;" disabled>Забронировать</button>
            </form>
            <p style="font-size:12px; color:#565d7a; margin-top:16px; text-align:center;">⏱ После бронирования у вас будет ${reservationMinutes} минут для оплаты</p>
        </div>
    </div>
</div>

<script>
    (function() {
        console.log("=== sessionPlaces.jsp loaded ===");
        console.log("Session ID: ${sessionId}");
        console.log("Reservation minutes: ${reservationMinutes}");
        var rows = document.querySelectorAll('.seat-row');
        console.log("Number of rows:", rows.length);
        rows.forEach(row => {
            var rowNum = row.querySelector('.row-label').textContent;
            var seats = row.querySelectorAll('.seat-btn');
            console.log("Row " + rowNum + " has " + seats.length + " seats");
            seats.forEach(seat => {
                console.log("  seat: id=" + seat.getAttribute('data-id') +
                            ", row=" + seat.getAttribute('data-row') +
                            ", seat=" + seat.getAttribute('data-seat') +
                            ", type=" + seat.getAttribute('data-type'));
            });
        });
    })();

    const selected = new Map();

    function toggleSeat(btn) {
        if (btn.disabled || btn.classList.contains('taken')) return;
        const id = btn.getAttribute('data-id');
        if (selected.has(id)) {
            selected.delete(id);
            btn.classList.remove('selected');
        } else {
            const row = btn.getAttribute('data-row');
            const seat = btn.getAttribute('data-seat');
            const type = btn.getAttribute('data-type');
            console.log("Adding seat: id=" + id + ", row=" + row + ", seat=" + seat + ", type=" + type);
            if (!row || !seat) {
                console.error("Missing row or seat attribute for button", btn);
            }
            selected.set(id, { row, seat, type });
            btn.classList.add('selected');
        }
        updateForm();
    }

    function updateForm() {
        const hiddenContainer = document.getElementById('hiddenInputs');
        const selectedContainer = document.getElementById('selectedPlaces');
        const submitBtn = document.getElementById('submitBtn');
        hiddenContainer.innerHTML = '';
        if (selected.size === 0) {
            selectedContainer.innerHTML = 'Нет выбранных мест';
            submitBtn.disabled = true;
            return;
        }
        let html = '';
        for (let [id, info] of selected.entries()) {
            const input = document.createElement('input');
            input.type = 'hidden';
            input.name = 'placeId';
            input.value = id;
            hiddenContainer.appendChild(input);
            const color = info.type === 'VIP' ? '#e8c84a' : '#8b93b0';
            html += `<div class="selected-item">
                        <span>Ряд ${info.row}, место ${info.seat}</span>
                        <span style="color: ${color}">${info.type}</span>
                     </div>`;
        }
        selectedContainer.innerHTML = html;
        submitBtn.disabled = false;
    }
</script>
</body>
</html>