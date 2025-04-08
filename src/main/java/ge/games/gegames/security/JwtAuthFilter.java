package ge.games.gegames.security;

import ge.games.gegames.security.details.AuthenticatedUsersService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;

import java.io.IOException;
import java.nio.file.PathMatcher;

@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CookieService cookieService;
    private final AuthenticatedUsersService authenticatedUsersService;
    private final UrlPathHelper urlPathHelper;
    private final PathMatcher pathMatcher;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String pathWithinApplication = urlPathHelper
    }
}
