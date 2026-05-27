package com.support_dashboard.TellMe.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration //this class contains bean definitions. Spring reads it at startup.
@EnableWebSecurity //activates Spring Security's web security support and tells Spring to use your SecurityFilterChain bean instead of the default one. Without this, our config is ignored.
public class WebConfig {

    @Value("${app.cors.allowed-origin}")
    private String allowedOrigin;

    private final JwtFilter jwtFilter;

    public WebConfig(JwtFilter jwtFilter){
        this.jwtFilter = jwtFilter;
    }
    private final List<String> allowedHeaders = List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS");

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configure(http))
                .sessionManagement(s->
                        s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated()) //Every other endpoint requires authentication
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        //Inserts your JwtFilter into the filter chain — specifically before Spring's default username/password filter.
//        This is where the JWT validation actually happens on every request. The filter:
//
//        Reads the cookie
//        Validates the JWT
//        Loads the user
//        Sets their identity in Spring Security context
//
//        Without this — Spring Security doesn't know how to validate JWTs. It just blocks everything.

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() { //CORS (Cross Origin Resource Sharing) is how your Spring Boot server tells browsers — requests from these specific origins are allowed.
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true); // set true only if using cookies/auth
        config.setAllowedOrigins(List.of(allowedOrigin));
        // Must explicitly allow the ngrok header
        config.addAllowedHeader("*");
        config.setAllowedMethods(allowedHeaders); // GET, POST, PUT, DELETE, OPTIONS

        // Important: expose headers if your frontend reads them
        config.addExposedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);

        return new CorsFilter(source);
    }
}
//Notes:
// 1) CSRF — Cross Site Request Forgery.
//An attack where a malicious website tricks a logged-in user's browser into making requests to your API using their session.
//Example without protection:
//User logged into operix.com
//User visits evil.com
//evil.com has hidden form that POSTs to operix.com/api/escalations/delete
//Browser sends operix session cookie automatically
//Your API thinks it's a legitimate request — deletes data
//Spring Security's CSRF protection works by requiring a special token in every state-changing request (POST, PUT, DELETE). The token is embedded in HTML forms — malicious sites can't access it.

//Why disable it for REST APIs?
//Because you're using HttpOnly cookies + CORS, not HTML forms. Your CORS config already only allows requests from localhost:3000. An attacker on evil.com is blocked by CORS before reaching your API.

//Also React sends requests programmatically — managing CSRF tokens in every Axios call adds complexity with no benefit when CORS is properly configured.

// 2) Enables CORS and points to your corsConfig() bean. Without this line — even with corsConfig() defined — Spring Security blocks cross-origin requests before they reach your CORS config.

// 3) Tells Spring Security — do not create HTTP sessions. Every request must carry its own authentication (the JWT cookie). The server remembers nothing between requests.
//Without this — Spring Security creates a session for every request even though you're using JWT. Wasted memory, misleading behavior.
//STATELESS enforces the JWT architecture properly.