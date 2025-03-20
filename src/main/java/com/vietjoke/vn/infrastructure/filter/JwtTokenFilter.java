package com.vietjoke.vn.infrastructure.filter;

import com.vietjoke.vn.domain.user.entity.UserEntity;
import com.vietjoke.vn.infrastructure.util.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    private static final List<EndpointMethod> BYPASS_ENDPOINTS = List.of(
            new EndpointMethod("/login", "GET"),
            new EndpointMethod("/register", "GET"),
            new EndpointMethod("/v1/api/login", "POST"),
            new EndpointMethod("/v1/api/register", "POST"),
            new EndpointMethod("/v1/api/admin/login", "POST")
    );

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if (isBypassEndpoint(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = extractTokenFromHeader(request);
            if (token == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No token provided");
                return;
            }

            authenticateToken(request, token);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
        }
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private void authenticateToken(HttpServletRequest request, String token) {
        String phoneNumber = jwtTokenUtil.extractUsername(token);
        if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserEntity userDetails = (UserEntity) userDetailsService.loadUserByUsername(phoneNumber);
            if (jwtTokenUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
    }

    private boolean isBypassEndpoint(HttpServletRequest request) {
        return BYPASS_ENDPOINTS.stream().anyMatch(endpoint ->
                request.getServletPath().contains(endpoint.path) &&
                        request.getMethod().equals(endpoint.method)
        );
    }

    private record EndpointMethod(String path, String method) {}
}