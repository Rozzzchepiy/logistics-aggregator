package rozchepiy.dev.logisticsaggregator.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import rozchepiy.dev.logisticsaggregator.model.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class OrderDTO {

    private Long id;

    @NotNull(message = "ID замовника є обов'язковим")
    private Long customerId;

    private Long driverId;

    private Set<Long> loaderIds;

    private String title;
    private String description;

    private OrderStatus status;

    @NotNull(message = "Вартість замовлення є обов'язковою")
    @Positive(message = "Вартість має бути більшою за нуль")
    private BigDecimal totalPrice;

    private String orderType;
    private java.time.LocalDateTime scheduledTime;

    private Double requiredCarVolume;
    private Double requiredCarWeight;
    private java.math.BigDecimal truckPricePerHour;

    private Integer requiredLoadersCount;
    private java.math.BigDecimal loaderPricePerHour;
    private Long driverUserId;
    private Set<Long> loaderUserIds;
}