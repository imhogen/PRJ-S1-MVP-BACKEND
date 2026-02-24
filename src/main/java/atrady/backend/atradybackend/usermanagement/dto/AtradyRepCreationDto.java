package atrady.backend.atradybackend.usermanagement.dto;

import atrady.backend.atradybackend.usermanagement.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AtradyRepCreationDto {
    private String firstName;

    private String lastName;

    private String email;

    private UserRole role;
}
