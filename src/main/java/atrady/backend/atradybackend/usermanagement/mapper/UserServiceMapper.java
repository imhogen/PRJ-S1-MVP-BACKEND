package atrady.backend.atradybackend.usermanagement.mapper;


import atrady.backend.atradybackend.common.dto.UserPrincipal;
import atrady.backend.atradybackend.usermanagement.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class UserServiceMapper {


    public static UserPrincipal convertToPrincipal(UserEntity user){
        return UserPrincipal.builder()
                .id(user.getId())
                .userName(user.getFirstName().concat(" ").concat(user.getLastName()))
                .role(user.getRole())
                .status(user.getStatus())
                .dateCreated(user.getDateCreated())
                .email(user.getEmail())
                .lastLogin(user.getLastLogin())
                .lastModified(user.getLastUpdated())
        .build();
    }
}
