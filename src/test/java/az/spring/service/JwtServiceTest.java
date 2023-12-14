package az.spring.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.MissingClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtServiceTest {

    @Value("${jwt.algorithm.key}")
    private String algorithmKey;

    @Autowired
    private JwtService jwtService;

    @Test
    public void testLoginJwtNotGeneratedByUs() {
        String token =
                JWT.create().withClaim("USERNAME", "UserA").sign(Algorithm.HMAC256("NotTheRealSecret"));
        assertThrows(SignatureVerificationException.class, () -> jwtService.getUsername(token));
    }

    @Test
    public void testLoginJwtCorrectlySignedNoIssuer() {
        String token = JWT.create().withClaim("USERNAME", "UserA").sign(Algorithm.HMAC256(algorithmKey));
        assertThrows(MissingClaimException.class, () -> jwtService.getUsername(token));
    }

}