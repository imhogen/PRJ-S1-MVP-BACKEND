package atrady.backend.atradybackend.usermanagement.entity;

import atrady.backend.atradybackend.usermanagement.enums.UserRole;
import atrady.backend.atradybackend.usermanagement.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "userentity")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserEntity {
    @Id
    private String id;

    private String firstName;

    private String lastName;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private String password;

    private LocalDateTime dateCreated;

    private LocalDateTime lastLogin;

    private LocalDateTime lastUpdated;
}
