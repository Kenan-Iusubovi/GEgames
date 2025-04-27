package ge.games.gegames.security.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class CookieService {

    public Optional<String> getCookieValueByName(@NonNull HttpServletRequest request, @NonNull String name){
        Cookie[] coockies = request.getCookies();

        if (coockies != null){
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(name))
                    .findFirst()
                    .map(Cookie::getValue);
        }

        return Optional.empty();
    }

    public void createCookie(HttpServletResponse response, String tokenName, String token, String path,
                             int lifetime){
        ResponseCookie cookie = ResponseCookie.from(tokenName, token)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(lifetime)
                .sameSite("Strict")
                .build();


        response.addHeader("Set-Cookie",cookie.toString());
    }

    public ResponseCookie clearRefreshTokenCookie(){
        return ResponseCookie.from(JwtService.REFRESH_TOKEN_NAME, "")
                .maxAge(0)
                .path("/")
                .httpOnly(true)
                .sameSite("Strict")
                .build();
    }

    public ResponseCookie clearAccessTokenCookie(){
        return ResponseCookie.from(JwtService.ACCESS_TOKEN_NAME, "")
                .maxAge(0)
                .path("/")
                .httpOnly(true)
                .sameSite("Strict")
                .build();
    }
}
