package rozchepiy.dev.logisticsaggregator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import rozchepiy.dev.logisticsaggregator.model.DriverProfile;

import java.sql.Driver;

public interface DriverProfileRepository extends JpaRepository<DriverProfile,Long>, JpaSpecificationExecutor<Driver> {
}
