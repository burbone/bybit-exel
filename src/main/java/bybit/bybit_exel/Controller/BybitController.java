package bybit.bybit_exel.Controller;

import bybit.bybit_exel.Bybit.BybitExamSym;
import bybit.bybit_exel.UserDTO.GenExel;
import bybit.bybit_exel.UserDTO.SavingData;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

@Controller
public class BybitController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/check-symbol")
    public ResponseEntity<String> checkSymbol(@RequestParam String symbol) {
        boolean isValid = BybitExamSym.checkSymbol(symbol);
        return ResponseEntity.ok(Boolean.toString(isValid));
    }

    @PostMapping("/export")
    public ResponseEntity<InputStreamResource> exportFile() throws FileNotFoundException {
        GenExel genExel = new GenExel();
        SavingData savingData = new SavingData();
        File exel = genExel.genExelFile(savingData.getSymbol(),
                savingData.getInterval(),
                savingData.getStartTime(),
                savingData.getEndTime());
        InputStreamResource resource = new InputStreamResource(new FileInputStream(exel));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + exel.getName() + "\"")
                .contentLength(exel.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
    @PostMapping("/api/export")
    public ResponseEntity<String> handleJson(@RequestBody Map<String, Object> jsonData) {
        SavingData savingData = new SavingData();
        try {
            savingData.setSymbol((String) jsonData.get("symbol"));
            savingData.setInterval((String) jsonData.get("interval"));
            savingData.setStartTime(savingData.parseDateTime(jsonData.get("startTime")));
            savingData.setEndTime(savingData.parseDateTime(jsonData.get("endTime")));
            return ResponseEntity.ok("Данные успешно сохранены");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка формата данных");
        }
    }
}
