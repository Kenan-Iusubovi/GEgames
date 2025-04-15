package ge.games.gegames.security.service;

import ge.games.gegames.Dto.user.request.UserLoginRequestDto;
import ge.games.gegames.Dto.user.request.UserRegistrationDto;
import ge.games.gegames.Dto.user.responce.UserDto;
import ge.games.gegames.entity.user.User;
import ge.games.gegames.security.exception.RestApiException;
import ge.games.gegames.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final String LOGIN_SUCCESS = "Login Successful";

    private final AuthenticationManager manager;

    private final JwtService jwtService;

    private final CookieService cookieService;

    private final UserService userService;



    public void logout(HttpServletResponse response){

        SecurityContextHolder.getContext().setAuthentication(null);
        response.addHeader(HttpHeaders.SET_COOKIE, cookieService.clearAccessTokenCookie().toString());
        response.addHeader(HttpHeaders.SET_COOKIE, cookieService.clearRefreshTokenCookie().toString());
    }

    public void processToken(HttpServletRequest request, HttpServletResponse response){

        jwtService.processToken(request, response);
    }

    public UserDto createUser(UserRegistrationDto dto){

        return userService.createUser(dto);
    }

    public UserDto login(UserLoginRequestDto request, HttpServletResponse response){

        try {
            Authentication auth = manager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getMail(), request.getPassword()
            ));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }catch (BadCredentialsException e){
            throw new RestApiException(HttpStatus.UNAUTHORIZED, "Invalid email or password please try again , " +
                    "or restore password with button RESTORE PASSWORD above");
        }

        final String accessToken = jwtService.generateAccessToken(request.getMail());
        final String refreshToken = jwtService.generateRefreshToken(request.getMail());

        cookieService.createCookie(response, JwtService.ACCESS_TOKEN_NAME, accessToken,
                "/", jwtService.getAccessTokenLifetime());
        cookieService.createCookie(response, jwtService.REFRESH_TOKEN_NAME, refreshToken,
                "/", jwtService.getRefreshTokenLifetime());

        response.setHeader("X-XSS-Protection", "1; mode=block");

        User user = userService.findUserByMail(request.getMail());

        return UserDto.from(user, LOGIN_SUCCESS);
    }
}
