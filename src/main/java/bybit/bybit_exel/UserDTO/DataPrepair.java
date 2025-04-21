package bybit.bybit_exel.UserDTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Data
public class DataPrepair {
    long start;
    long end;

    public void startTimePre (String startTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(startTime, formatter);
        this.start = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public void endTimePre (String endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(endTime, formatter);
        this.end = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public long intervalToMill (String interval) {
        return Integer.parseInt(interval) * 60000L;
    }

    public ArrayList<Long> promTime (String startTime, String endTime, String interval) {
        long inter = intervalToMill(interval);
        startTimePre(startTime);
        endTimePre(endTime);
        System.out.println(start + " " + end + " " + inter);
        ArrayList<Long> prom = new ArrayList<>();
        while (start < end) {
            prom.add(start);
            start = start + (inter * 1000);
        }
        prom.add(end);
        return prom;
    }
}