package bybit.bybit_exel.UserDTO;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Data
@RestController
public class SavingData {
    private String symbol;
    private String interval;
    private String startTime;
    private String endTime;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/api/export")
    public ResponseEntity<String> handleJson(@RequestBody Map<String, Object> jsonData) {
        try {
            this.symbol = (String) jsonData.get("symbol");
            this.interval = (String) jsonData.get("interval");
            this.startTime = parseDateTime(jsonData.get("startTime")).toString().replace("T", " ");
            this.endTime = parseDateTime(jsonData.get("endTime")).toString().replace("T", " ");
            System.out.println(symbol + " " +
                    interval + " " +
                    startTime + " " +
                    endTime);
            return ResponseEntity.ok("Данные успешно сохранены");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка формата данных");
        }
    }

    private LocalDateTime parseDateTime(Object dateValue) {
        if (dateValue instanceof String) {
            return LocalDateTime.parse((String) dateValue, formatter);
        }
        throw new IllegalArgumentException("Неподдерживаемый формат даты");
    }
}
