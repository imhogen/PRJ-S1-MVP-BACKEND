package atrady.backend.atradybackend.usermanagement.service;

import atrady.backend.atradybackend.common.Jwt.JwtToken;
import atrady.backend.atradybackend.usermanagement.dto.*;
import atrady.backend.atradybackend.usermanagement.entity.UserEntity;
import atrady.backend.atradybackend.usermanagement.enums.UserRole;
import atrady.backend.atradybackend.usermanagement.repository.UserRepository;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class UserAuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtToken tokenService;

    @Value("${firebase.password-reset.redirect-url}")
    private String resetRedirectUrl;

    public UserAuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtToken tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public LoginResponseDto signIn(SignInDto requestDto){
        log.info("request to sign in received from user  : {}", requestDto.getEmail());
        LoginResponseDto loginResponse = new LoginResponseDto();
        UserEntity user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(()->new RuntimeException("user not found"));
        log.info("user email valid. user retrieved successfully ");
    try {
        if (passwordEncoder.matches(user.getPassword(), requestDto.getPassword())) {
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
            if (user.getRole().equals(UserRole.admin) || user.getRole().equals(UserRole.attendant)) {
                    loginResponse.setUserData(new AtradyRepDataResponseDto(user));
                    loginResponse.setAccessToken(tokenService.generateAccessToken(user));
            } else if (user.getRole().equals(UserRole.client)) {
                user.setLastLogin(LocalDateTime.now());
                userRepository.save(user);
                    loginResponse.setUserData(new ClientDataResponseDto(user));
                    loginResponse.setAccessToken(tokenService.generateAccessToken(user));
                }
            } else {
            throw new RuntimeException("invalid password");
            }
            return loginResponse;
    }catch (Exception e){
        log.error("error logging user in {} ", e.getMessage());
        throw new RuntimeException("error logging user in");
    }
    }



    /**
     * Step 1: Generate a Firebase password reset link and send it via email.
     *
     * Why we generate the link on the backend instead of using Firebase's built-in email:
     * - Full control over email branding and template
     * - Can verify the user exists in OUR database first
     * - Can add rate limiting, logging, etc.
     */
    public boolean initiatePasswordReset(ForgotPasswordRequestDto request) {
        String email = request.getEmail().trim().toLowerCase();

        if (!userRepository.existsByEmail(email)) {
            log.warn("Password reset requested for non-existent email: {}", email);
            throw new RuntimeException("user does not exist");
        }

        try {
            ActionCodeSettings actionCodeSettings = ActionCodeSettings.builder()
                    .setUrl(resetRedirectUrl)
                    .setHandleCodeInApp(true)
                    .build();


            String resetLink = FirebaseAuth.getInstance()
                    .generatePasswordResetLink(email, actionCodeSettings);

            log.info("Password reset link generated for: {}", email);

            // write logic to push to email service to send the email
            return true;


        } catch (FirebaseAuthException e) {
            log.error("Firebase error generating reset link for {}: {}", email, e.getMessage());

            throw new RuntimeException("Unable to process password reset request");
        }
    }

    /**
     * Step 2: After the frontend completes the Firebase reset flow,
     * sync the new password to your own database.
     *
     * The frontend sends:
     * - The user's email
     * - The new password
     * - A Firebase ID token (proof they completed the flow)
     *
     * We verify the Firebase ID token to ensure the request is legitimate.
     */
    public boolean resetPassword(PasswordResetDto request) {
        try {

            FirebaseToken decodedToken = FirebaseAuth.getInstance()
                    .verifyIdToken(request.getFirebaseIdToken());

            String tokenEmail = decodedToken.getEmail();

            if (!tokenEmail.equalsIgnoreCase(request.getEmail())) {
                log.warn("Email mismatch: token={}, request={}", tokenEmail, request.getEmail());
                throw new SecurityException("Email verification failed");
            }
            UserEntity userEntity = userRepository.findByEmail(tokenEmail).orElseThrow(()-> new RuntimeException("user does not exist"));

            userEntity.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(userEntity);

            log.info("Password synced successfully for: {}", request.getEmail());
            return true;

        } catch (FirebaseAuthException e) {
            log.error("Firebase token verification failed: {}", e.getMessage());
            throw new SecurityException("Invalid or expired token");
        }
    }
}
