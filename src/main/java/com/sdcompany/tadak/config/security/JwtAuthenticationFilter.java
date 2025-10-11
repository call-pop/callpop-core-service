package com.sdcompany.tadak.config.security;

import com.sdcompany.tadak.login.dto.TadakUser;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {
        String token = tokenProvider.parseBearerToken(request);

        if (token != null && !"null".equalsIgnoreCase(token)) {
            try {
                Claims claims = tokenProvider.parseClaims(token);

                String sub = claims.getSubject();
                Long userId = (sub != null && !sub.isBlank()) ? Long.parseLong(sub) : null;

                String userIdentifier = claims.get("userIdentifier", String.class);

                // roles는 문자열 리스트로 받음
                List<String> roles = extractRoles(claims);
                Collection<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                TadakUser principal = new TadakUser(userId, userIdentifier, authorities);

                AbstractAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
                auth.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                SecurityContextHolder.clearContext();
                throw new ServletException("Invalid token", e);
            }
        }

        chain.doFilter(request, response);
    }

    @SuppressWarnings("unchecked")
    private List<String> extractRoles(Claims claims) {
        Object raw = claims.get("roles");
        if (raw instanceof List<?>) {
            return ((List<?>) raw).stream().map(String::valueOf).toList();
        }
        // 없으면 기본
        return List.of("ROLE_USER");
    }
}
