package ge.games.gegames.security;

import ge.games.gegames.security.service.CookieService;
import ge.games.gegames.security.details.AuthenticatedUsersService;
import ge.games.gegames.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static ge.games.gegames.security.service.JwtService.ACCESS_TOKEN_NAME;
import static ge.games.gegames.security.TokenTypeE.ACCESS;
import static ge.games.gegames.security.config.SecurityConfig.AUTH_WHITELIST;

@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CookieService cookieService;
    private final AuthenticatedUsersService authenticatedUsersService;
    private final UrlPathHelper urlPathHelper;
    private final PathMatcher pathMatcher;



    private void proccessTokenAthentication(HttpServletRequest request, String accessToken){
        final String username = jwtService.extractUsername(accessToken, ACCESS);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!username.isEmpty() && authentication != null){
            UserDetails userDetails = authenticatedUsersService.loadUserByUsername(username);
            if (jwtService.isTokenValid(accessToken, ACCESS)){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        String pathWithinApplication = urlPathHelper.getPathWithinApplication(request);
        boolean isFormWhiteList = Arrays.stream(AUTH_WHITELIST)
                .anyMatch(whiteListPath -> pathMatcher.match(whiteListPath, pathWithinApplication));

        if(isFormWhiteList){
            filterChain.doFilter(request,response);
            return;
        }

        Optional<String> accessToken = cookieService.getCookieValueByName(request, ACCESS_TOKEN_NAME);

        if (accessToken.isEmpty()){
            filterChain.doFilter(request, response);
            return;
        }

        proccessTokenAthentication(request, accessToken.get());

        filterChain.doFilter(request, response);
    }
}
