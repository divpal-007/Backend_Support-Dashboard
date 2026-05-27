package com.support_dashboard.TellMe.config;

import com.support_dashboard.TellMe.repository.UserRepository;
import com.support_dashboard.TellMe.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public JwtFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api/auth/") || path.equals("/error");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Spring's filter chain can call filters multiple times per request in some scenarios. OncePerRequestFilter guarantees doFilterInternal runs exactly once per request — no duplicate processing.

        //Step 1 - Extract token from cookie
//        Manually reads cookies from the raw HttpServletRequest. The filter runs before Spring's parameter injection (@CookieValue) — so you extract cookies manually here:
        String token = "";
        Cookie [] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("operix_token")){
                    token = cookie.getValue();
                }
            }
        }
        //Step 2- validate and set authentication
//        Validates signature and expiration. If invalid — skip authentication, continue filter chain. The authorization filter later will block the request if the endpoint requires authentication.
        if(!token.isEmpty() && jwtUtil.validateToken(token)){
           String email = jwtUtil.extractEmailFromToken(token);

           userRepository.findByEmail(email).ifPresent(user -> {
               //Step 3 - build authentication object
//               Spring Security's way of representing an authenticated user
               UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user.getEmail(),null, List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole())));
//             SimpleGrantedAuthority("ROLE_SRE") — Spring Security uses roles prefixed with ROLE_. This enables role-based access control later

               //Step 4 - set in security context
//               The security context is a thread-local storage — it holds the current user's authentication for the duration of the request.
//               Once set — Spring Security considers the user authenticated. The authorization filter sees a valid authentication object and allows access to protected endpoints.
//After the request completes — Spring Security clears the context automatically. Next request starts fresh.
               SecurityContextHolder.getContext().setAuthentication(auth);
           });
        }
        //Step 5 - continue the filter chain
//        Critical line — must always be called. Passes the request to the next filter in the chain. If you forget this — the request stops here and your controller never runs.
        filterChain.doFilter(request, response);
    }
}
