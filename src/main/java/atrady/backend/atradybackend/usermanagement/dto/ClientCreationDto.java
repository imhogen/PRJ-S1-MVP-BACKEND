package atrady.backend.atradybackend.usermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClientCreationDto {
    private String firsName;

    private String lastName;

    private String email;

    private String password;
}
