package com.ecommerce.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {



    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("==== JwtAuthenticationFilter triggered ====");
        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization header: " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println("Token extracted: " + token);

            try {
                if (jwtUtil.validateToken(token)) {
                    Claims claims = jwtUtil.extractAllClaims(token);
                    String username = claims.getSubject();
                    String role = claims.get("role", String.class);
                    System.out.println("Claims extracted - username: " + username + ", role: " + role);

                    List<SimpleGrantedAuthority> authorities =
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
                    System.out.println("Authorities: " + authorities);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    new User(username, "", authorities),
                                    null,
                                    authorities
                            );

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    System.out.println("Authentication set in context: " + SecurityContextHolder.getContext().getAuthentication());
                } else {
                    System.out.println("Token validation failed!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired JWT token");
                return;
            }
        } else {
            System.out.println("No Authorization header or wrong format");
        }

        filterChain.doFilter(request, response);
    }

}
