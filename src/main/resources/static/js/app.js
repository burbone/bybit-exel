document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('exportForm');
    const checkSymbolButton = document.getElementById('checkSymbolButton');
    const generateReportButton = document.getElementById('generateReportButton');
    const statusDiv = document.getElementById('status');

    if (checkSymbolButton) {
        checkSymbolButton.addEventListener('click', async () => {
            const symbol = document.getElementById('symbol').value.toUpperCase();

            try {
                const response = await fetch('/check-symbol', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: `symbol=${encodeURIComponent(symbol)}`
                });

                if (!response.ok) {
                    throw new Error('Ошибка проверки символа');
                }

                const textResponse = await response.text(); // Дождитесь завершения Promise
                const isValid = textResponse.trim(); // Теперь можно вызвать trim()

                if (isValid === 'true') {
                    statusDiv.className = 'status-message success';
                    statusDiv.textContent = 'Символ найден';
                } else {
                    statusDiv.className = 'status-message error';
                    statusDiv.textContent = 'Символ не найден';
                }
            } catch (error) {
                statusDiv.className = 'status-message error';
                statusDiv.textContent = `Ошибка: ${error.message}`;
            }
        });
    } else {
        console.error('Кнопка проверки символа не найдена');
    }

    if (generateReportButton) {
        generateReportButton.addEventListener('click', async () => {
            statusDiv.className = 'status-message processing';
            statusDiv.textContent = 'Обработка запроса...';

            try {
                const requestData = {
                    symbol: document.getElementById('symbol').value.toUpperCase(),
                    interval: document.getElementById('interval').value,
                    startTime: convertDateTime(document.getElementById('startTime').value),
                    endTime: convertDateTime(document.getElementById('endTime').value)
                };

                const response = await fetch('/api/export', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(requestData)
                });

                if (!response.ok) {
                    const error = await response.json();
                    throw new Error(error.message || 'Ошибка сервера');
                }

                // Скачивание файла
                const blob = await response.blob();
                const url = window.URL.createObjectURL(blob);
                const link = document.createElement('a');
                link.href = url;
                link.download = `crypto_${requestData.symbol}_${requestData.interval}.xlsx`;
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);

                statusDiv.className = 'status-message success';
                statusDiv.textContent = 'Файл успешно сгенерирован!';

            } catch (error) {
                statusDiv.className = 'status-message error';
                statusDiv.textContent = `Ошибка: ${error.message}`;
            }
        });
    } else {
        console.error('Кнопка генерации отчета не найдена');
    }
});

// Преобразование даты из формата HTML в timestamp
function convertDateTime(htmlDateTime) {
    if (!htmlDateTime) {
        throw new Error('Дата не указана');
    }

    const date = new Date(htmlDateTime);

    if (isNaN(date)) {
        throw new Error('Некорректный формат даты');
    }

    // Форматируем дату строго в формат YYYY-MM-DD HH:MM:SS
    const yyyy = date.getFullYear();
    const mm = String(date.getMonth() + 1).padStart(2, '0');
    const dd = String(date.getDate()).padStart(2, '0');
    const hh = String(date.getHours()).padStart(2, '0');
    const mi = String(date.getMinutes()).padStart(2, '0');
    const ss = String(date.getSeconds()).padStart(2, '0');

    return `${yyyy}-${mm}-${dd} ${hh}:${mi}:${ss}`;
}