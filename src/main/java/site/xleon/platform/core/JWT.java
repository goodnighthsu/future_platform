package site.xleon.platform.core;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import site.xleon.platform.config.app.AppConfig;
import site.xleon.platform.models.SysUserEntity;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
@AllArgsConstructor
public class JWT {

    private final AppConfig appConfig;

    private Jws<Claims> getClaims(String token) throws MyException {
        if (token == null || token.isEmpty()) {
            throw new MyException("token can not be null");
        }

        try {

            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(appConfig.getJwt().getSecret()));
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            String id = claims.getBody().getId();
            if (id == null) {
                throw new MyException("invalid token");
            }

            return claims;
        } catch (ExpiredJwtException e) {
            throw new MyException("token expiry", ResultCodeEnum.TOKEN_EXPIRY);
        } catch (JwtException e) {
            throw new MyException("invalid token");
        }
    }

    public Integer getUserId(HttpServletRequest request) throws MyException {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token == null || token.isEmpty()) {
            throw new MyException("token can not be null");
        }

        // remove Brear from authorization
        token = token.substring(7);

        Jws<Claims> claims = getClaims(token);
        String id = claims.getBody().getId();
        return Integer.parseInt(id);
    }

    public String createByUser(SysUserEntity user, boolean isExpiry) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(appConfig.getJwt().getSecret()));

        JwtBuilder builder = Jwts.builder()
                .setIssuer("xleon.site")
                .setId(user.getId().toString())
                .setAudience("token")
                .setIssuedAt(new Date())
                .signWith(key);

        if (isExpiry) {
            long expiration = System.currentTimeMillis() + appConfig.getJwt().getExpiry() * 60 * 1000;
            builder.setExpiration(new Date(expiration));
        }

        return builder.compact();
    }
}
