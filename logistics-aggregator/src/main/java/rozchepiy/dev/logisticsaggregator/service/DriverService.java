package rozchepiy.dev.logisticsaggregator.service;

import rozchepiy.dev.logisticsaggregator.dto.DriverDTO;
import java.util.List;

public interface DriverService {
    List<DriverDTO> getAllDrivers();
    DriverDTO getDriverById(Long id);
    DriverDTO createDriverProfile(Long userId, DriverDTO driverDTO);
    DriverDTO updateDriverProfile(Long id, DriverDTO driverDTO);
    void deleteDriverProfile(Long id);
}