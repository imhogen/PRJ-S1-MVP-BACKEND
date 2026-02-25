package atrady.backend.atradybackend.common.exception.globalExceptionHandler;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message){
        super(message);
    }
}
