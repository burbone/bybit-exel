package bybit.bybit_exel.UserDTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class SavingData {
    private String symbol;
    private String interval;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LocalDateTime parseDateTime(Object dateValue) {
        if (dateValue instanceof String) {
            return LocalDateTime.parse((String) dateValue, formatter);
        }
        throw new IllegalArgumentException("Неподдерживаемый формат даты");
    }
}
