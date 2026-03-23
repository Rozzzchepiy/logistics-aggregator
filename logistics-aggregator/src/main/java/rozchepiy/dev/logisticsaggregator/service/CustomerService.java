package rozchepiy.dev.logisticsaggregator.service;

import rozchepiy.dev.logisticsaggregator.dto.CustomerDTO;
import java.util.List;

public interface CustomerService {
    List<CustomerDTO> getAllCustomers();
    CustomerDTO getCustomerById(Long id);
    CustomerDTO createCustomerProfile(Long userId);
    void deleteCustomerProfile(Long id);
}