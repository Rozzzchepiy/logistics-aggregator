package rozchepiy.dev.logisticsaggregator.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rozchepiy.dev.logisticsaggregator.dto.DriverDTO;
import rozchepiy.dev.logisticsaggregator.exception.AlreadyExistException;
import rozchepiy.dev.logisticsaggregator.exception.NotFoundException;
import rozchepiy.dev.logisticsaggregator.model.DriverProfile;
import rozchepiy.dev.logisticsaggregator.model.User;
import rozchepiy.dev.logisticsaggregator.model.enums.Role;
import rozchepiy.dev.logisticsaggregator.repository.DriverProfileRepository;
import rozchepiy.dev.logisticsaggregator.repository.UserRepository;
import rozchepiy.dev.logisticsaggregator.service.DriverService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverProfileRepository driverRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<DriverDTO> getAllDrivers() {
        return driverRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public DriverDTO getDriverById(Long id) {
        DriverProfile driver = driverRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Профіль водія не знайдено з ID: " + id));
        return convertToDto(driver);
    }

    @Override
    @Transactional
    public DriverDTO createDriverProfile(Long userId, DriverDTO driverDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Користувача не знайдено з ID: " + userId));

        if (user.getDriverProfile() != null) {
            throw new AlreadyExistException("У цього користувача вже є профіль водія");
        }

        user.getRoles().add(Role.DRIVER);

        DriverProfile driverProfile = modelMapper.map(driverDTO, DriverProfile.class);
        driverProfile.setUser(user);
        driverProfile.setDriverRating(5.0);

        DriverProfile savedProfile = driverRepository.save(driverProfile);
        userRepository.save(user);

        return convertToDto(savedProfile);
    }

    @Override
    @Transactional
    public DriverDTO updateDriverProfile(Long id, DriverDTO driverDTO) {
        DriverProfile existingProfile = driverRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Профіль водія не знайдено з ID: " + id));

        existingProfile.setAge(driverDTO.getAge());
        existingProfile.setCarBrand(driverDTO.getCarBrand());
        existingProfile.setCarVolume(driverDTO.getCarVolume());

        DriverProfile updatedProfile = driverRepository.save(existingProfile);
        return convertToDto(updatedProfile);
    }

    @Override
    @Transactional
    public void deleteDriverProfile(Long id) {
        DriverProfile profile = driverRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Профіль водія не знайдено з ID: " + id));

        User user = profile.getUser();
        user.getRoles().remove(Role.DRIVER);
        userRepository.save(user);

        driverRepository.delete(profile);
    }

    private DriverDTO convertToDto(DriverProfile driver) {
        DriverDTO dto = modelMapper.map(driver, DriverDTO.class);

        if (driver.getUser() != null) {
            dto.setName(driver.getUser().getName());
            dto.setNumber(driver.getUser().getNumber());
        }

        return dto;
    }
}