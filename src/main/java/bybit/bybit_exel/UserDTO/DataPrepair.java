package bybit.bybit_exel.UserDTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Data
public class DataPrepair {
    SavingData savingData;
    long start;
    long end;

    public void startTimePre (LocalDateTime startTime) {
        String start = startTime.toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(start, formatter);
        long startMill = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
        this.start = startMill;
    }

    public void endTimePre (LocalDateTime endTime) {
        String start = endTime.toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(start, formatter);
        long endMill = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
        this.end = endMill;
    }

    public long intervalToMill () {
        long inter = 0;
        switch (savingData.getInterval()) {
            case "1" : inter = 60000;
            case "3" : inter = 3 * 60000;
            case "5" : inter = 5 * 60000;
            case "15" : inter = 15 * 60000;
            case "30" : inter = 30 * 60000;
            case "60" : inter = 60 * 60000;
            case "120" : inter = 120 * 60000;
            case "240" : inter = 240 * 60000;
            case "360" : inter = 360 * 60000;
            case "720" : inter = 720 * 60000;
            default: inter = 0;
        }
        return inter;
    }

    public ArrayList<Long> promTime (LocalDateTime startTime, LocalDateTime endTime) {
        long inter = intervalToMill();
        startTimePre(startTime);
        endTimePre(endTime);
        ArrayList<Long> prom = new ArrayList<>();
        while (start <= end) {
            prom.add(start);
            start = start + (inter * 1000);
        }
        return prom;
    }
}
