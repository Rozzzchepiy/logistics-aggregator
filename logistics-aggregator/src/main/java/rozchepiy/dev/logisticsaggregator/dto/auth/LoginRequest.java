package rozchepiy.dev.logisticsaggregator.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Email не може бути порожнім")
    @Email(message = "Некоректний формат Email")
    private String email;

    @NotBlank(message = "Пароль не може бути порожнім")
    private String password;
}