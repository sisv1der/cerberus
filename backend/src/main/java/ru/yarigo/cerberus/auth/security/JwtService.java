package ru.yarigo.cerberus.auth.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    private JWSSigner getSigner() throws KeyLengthException {
        return new MACSigner(secretKey);
    }

    public String generateToken(UserDetails userDetails) throws JOSEException {
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        long now = System.currentTimeMillis();
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(userDetails.getUsername())
                .jwtID(UUID.randomUUID().toString())
                .issuer("Cerberus")
                .issueTime(new Date(now))
                .expirationTime(new Date(now + 60 * 60 * 1000)) // 1 час жизни
                .claim("roles", roles)
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), jwtClaimsSet);

        signedJWT.sign(getSigner());

        return signedJWT.serialize();
    }

    public boolean verifyToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        boolean validSignature = signedJWT.verify(new MACVerifier(secretKey));

        Date expirationDate = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean isNotExpired = new Date().before(expirationDate);

        return validSignature && isNotExpired;
    }

    public String extractUsername(String token) throws ParseException {
        return SignedJWT.parse(token).getJWTClaimsSet().getSubject();
    }

    public List<String> extractAuthorities(String token) throws ParseException {
        return (List<String>) SignedJWT.parse(token).getJWTClaimsSet().getClaim("roles");
    }
}
