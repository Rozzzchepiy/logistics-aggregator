package rozchepiy.dev.logisticsaggregator.service;

import rozchepiy.dev.logisticsaggregator.dto.auth.AuthResponse;
import rozchepiy.dev.logisticsaggregator.dto.auth.LoginRequest;
import rozchepiy.dev.logisticsaggregator.dto.auth.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}