package rozchepiy.dev.logisticsaggregator.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rozchepiy.dev.logisticsaggregator.dto.DriverDTO;
import rozchepiy.dev.logisticsaggregator.service.DriverService;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @GetMapping
    public ResponseEntity<List<DriverDTO>> getAllDrivers() {
        return ResponseEntity.ok(driverService.getAllDrivers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverDTO> getDriverById(@PathVariable Long id) {
        return ResponseEntity.ok(driverService.getDriverById(id));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<DriverDTO> createDriverProfile(
            @PathVariable Long userId,
            @Valid @RequestBody DriverDTO driverDTO) {
        DriverDTO createdProfile = driverService.createDriverProfile(userId, driverDTO);
        return new ResponseEntity<>(createdProfile, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DriverDTO> updateDriverProfile(
            @PathVariable Long id,
            @Valid @RequestBody DriverDTO driverDTO) {
        return ResponseEntity.ok(driverService.updateDriverProfile(id, driverDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriverProfile(@PathVariable Long id) {
        driverService.deleteDriverProfile(id);
        return ResponseEntity.noContent().build();
    }
}