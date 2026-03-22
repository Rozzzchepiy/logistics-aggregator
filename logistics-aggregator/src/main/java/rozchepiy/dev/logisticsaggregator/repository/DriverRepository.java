package rozchepiy.dev.logisticsaggregator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.sql.Driver;

public interface DriverRepository extends JpaRepository<Driver,Long>, JpaSpecificationExecutor<Driver> {
}
