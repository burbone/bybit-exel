package bybit.bybit_exel.Bybit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class BybitExamSym {
    public static boolean checkSymbol(String symbol) {
        boolean symbolHas = false;
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api-testnet.bybit.com/v5/market/instruments-info?category=spot&symbol="
                            + symbol))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.body());

                JsonNode listNode = rootNode.path("result").path("list");
                if (listNode.isArray() && listNode.size() > 0) {
                    symbolHas = true;
                } else {
                    symbolHas = false;
                }
            } else {
                System.out.println("Ошибка: " + response.statusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(symbolHas);
        return symbolHas;
    }
}
