package rozchepiy.dev.logisticsaggregator.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rozchepiy.dev.logisticsaggregator.dto.auth.AuthResponse;
import rozchepiy.dev.logisticsaggregator.dto.auth.LoginRequest;
import rozchepiy.dev.logisticsaggregator.dto.auth.RegisterRequest;
import rozchepiy.dev.logisticsaggregator.service.AuthService; // Вам потрібно буде створити цей інтерфейс/клас

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}