package rozchepiy.dev.logisticsaggregator.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rozchepiy.dev.logisticsaggregator.dto.auth.AuthResponse;
import rozchepiy.dev.logisticsaggregator.dto.auth.LoginRequest;
import rozchepiy.dev.logisticsaggregator.dto.auth.RegisterRequest;
import rozchepiy.dev.logisticsaggregator.model.*;
import rozchepiy.dev.logisticsaggregator.model.enums.Role;
import rozchepiy.dev.logisticsaggregator.repository.UserRepository;
import rozchepiy.dev.logisticsaggregator.service.AuthService;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Користувач з таким email вже існує");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setNumber(request.getNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // 1. Встановлюємо обрані ролі в сутність User
        Set<Role> selectedRoles = request.getRoles();
        user.setRoles(selectedRoles);

        // 2. Ініціалізуємо відповідні профілі залежно від обраних ролей
        if (selectedRoles.contains(Role.CUSTOMER)) {
            CustomerProfile customerProfile = new CustomerProfile();
            customerProfile.setUser(user);
            user.setCustomerProfile(customerProfile);
        }

        if (selectedRoles.contains(Role.DRIVER)) {
            DriverProfile driverProfile = new DriverProfile();
            driverProfile.setUser(user);
            user.setDriverProfile(driverProfile);
        }

        if (selectedRoles.contains(Role.LOADER)) {
            LoaderProfile loaderProfile = new LoaderProfile();
            loaderProfile.setUser(user);
            user.setLoaderProfile(loaderProfile);
        }

        User savedUser = userRepository.save(user);

        String jwtToken = jwtService.generateToken(savedUser);

        return new AuthResponse(jwtToken, savedUser.getId(), "Реєстрація успішна");
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Користувача не знайдено"));

        String jwtToken = jwtService.generateToken(user);

        return new AuthResponse(jwtToken, user.getId(), "Вхід успішний");
    }
}