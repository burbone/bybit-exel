document.addEventListener('DOMContentLoaded', () => {
    const form                 = document.getElementById('exportForm');
    const checkSymbolButton    = document.getElementById('checkSymbolButton');
    const generateReportButton = document.getElementById('generateReportButton');
    const statusDiv            = document.getElementById('status');

    form?.addEventListener('submit', e => e.preventDefault());

    checkSymbolButton?.addEventListener('click', async e => {
      e.preventDefault();
      const symbol = document.getElementById('symbol').value.trim().toUpperCase();
      try {
        const resp = await fetch('/check-symbol', {
          method: 'POST',
          headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
          body: `symbol=${encodeURIComponent(symbol)}`
        });
        if (!resp.ok) throw new Error('Ошибка проверки символа');
        const isValid = (await resp.text()).trim() === 'true';
        statusDiv.className   = isValid ? 'status-message success' : 'status-message error';
        statusDiv.textContent = isValid ? 'Символ найден' : 'Символ не найден';
      } catch (err) {
        statusDiv.className   = 'status-message error';
        statusDiv.textContent = `Ошибка: ${err.message}`;
      }
    });

    generateReportButton?.addEventListener('click', async e => {
      e.preventDefault();
      statusDiv.className   = 'status-message processing';
      statusDiv.textContent = 'Обработка запроса…';
  
      try {
        const requestData = {
          symbol:    document.getElementById('symbol').value.trim().toUpperCase(),
          interval:  document.getElementById('interval').value,
          startTime: convertDateTime(document.getElementById('startTime').value),
          endTime:   convertDateTime(document.getElementById('endTime').value)
        };
  
        const response = await fetch('/api/export', {
          method:  'POST',
          headers: { 'Content-Type': 'application/json' },
          body:    JSON.stringify(requestData)
        });
  
        if (!response.ok) {
          const err = await response.json();
          throw new Error(err.message || 'Ошибка сервера');
        }

        const cdHeader = response.headers.get('Content-Disposition') || '';
        let filename   = `crypto_${requestData.symbol}_${requestData.interval}.xlsx`;
        if (cdHeader.includes('filename=')) {
          filename = cdHeader
            .split('filename=')[1]
            .replace(/['"]/g, '')
            .trim();
        }

        const blob = await response.blob();
        const url  = URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href    = url;
        link.download = filename;
        document.body.appendChild(link);
        link.click();
        link.remove();
        URL.revokeObjectURL(url);
  
        statusDiv.className   = 'status-message success';
        statusDiv.textContent = 'Файл успешно сгенерирован!';
      } catch (err) {
        statusDiv.className   = 'status-message error';
        statusDiv.textContent = `Ошибка: ${err.message}`;
      }
    });
  });

  function convertDateTime(htmlDateTime) {
    if (!htmlDateTime) throw new Error('Дата не указана');
    const d = new Date(htmlDateTime);
    if (isNaN(d)) throw new Error('Некорректный формат даты');
    const yyyy = d.getFullYear();
    const mm   = String(d.getMonth()+1).padStart(2, '0');
    const dd   = String(d.getDate()).padStart(2, '0');
    const hh   = String(d.getHours()).padStart(2, '0');
    const mi   = String(d.getMinutes()).padStart(2, '0');
    const ss   = String(d.getSeconds()).padStart(2, '0');
    return `${yyyy}-${mm}-${dd} ${hh}:${mi}:${ss}`;
  }