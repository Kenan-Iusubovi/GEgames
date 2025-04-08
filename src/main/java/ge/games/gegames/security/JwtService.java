package ge.games.gegames.security;

import io.jsonwebtoken.Claims;
import ge.games.gegames.security.details.AuthenticatedUsersService;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    public static final String REFRESH_TOKEN_NAME = "GEgamesRefreshToken";
    public static final String ACCESS_TOKEN_NAME = "GEgamesAccessToken";

    @Value("${jwt.at.secret}")
    private String jwtAccessSecret;

    @Value("${jwt.rt.secret}")
    private String jwtRefreshSecret;

    @Value("${jwt.at.lifetime")
    private int accessTokenLifetime;

    @Value("${jwt.rt.lifetime")
    private int refreshTokenLifetime;

    private final AuthenticatedUsersService authenticatedUsersService;
    private final CookieService cookieService;


    private Claims extractAllClaims(String token, String secret){
        try {
            return Jwts.parser().verifyWith(get)
        }
    }

    public <T> T extractClaim(String token, Function< Claims, T> claimResolver, String secret){
        final Claims claims = extra(token, secret);
    }

    private String getSecret(TokenTypeE tokenTypeE){
        if (tokenTypeE.REFRESH.equals(tokenTypeE)){
            return jwtRefreshSecret;
        }

        return jwtAccessSecret;
    }

    public String extractUsername(String token, TokenTypeE tokenTypeE){
        String secret = getSecret(tokenTypeE);
        return
    }
}
