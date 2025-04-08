package ge.games.gegames.security;

import ge.games.gegames.security.exception.RestApiException;
import io.jsonwebtoken.*;
import ge.games.gegames.security.details.AuthenticatedUsersService;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
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



    public String createToken(String mail, String secret, int leftTime){
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expirationTime = currentTime.plusSeconds(leftTime);

        return Jwts.builder()
                .subject(mail)
                .issuedAt(Date.from(currentTime.toInstant(ZoneOffset.UTC)))
                .expiration(Date.from(expirationTime.toInstant(ZoneOffset.UTC)))
                .signWith(getSignInKey(secret), Jwts.SIG.HS256)
                .compact();
    }

    public String generateAccessToken(String mail){
        return createToken(mail,jwtAccessSecret, accessTokenLifetime);
    }

    public String generateRefreshToken(String mail){
        return createToken(mail, jwtRefreshSecret, refreshTokenLifetime);
    }

    private SecretKey getSignInKey(String secret){
        byte [] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token, String secret) {
        try {
            return Jwts.parser().verifyWith(getSignInKey(secret)).build().parseSignedClaims(token).getPayload();
        } catch (SignatureException e) {
            throw new RestApiException(HttpStatus.UNAUTHORIZED, "Invalid JWT signature");
        } catch (MalformedJwtException e) {
            throw new RestApiException(HttpStatus.UNAUTHORIZED, "Invalid JWT token");
        } catch (UnsupportedJwtException e) {
            throw new RestApiException(HttpStatus.UNAUTHORIZED, "Unsupported JWT token");
        } catch (ExpiredJwtException e) {
            throw new RestApiException(HttpStatus.UNAUTHORIZED, "Expired JWT token");
        } catch (IllegalArgumentException e) {
            throw new RestApiException(HttpStatus.UNAUTHORIZED, "JWT claims string is empty");
        }
    }

    public <T> T extractClaim(String token, Function< Claims, T> claimResolver, String secret){
        final Claims claims = extractAllClaims(token, secret);
        return claimResolver.apply(claims);
    }

    private String getSecret(TokenTypeE tokenTypeE){
        if (tokenTypeE.REFRESH.equals(tokenTypeE)){
            return jwtRefreshSecret;
        }

        return jwtAccessSecret;
    }

    public String extractUsername(String token, TokenTypeE tokenTypeE){
        String secret = getSecret(tokenTypeE);
        return extractClaim(token,Claims::getSubject, secret);
    }
}
