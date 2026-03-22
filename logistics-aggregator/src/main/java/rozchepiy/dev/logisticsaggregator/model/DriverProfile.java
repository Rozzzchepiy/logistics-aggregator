package rozchepiy.dev.logisticsaggregator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "driver_profiles")
@Data
@NoArgsConstructor
public class DriverProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    @Column(name = "age")
    private Integer age;

    @Column(name = "car_brand")
    private String carBrand;

    @Column(name = "car_volume")
    private Double carVolume;

    @Column(name = "driver_rating")
    private Double driverRating = 5.0;
}