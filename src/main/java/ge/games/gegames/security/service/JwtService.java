package ge.games.gegames.security.service;

import ge.games.gegames.security.TokenTypeE;
import ge.games.gegames.security.exception.RestApiException;
import io.jsonwebtoken.*;
import ge.games.gegames.security.details.AuthenticatedUsersService;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

import static ge.games.gegames.security.TokenTypeE.*;

@Service
@RequiredArgsConstructor
public class JwtService {

    public static final String REFRESH_TOKEN_NAME = "GEgamesRefreshToken";
    public static final String ACCESS_TOKEN_NAME = "GEgamesAccessToken";

    @Value("${jwt.at.secret}")
    private String jwtAccessSecret;

    @Value("${jwt.rt.secret}")
    private String jwtRefreshSecret;

    @Value("${jwt.at.lifetime}")
    @Getter
    private int accessTokenLifetime;

    @Value("${jwt.rt.lifetime}")
    @Getter
    private int refreshTokenLifetime;

    private final AuthenticatedUsersService authenticatedUsersService;
    private final CookieService cookieService;



    public void processToken(HttpServletRequest request, HttpServletResponse response){
        Optional<String> refreshToken = cookieService.getCookieValueByName(request, REFRESH_TOKEN_NAME);
        if (refreshToken.isEmpty() || !isTokenValid(refreshToken.get(), REFRESH)){
            throw new RestApiException(HttpStatus.FORBIDDEN, "Token doesn't exist");
        }

        final String mail = extractUsername(refreshToken.get(), REFRESH);
        UserDetails userDetails = authenticatedUsersService.loadUserByMail(mail);
        String accessToken = generateAccessToken(userDetails.getUsername());
        cookieService.createCookie(response, ACCESS_TOKEN_NAME, accessToken, "/", accessTokenLifetime);

        response.setHeader("X-XSS-Protection", "1; mode=block");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private Date extractExpiration(String token, String secret){
        return extractClaim(token, Claims::getExpiration, secret);
    }

    public boolean isTokenExpired(String token, TokenTypeE tokenTypeE){
        String secret = getSecret(tokenTypeE);
        return extractExpiration(token, secret).before(new Date());
    }

    public boolean isTokenValid(String token, TokenTypeE tokenTypeE){
        final String mail = extractUsername(token, tokenTypeE);
        UserDetails userDetails = authenticatedUsersService.loadUserByMail(mail);
        return mail.equals(userDetails.getUsername()) && !isTokenExpired(token, tokenTypeE);
    }

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

    public String generateAccessToken(String username){
        return createToken(username,jwtAccessSecret, accessTokenLifetime);
    }

    public String generateRefreshToken(String username){
        return createToken(username, jwtRefreshSecret, refreshTokenLifetime);
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
