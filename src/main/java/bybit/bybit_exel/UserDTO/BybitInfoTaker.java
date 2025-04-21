package bybit.bybit_exel.UserDTO;

import lombok.Data;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class BybitInfoTaker {
    public List<?>[] getInfo (String symbol, String interval, long startTime, long endTime) {
        List<?>[] info = new List<?>[5];
        ArrayList<Long> times = new ArrayList<>();
        ArrayList<Double> open = new ArrayList<>();
        ArrayList<Double> close = new ArrayList<>();
        ArrayList<Double> max = new ArrayList<>();
        ArrayList<Double> min = new ArrayList<>();
        System.out.println(symbol + " " + interval + " " + startTime + " " + endTime);

        RestTemplate restTemplate = new RestTemplate();
        Response response = restTemplate.getForObject("api-testnet.bybit.com/v5/market/kline?" +
                "category=spot" +
                "&symbol=" + symbol +
                "&interval=" + interval +
                "&start=" + startTime +
                "&end=" + endTime +
                "&limit=1000", Response.class);
        if (response != null && response.getResult() != null && response.getResult().getList() != null) {
            for (List<String> candle : response.getResult().getList()) {
                if (candle.size() >= 5) {
                    times.add(Long.parseLong(candle.get(0)));
                    open.add(Double.parseDouble(candle.get(1)));
                    max.add(Double.parseDouble(candle.get(2)));
                    min.add(Double.parseDouble(candle.get(3)));
                    close.add(Double.parseDouble(candle.get(4)));
                }
            }
        }

        info[0] = times; times.clear();
        info[1] = open; open.clear();
        info[2] = close; close.clear();
        info[3] = max; max.clear();
        info[4] = min; min.clear();

        return info;
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