package rozchepiy.dev.logisticsaggregator.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DriverDTO {

    private Long id;

    @NotNull(message = "User ID не може бути порожнім")
    private Long userId;

    private String name;
    private String number;

    @NotNull(message = "Вік є обов'язковим полем")
    @Min(value = 18, message = "Водію має бути щонайменше 18 років")
    @Max(value = 65, message = "Вік водія не може перевищувати 65 років")
    private Integer age;

    @NotBlank(message = "Марка авто не може бути порожньою")
    @Size(min = 2, max = 100, message = "Назва марки авто має містити від 2 до 100 символів")
    private String carBrand;

    @NotNull(message = "Об'єм кузова є обов'язковим полем")
    @Positive(message = "Об'єм кузова має бути додатнім числом")
    private Double carVolume;

    private Double driverRating;
}