package atrady.backend.atradybackend.usermanagement.dto;

import atrady.backend.atradybackend.usermanagement.entity.UserEntity;
import atrady.backend.atradybackend.usermanagement.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AtradyRepDataResponseDto {
    private String id;

    private String name;

    private String email;

    private UserRole userRole;

    private LocalDateTime lastLogin;

    public AtradyRepDataResponseDto(UserEntity user){
        this.id = user.getId();
        this.name = user.getFirstName().concat(" ").concat(user.getLastName());
        this.email = user.getEmail();
        this.userRole = user.getRole();
        this.lastLogin = user.getLastLogin();
    }
}
