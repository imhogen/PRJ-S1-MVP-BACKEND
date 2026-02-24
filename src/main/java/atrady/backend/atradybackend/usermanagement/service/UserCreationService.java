package atrady.backend.atradybackend.usermanagement.service;

import atrady.backend.atradybackend.usermanagement.dto.AtradyRepCreationDto;
import atrady.backend.atradybackend.usermanagement.dto.ClientCreationDto;
import atrady.backend.atradybackend.usermanagement.entity.UserEntity;
import atrady.backend.atradybackend.usermanagement.enums.UserRole;
import atrady.backend.atradybackend.usermanagement.enums.UserStatus;
import atrady.backend.atradybackend.usermanagement.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static atrady.backend.atradybackend.common.util.JsonUtil.toJson;

@Service
@Slf4j
public class UserCreationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public UserCreationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String createClient(ClientCreationDto request){
        log.info("request to create client received {}", toJson(request));
        UserEntity entity = new UserEntity();

        try {
            entity.setEmail(request.getEmail());

            entity.setFirstName(request.getFirsName());
            entity.setLastName(request.getLastName());
            entity.setRole(UserRole.client);
            entity.setPassword(passwordEncoder.encode(request.getPassword()));
            entity.setStatus(UserStatus.inactive);
            //send email to user's email
            userRepository.save(entity);
            return "user created successfully";
        }catch (Exception e){
            log.error("error creating client account : {}", e.getMessage());
            throw new RuntimeException("error creating account");
        }
    }

    public String createAtradyUser(AtradyRepCreationDto request){
        log.info("request to create Atrady rep received {}", toJson(request));
        UserEntity entity = new UserEntity();

        try {
            entity.setEmail(request.getEmail());
            entity.setFirstName(request.getFirstName());
            entity.setLastName(request.getLastName());
            entity.setRole(UserRole.client);
            entity.setStatus(UserStatus.inactive);
            //generate a temporary password for user and send later to user's email
            //send email to user's email
            userRepository.save(entity);
            return "user created successfully";
        }catch (Exception e){
            log.error("error creating atrady user account : {}", e.getMessage());
            throw new RuntimeException("error creating account");
        }
    }


}
