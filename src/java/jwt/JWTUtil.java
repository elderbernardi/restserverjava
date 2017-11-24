/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jwt;
/*
Código baseado em :http://www.totalcross.com/blog/seguranca-com-jwt-e-java/
Acessado em: nov/2017

*/
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

/**
 *
 * @author elder
 */
public class JWTUtil {

    private static final String key = "SECRET_TOKEN";

    public static final String TOKEN_HEADER = "Authorization";

    public static String create(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .signWith(SignatureAlgorithm.HS512, key)
                .setExpiration(new Date( new Date().getTime() + 600000 ) ) //tempo de 10 minutos de validade
                .setIssuer("AUTORIZADOR")
                .compact();
    }

    public static Jws<Claims> decode(String token){
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token);
    }
}
