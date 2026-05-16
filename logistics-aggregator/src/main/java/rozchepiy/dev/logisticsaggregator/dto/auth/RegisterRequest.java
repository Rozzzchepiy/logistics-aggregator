package rozchepiy.dev.logisticsaggregator.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import rozchepiy.dev.logisticsaggregator.model.enums.Role;

import java.util.Set;

@Data
public class RegisterRequest {

    @NotBlank(message = "Ім'я не може бути порожнім")
    private String name;

    @NotBlank(message = "Email не може бути порожнім")
    @Email(message = "Некоректний формат Email")
    private String email;

    @NotBlank(message = "Номер телефону не може бути порожнім")
    private String number;

    @NotBlank(message = "Пароль є обов'язковим")
    @Size(min = 6, message = "Пароль має містити мінімум 6 символів")
    private String password;

    @NotEmpty(message = "Потрібно обрати хоча б одну роль")
    private Set<Role> roles;
}