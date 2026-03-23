package rozchepiy.dev.logisticsaggregator.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rozchepiy.dev.logisticsaggregator.dto.CustomerDTO;
import rozchepiy.dev.logisticsaggregator.dto.DriverDTO;
import rozchepiy.dev.logisticsaggregator.dto.LoaderDTO;
import rozchepiy.dev.logisticsaggregator.exception.AlreadyExistException;
import rozchepiy.dev.logisticsaggregator.exception.NotFoundException;
import rozchepiy.dev.logisticsaggregator.model.CustomerProfile;
import rozchepiy.dev.logisticsaggregator.model.DriverProfile;
import rozchepiy.dev.logisticsaggregator.model.User;
import rozchepiy.dev.logisticsaggregator.model.enums.Role;
import rozchepiy.dev.logisticsaggregator.repository.CustomerProfileRepository;
import rozchepiy.dev.logisticsaggregator.repository.UserRepository;
import rozchepiy.dev.logisticsaggregator.service.CustomerService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerProfileRepository customerRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        CustomerProfile customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Профіль замовника не знайдено з ID: " + id));
        return convertToDto(customer);
    }

    @Override
    @Transactional
    public CustomerDTO createCustomerProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Користувача не знайдено з ID: " + userId));

        if (user.getCustomerProfile() != null) {
            throw new AlreadyExistException("У цього користувача вже є профіль замовника");
        }

        user.getRoles().add(Role.CUSTOMER);

        CustomerProfile customerProfile = new CustomerProfile();
        customerProfile.setUser(user);
        customerProfile.setCustomerRating(5.0);
        CustomerProfile savedProfile = customerRepository.save(customerProfile);
        userRepository.save(user);

        return convertToDto(savedProfile);
    }

    @Override
    @Transactional
    public void deleteCustomerProfile(Long id) {
        CustomerProfile profile = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Профіль замовника не знайдено з ID: " + id));

        User user = profile.getUser();
        user.getRoles().remove(Role.CUSTOMER);
        userRepository.save(user);

        customerRepository.delete(profile);
    }

    private CustomerDTO convertToDto(CustomerProfile customer) {
        CustomerDTO dto = modelMapper.map(customer, CustomerDTO.class);

        if (customer.getUser() != null) {
            dto.setName(customer.getUser().getName());
            dto.setNumber(customer.getUser().getNumber());
        }

        return dto;
    }
}