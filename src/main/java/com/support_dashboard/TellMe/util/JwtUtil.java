package com.support_dashboard.TellMe.util;


import com.support_dashboard.TellMe.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component //@Component — tells Spring to manage this class as a bean It gets created once and injected wherever needed — same as @Service and @Repository but for utility classes.
public class JwtUtil {
//   The problem JWT solves
//  HTTP is stateless — every request is independent. The server remembers nothing between requests.
//  User logs in          → server creates a signed token → sends to browser
//  User makes a request  → sends token with it
//  Server validates signature → trusts the token → no memory needed
//  Any server can validate it → scales perfectly

//  The token carries the proof of identity itself.

    @Value("${app.jwt.secret}") //@Value("${app.jwt.secret}") — reads from application.properties.
    private String secret;

    @Value("${app.jwt.expiration}")  //The ${} syntax tells Spring — look up this property and inject its value here. So secret gets the value operix-super-secret-key-minimum-32-characters-long automatically at startup.
    private long expiration;

    // extracted — used in both generateToken and getClaims
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
//        StandardCharsets.UTF_8 — ensures consistent byte encoding regardless of the platform the app runs on. Without specifying charset, different OS's might encode the string differently — your key would be inconsistent.
//        Keys.hmacShaKeyFor() — takes the bytes and creates an HMAC-SHA key. HMAC stands for Hash-based Message Authentication Code — it's the algorithm that signs the JWT.
    }

    public String generateToken(User user) {
        return Jwts.builder() //this is builder patter of Design Patterns
                .setSubject(user.getEmail())
                .claim("name",user.getName())
                .claim("role",user.getRole())
                .claim("workspace",user.getWorkSpace())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();

//     Building the JWT piece by piece:
//      .setSubject(user.getEmail()) — the sub claim. Standard JWT field for identifying who the token belongs to. Using email as the identifier — unique per user.
//      .claim("name", user.getName()) — custom claims. You add any data you want the frontend to read without making another API call. React can decode the token and know the user's name and role immediately.
//      .setIssuedAt(new Date()) — the iat claim. Records when the token was created. Useful for auditing.
//      .setExpiration(...) — the exp claim. System.currentTimeMillis() is current time in milliseconds. Adding expiration (86400000ms = 24 hours) sets when the token dies. After this time the library automatically rejects it.
//      .signWith(getSigningKey()) — signs the header + payload with your secret key. This produces the third part of the JWT.
//      .compact() — assembles everything into the final header.payload.signature string.
        }

    public String extractEmailFromToken(String token) {
        return getClaims(token).getSubject(); //getting the email
//        Reads the sub claim from the token — which is the user's email. Used when a protected request comes in — you extract the email, look up the user in the database, confirm they exist.
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        }catch (JwtException | IllegalArgumentException e) {
            return false;
        }

//       Validates the token in one method. getClaims() does all the heavy lifting:
//      Checks the signature matches — token wasn't tampered with
//      Checks expiration — token isn't expired
//      Checks structure — token is properly formed
//      If any check fails — the library throws a JwtException. You catch it and return false. Clean — no if/else chains, no manual checks.
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // uses the same secret key that signed the token. If the token was signed with a different key — validation fails.
                .build()
                .parseClaimsJws(token)
                .getBody();

////        WRONG — for unsigned tokens
//              .parseClaimsJwt(token)
//
////         CORRECT — for signed tokens (JWS = JSON Web Signature)
//                .parseClaimsJws(token)
//        .parseClaimsJwt(token) ---> parses the token string. Does three things simultaneously:
//        Decodes the Base64 header and payload
//        Recomputes the signature and compares
//        Checks the expiration
    }
}
