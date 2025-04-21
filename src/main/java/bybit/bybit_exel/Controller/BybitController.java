package bybit.bybit_exel.Controller;

import bybit.bybit_exel.Bybit.BybitExamSym;
import bybit.bybit_exel.UserDTO.GenExel;
import bybit.bybit_exel.UserDTO.SavingData;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RestController
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
    @PostMapping(
            value = "/api/export",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<InputStreamResource> exportFile(@RequestBody SavingData sd)
            throws FileNotFoundException {
        File exel = new GenExel()
                .genExelFile(
                        sd.getSymbol(),
                        sd.getInterval(),
                        sd.getStartTime(),
                        sd.getEndTime()
                );

        InputStreamResource resource =
                new InputStreamResource(new FileInputStream(exel));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename(exel.getName())
                        .build()
        );
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(exel.length())
                .body(resource);
    }
}
