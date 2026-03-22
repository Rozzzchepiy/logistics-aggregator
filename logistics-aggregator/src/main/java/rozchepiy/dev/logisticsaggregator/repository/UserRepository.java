package rozchepiy.dev.logisticsaggregator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import rozchepiy.dev.logisticsaggregator.model.User;

public interface UserRepository extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {
}
