package bybit.bybit_exel.UserDTO;

import lombok.Data;

@Data
public class SavingData {
    private String symbol;
    private String interval;
    private String startTime;
    private String endTime;
}
