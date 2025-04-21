package bybit.bybit_exel.UserDTO;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class BybitInfoTaker {
    public List<?>[] getInfo (String symbol, String interval, long startTime, long endTime) {
        ArrayList<String> times = new ArrayList<>();
        ArrayList<String> open = new ArrayList<>();
        ArrayList<String> close = new ArrayList<>();
        ArrayList<String> max = new ArrayList<>();
        ArrayList<String> min = new ArrayList<>();
        System.out.println(symbol + " " + interval + " " + startTime + " " + endTime);
        String start = String.valueOf(startTime);
        String end = String.valueOf(endTime);
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api-testnet.bybit.com/v5/market/kline" +
                            "?category=spot" +
                            "&symbol=" + symbol +
                            "&interval=" + interval +
                            "&start=" + start +
                            "&end=" + end +
                            "&limit=1000"
                    ))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.body());
                JsonNode listNode = rootNode.path("result").path("list");
                for (JsonNode candle : listNode) {
                    times.add(String.valueOf(candle.get(0)));
                    open.add(String.valueOf(candle.get(1).asDouble()));
                    max.add(String.valueOf(candle.get(2).asDouble()));
                    min.add(String.valueOf(candle.get(3).asDouble()));
                    close.add(String.valueOf(candle.get(4).asDouble()));
                }
            }
        } catch (JsonMappingException e) {
            System.out.println("1 error");
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            System.out.println("2 error");
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("3 error");
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            System.out.println("4 error");
            throw new RuntimeException(e);
        }
        return new List<?>[]{ times, open, close, max, min };
    }
        @Data
    class Response {
        private int retCode;
        private String retMsg;
        private Result result;
    }

    @Data
    class Result {
        private String category;
        private String symbol;
        private List<List<String>> list;
    }
}