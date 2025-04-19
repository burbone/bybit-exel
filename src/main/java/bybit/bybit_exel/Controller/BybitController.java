package bybit.bybit_exel.Controller;

import bybit.bybit_exel.Bybit.BybitExamSym;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
