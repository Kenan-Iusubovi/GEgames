package ge.games.gegames.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
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

    public void createCookie(HttpServletResponse response, String name, String token, String path, boolean isHttpOnly,
                             int lifetime){
        Cookie cookie = new Cookie(name, token);
        cookie.setPath(path);
        cookie.setHttpOnly(isHttpOnly);
        cookie.setMaxAge(lifetime);
        response.addCookie(cookie);
    }
}
