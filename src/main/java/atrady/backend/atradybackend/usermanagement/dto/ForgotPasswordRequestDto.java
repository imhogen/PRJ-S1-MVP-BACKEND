package atrady.backend.atradybackend.usermanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ForgotPasswordRequestDto {
    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
}
