package bybit.bybit_exel.UserDTO;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GenExel {
    public File genExelFile(String symbol, String interval, LocalDateTime startTime, LocalDateTime endTime) {
        File exel = new File(symbol + ".xlsx");
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(symbol);
        Row headRow = sheet.createRow(0);
        headRow.createCell(0).setCellValue("time");
        headRow.createCell(1).setCellValue("open");
        headRow.createCell(2).setCellValue("close");
        headRow.createCell(3).setCellValue("max");
        headRow.createCell(4).setCellValue("min");
        BybitInfoTaker bybitInfoTaker = new BybitInfoTaker();
        DataPrepair dataPrepair = new DataPrepair();

        ArrayList<Long> times = dataPrepair.promTime(startTime, endTime);
        int localRow = 1;
        for(int i = 1; i < times.size(); i++) {
            List<?>[] info = bybitInfoTaker.getInfo(symbol, interval, times.get(i-1), times.get(i));
            for(int j = 0; j < info[0].size(); j++) {
                localRow++;
                Row nextRow = sheet.createRow(localRow);
                nextRow.createCell(0).setCellValue((String) info[0].get(j));
                nextRow.createCell(1).setCellValue((String) info[1].get(j));
                nextRow.createCell(2).setCellValue((String) info[2].get(j));
                nextRow.createCell(3).setCellValue((String) info[3].get(j));
                nextRow.createCell(4).setCellValue((String) info[4].get(j));
            }
        }
        try (FileOutputStream fileOut = new FileOutputStream(exel)) {
            workbook.write(fileOut);
            System.out.println("exel создан");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return exel;
    }
}
