package rozchepiy.dev.logisticsaggregator.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerDTO {

    private Long id;

    @NotNull(message = "User ID не може бути порожнім")
    private Long userId;

    private String name;
    private String number;

    private Double customerRating;
}