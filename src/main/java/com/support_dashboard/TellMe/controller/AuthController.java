package com.support_dashboard.TellMe.controller;

import com.support_dashboard.TellMe.dto.LoginRequest;
import com.support_dashboard.TellMe.dto.RegisterRequest;
import com.support_dashboard.TellMe.dto.UserResponse;
import com.support_dashboard.TellMe.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//The controller is the entry point for every HTTP request. Its only job is:
//Receive the HTTP request
//Extract data from it
//Pass to the service
//Return the response

//@Controller — marks this as a Spring MVC controller, Spring registers it and routes HTTP requests to it.
//@ResponseBody — tells Spring to convert whatever you return from each method directly to JSON and write it to the HTTP response body. Without this, Spring would try to find an HTML template to render.
@CrossOrigin(origins = "*")
@RestController //(@Controller + @ResponseBody)
@RequestMapping("/api/auth")
public class AuthController {
//    Same pattern as AuthService — constructor injection, final field. Spring provides the AuthService bean automatically.
//Controller knows nothing about UserRepository, JwtUtil, or PasswordEncoder — it only knows about AuthService. Clean layering.

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login") //Maps POST /api/auth/login to this method. POST because you're sending sensitive data — email and password go in the request body, not the URL.
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
//       @RequestBody — tells Spring to read the HTTP request body and deserialize the JSON into a LoginRequest object:
//        @Valid
//Triggers Bean Validation on LoginRequest before the method runs. If email is blank or invalid — Spring returns 400 Bad Request immediately, GlobalExceptionHandler formats the error. authService.login() never executes.
        return ResponseEntity.ok(authService.login(loginRequest,response));

//        You pass the raw HTTP response object to authService.login() so it can attach the cookie. Spring injects this automatically — you just declare it as a parameter.
    }
//    So when you return UserResponse — Spring uses Jackson to convert it to JSON automatically:

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest registerRequest, HttpServletResponse response) {
        System.out.println("inside register controller");
        return ResponseEntity.ok(authService.register(registerRequest,response));
//        Identical structure to Log in — just different DTO and service method. Clean and consistent.
    }

    @PostMapping("/logout")
    public ResponseEntity<UserResponse> logout(HttpServletResponse response) {
        authService.logout(response);
        return ResponseEntity.ok().build(); //When there's no body — use .build() instead of .ok(body): //200 OK, no body
//        return ResponseEntity.ok(userResponse);  200 OK, with body
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@CookieValue("operix_token") String token) {

//        // without @CookieValue — manual extraction
//public ResponseEntity<UserResponse> me(HttpServletRequest request) {
//    Cookie[] cookies = request.getCookies();
//    String token = null;
//
//    if (cookies != null) {
//        for (Cookie cookie : cookies) {
//            if (cookie.getName().equals("operix_token")) {
//                token = cookie.getValue();
//                break;
//            }
//        }
//    }
//
//    if (token == null) {
//        return ResponseEntity.status(401).build();
//    }
//}
        return ResponseEntity.ok(authService.getMe(token));
//        GET because we're fetching data — no body being sent, just the cookie the browser attaches automatically.
//        @CookieValue("operix_token")
//        Extracts the operix_token cookie from the incoming request and injects it as the token parameter. Spring handles cookie parsing automatically.
    }
}
