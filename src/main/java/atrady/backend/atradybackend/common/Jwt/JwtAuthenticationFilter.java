package atrady.backend.atradybackend.common.Jwt;


import atrady.backend.atradybackend.common.dto.UserPrincipal;
import atrady.backend.atradybackend.common.exception.globalExceptionHandler.InvalidJwtToken;
import atrady.backend.atradybackend.usermanagement.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static atrady.backend.atradybackend.usermanagement.mapper.UserServiceMapper.convertToPrincipal;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Value("${jwt.secret}")
    String secretString;

    private final JwtToken tokenService;

    private final UserRepository userRepository;


    public JwtAuthenticationFilter(JwtToken tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }



    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {


        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);

            return;
        }
       try{
           String jwtToken = request.getHeader("Authorization");
           if(tokenService.validateToken(jwtToken, secretString)){
                log.info("retrieving user principal");
                UserPrincipal userPrincipal = convertToPrincipal(userRepository.findById(tokenService.extractId(jwtToken, secretString)).orElseThrow(()->new InvalidJwtToken("Invalid Token. User Cannot be found")));

                log.info("user principal retrieved");
                List<SimpleGrantedAuthority> authority = Collections.singletonList(new SimpleGrantedAuthority(String.valueOf((userPrincipal.getRole()))));
                log.info("user security context formed : {}", authority);
               UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null, authority);

               SecurityContextHolder.getContext().setAuthentication(authentication);
               request.getSession(true);
               filterChain.doFilter(request, response);

           }
       } catch(Exception e) {
           log.error("Could not extract user due invalid token", e);
           throw new InvalidJwtToken("Token Validation Error : " + e);
       }

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        String path = request.getServletPath();
        log.info("Processing request for path: {}", path);
        return path.startsWith("atrady/user/account/client/sign-up")||
                path.startsWith("atrady/user/account/staff/create")||
                path.equals("atrady/user/account/sign-in")||
                path.equals("atrady/user/account/forgot-password")||
                path.equals("atrady/user/account/reset-password");

    }

}
