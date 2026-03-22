package rozchepiy.dev.logisticsaggregator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import rozchepiy.dev.logisticsaggregator.model.LoaderProfile;

public interface LoaderRepository extends JpaRepository<LoaderProfile,Long>, JpaSpecificationExecutor<LoaderProfile> {
}
