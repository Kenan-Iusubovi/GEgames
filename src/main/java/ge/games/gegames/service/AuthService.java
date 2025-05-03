package ge.games.gegames.service;

import ge.games.gegames.dto.user.request.UserLoginRequestDto;
import ge.games.gegames.dto.user.request.UserRegistrationDto;
import ge.games.gegames.dto.user.responce.UserDto;
import ge.games.gegames.security.exception.RestApiException;
import ge.games.gegames.security.auth.CookieService;
import ge.games.gegames.security.auth.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final String LOGIN_SUCCESS = "Login Successful";

    private final AuthenticationManager manager;

    private final JwtService jwtService;

    private final CookieService cookieService;

    private final UserService userService;



    public UserDto firebaseLogin(String token, HttpServletResponse response){

        UserDto user = userService.getUserByFirebaseToken(token);

        try {

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    user.getLogin(), null);
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (AuthenticationException e) {
            throw new RestApiException(HttpStatus.UNAUTHORIZED,
                    "We couldn't log you in with Google." +
                            " Please try again later or choose another login option.");
        }

        setHeaderAndCookies(user,response);

        return user;
    }

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
                request.getLogin(), request.getPassword()
            ));

            SecurityContextHolder.getContext().setAuthentication(auth);

        }catch (BadCredentialsException e){
            throw new RestApiException(HttpStatus.UNAUTHORIZED,
                    "Invalid email or password please try again , " +
                    "or restore password with button RESTORE PASSWORD above");
        }

        UserDto user = userService.getUserByLogin(request.getLogin());

        setHeaderAndCookies(user,response);
         return user;
    }

    private void setHeaderAndCookies(UserDto user, HttpServletResponse response){

        final String accessToken = jwtService.generateAccessToken(user.getLogin());
        final String refreshToken = jwtService.generateRefreshToken(user.getLogin());

        cookieService.createCookie(response, JwtService.ACCESS_TOKEN_NAME, accessToken,
                "/", jwtService.getAccessTokenLifetime());
        cookieService.createCookie(response, jwtService.REFRESH_TOKEN_NAME, refreshToken,
                "/", jwtService.getRefreshTokenLifetime());

        response.setHeader("X-XSS-Protection", "1; mode=block");

        user.setMessage(LOGIN_SUCCESS);
    }
}
