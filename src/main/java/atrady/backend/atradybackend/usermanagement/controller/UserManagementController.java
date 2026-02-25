package atrady.backend.atradybackend.usermanagement.controller;

import atrady.backend.atradybackend.usermanagement.dto.*;
import atrady.backend.atradybackend.usermanagement.service.UserAuthenticationService;
import atrady.backend.atradybackend.usermanagement.service.UserCreationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("atrady/user/account/")
public class UserManagementController {
    private final UserCreationService userCreationService;

    private final UserAuthenticationService userAuthenticationService;

    public UserManagementController(UserCreationService userCreationService, UserAuthenticationService userAuthenticationService) {
        this.userCreationService = userCreationService;
        this.userAuthenticationService = userAuthenticationService;
    }

    @PostMapping("client/sign-up")
    private ResponseEntity<?> clientSignUp(@RequestBody ClientCreationDto request){
        return new ResponseEntity<>(userCreationService.createClient(request), HttpStatus.OK);}

    @PostMapping("staff/create")
    private ResponseEntity<?> clientSignUp(@RequestBody AtradyRepCreationDto request){
        return new ResponseEntity<>(userCreationService.createAtradyUser(request), HttpStatus.OK);}

    @PostMapping("sign-in")
    private ResponseEntity<?> signIn(@RequestBody SignInDto request){
        return new ResponseEntity<>(userAuthenticationService.signIn(request), HttpStatus.OK);}
    
    
    @PostMapping("forgot-password")
    private ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequestDto request){
        return new ResponseEntity<>(userAuthenticationService.initiatePasswordReset(request), HttpStatus.OK);
    }

    @PostMapping("reset-password")
    private ResponseEntity<?> resetPassword(@RequestBody PasswordResetDto request){
        return new ResponseEntity<>(userAuthenticationService.resetPassword(request), HttpStatus.OK);
    }

}
