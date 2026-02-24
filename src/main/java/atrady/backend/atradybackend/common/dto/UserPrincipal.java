package atrady.backend.atradybackend.common.dto;

import atrady.backend.atradybackend.usermanagement.enums.UserRole;
import atrady.backend.atradybackend.usermanagement.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import jakarta.annotation.Nonnull;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserPrincipal {
    private String id;
    private String email;
    private UserRole role;
    private String telePhone;
    private String userName;
    private UserStatus status;
    private LocalDateTime lastLogin;
    private LocalDateTime dateCreated;
    private LocalDateTime lastModified;
}
