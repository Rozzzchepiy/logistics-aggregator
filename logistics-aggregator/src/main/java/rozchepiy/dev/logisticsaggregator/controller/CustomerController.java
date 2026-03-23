package rozchepiy.dev.logisticsaggregator.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rozchepiy.dev.logisticsaggregator.dto.CustomerDTO;
import rozchepiy.dev.logisticsaggregator.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<CustomerDTO> createCustomerProfile(@PathVariable Long userId) {
        CustomerDTO createdProfile = customerService.createCustomerProfile(userId);
        return new ResponseEntity<>(createdProfile, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerProfile(@PathVariable Long id) {
        customerService.deleteCustomerProfile(id);
        return ResponseEntity.noContent().build();
    }
}