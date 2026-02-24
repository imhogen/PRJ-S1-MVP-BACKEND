package atrady.backend.atradybackend.common.exception.globalExceptionHandler;

public class InvalidJwtToken extends RuntimeException{
    public InvalidJwtToken(String message){
        super(message);
    }
}
