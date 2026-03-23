package rozchepiy.dev.logisticsaggregator.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rozchepiy.dev.logisticsaggregator.dto.LoaderDTO;
import rozchepiy.dev.logisticsaggregator.service.LoaderService;

import java.util.List;

@RestController
@RequestMapping("/api/loaders")
@RequiredArgsConstructor
public class LoaderController {

    private final LoaderService loaderService;

    @GetMapping
    public ResponseEntity<List<LoaderDTO>> getAllLoaders() {
        return ResponseEntity.ok(loaderService.getAllLoaders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoaderDTO> getLoaderById(@PathVariable Long id) {
        return ResponseEntity.ok(loaderService.getLoaderById(id));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<LoaderDTO> createLoaderProfile(
            @PathVariable Long userId,
            @Valid @RequestBody LoaderDTO loaderDTO) {
        LoaderDTO createdProfile = loaderService.createLoaderProfile(userId, loaderDTO);
        return new ResponseEntity<>(createdProfile, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoaderDTO> updateLoaderProfile(
            @PathVariable Long id,
            @Valid @RequestBody LoaderDTO loaderDTO) {
        return ResponseEntity.ok(loaderService.updateLoaderProfile(id, loaderDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoaderProfile(@PathVariable Long id) {
        loaderService.deleteLoaderProfile(id);
        return ResponseEntity.noContent().build();
    }
}