package ru.yarigo.cerberus.security;

import com.nimbusds.jose.JOSEException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.yarigo.cerberus.security.service.JwtService;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                if (jwtService.verifyToken(token)) {
                    String username = jwtService.extractUsername(token);
                    List<SimpleGrantedAuthority> authorities = jwtService.extractAuthorities(token).stream()
                                    .map(SimpleGrantedAuthority::new)
                                            .toList();

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            username, null, authorities
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (ParseException | JOSEException e) {
                throw new AuthenticationServiceException("Failed to parse token", e);
            }
        }

        filterChain.doFilter(request, response);
    }
}
