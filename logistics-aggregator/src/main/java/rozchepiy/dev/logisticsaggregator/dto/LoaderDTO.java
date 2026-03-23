package rozchepiy.dev.logisticsaggregator.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoaderDTO {
    private Long id;

    @NotNull(message = "User ID не може бути порожнім")
    private Long userId;
    private String name;
    private String number;

    @NotNull(message = "Вік є обов'язковим полем")
    @Min(value = 18, message = "Вантажнику має бути щонайменше 18 років")
    @Max(value = 65, message = "Вік вантажника не може перевищувати 65 років")
    private Integer age;

    @NotNull(message = "Ріст є обов'язковим полем")
    @Min(value = 150, message = "Ріст вантажника має бути щонайменше 150 см")
    @Min(value = 230, message = "Ріст вантажника не може перевищувати 230 см")
    private Integer height;

    @NotNull(message = "Вага є обов'язковим полем")
    @Min(value = 50, message = "Вага вантажника має бути щонайменше 50 кг")
    @Max(value = 50, message = "Вага вантажника не може перевищувати 150 кг")
    private Double weight;
    private Double loaderRating;
}