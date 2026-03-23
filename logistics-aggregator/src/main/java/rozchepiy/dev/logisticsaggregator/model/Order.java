package rozchepiy.dev.logisticsaggregator.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import rozchepiy.dev.logisticsaggregator.model.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@ToString(exclude = {"customer", "driver", "loaders"})
@EqualsAndHashCode(exclude = {"customer", "driver", "loaders"})
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private DriverProfile driver;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "order_loaders",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "loader_id")
    )
    private Set<LoaderProfile> loaders = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status = OrderStatus.CREATED;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;
}