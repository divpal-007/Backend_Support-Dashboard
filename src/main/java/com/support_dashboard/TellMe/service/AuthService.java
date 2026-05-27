package com.support_dashboard.TellMe.service;

import com.support_dashboard.TellMe.dto.LoginRequest;
import com.support_dashboard.TellMe.dto.RegisterRequest;
import com.support_dashboard.TellMe.dto.UserResponse;
import com.support_dashboard.TellMe.model.User;
import com.support_dashboard.TellMe.repository.UserRepository;
import com.support_dashboard.TellMe.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service //@Service — tells Spring this is a service bean. Spring creates one instance and injects it wherever needed.
public class AuthService {
//    This is dependency injection — one of Spring's core concepts. You declare what you need, Spring provides it
//    Why a Service Layer we could put all the logic directly in the controller
//    It would work. But:
//Controllers become massive and hard to read
//You can't reuse logic across multiple controllers
//Testing becomes painful — you'd need to spin up HTTP to test business logic
//Violates single responsibility — controller should only handle HTTP, not business decisions
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final JwtUtil jwtUtil;

//Field injection     → dependencies can be null, harder to test

//Constructor injection → dependencies guaranteed at creation, easy to test
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

//login api
    public UserResponse login(LoginRequest req, HttpServletResponse res) {

//        Step 1 - Find the User
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email"));

//        Step 2 - verify Password
//                            matches() hashes the incoming password with the same salt and compares the results.
        if(!passwordEncoder.matches(req.getPassword(), user.getPassword())){
//                                    ↑ plain text from React    ↑ BCrypt hash from DB(BCrypt works in one direction — you can never decode the hash back to plain text.)
            throw new RuntimeException("Invalid password");
        }

//        Step 3 - generate JWT and set Cookie

        String token = jwtUtil.generateToken(user);
        attachCookie(token,res);
//        Generates the JWT and attaches it to the HTTP response as an HttpOnly cookie. The browser stores it automatically.

        return toResponse(user);
    }

//    Register Api
    public UserResponse register(RegisterRequest req, HttpServletResponse res) {

//        Step 1 - Check Email not already taken
       if(userRepository.existsByEmail(req.getEmail())){
           throw new RuntimeException("Email already exists");
       }

//       Step 2 - build the user entity
        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
//        Never store plain text passwords. encode() runs BCrypt on the password before saving. The database never sees "password123" — only the hash.
        user.setWorkSpace(req.getWorkspace());

//        Step 3 - save to database
        userRepository.save(user);

//        Step 4 - generate JWT and set cookie
        String token = jwtUtil.generateToken(user);
        attachCookie(token,res);

        return toResponse(user);
    }

//    logout
    public void logout(HttpServletResponse res) {
        Cookie cookie = new Cookie("operix_token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        res.addCookie(cookie);
//        You cannot delete a cookie from the server side directly. Instead you overwrite it with:
//        null value — empty
//        MaxAge(0) — expires immediately
//        The browser receives this, sees the cookie is expired, removes it. Next request — no cookie — user is logged out.
    }

//    revalidate the session
    public UserResponse getMe(String token){
        String email = jwtUtil.extractEmailFromToken(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return toResponse(user);

//      When React loads and finds an existing cookie — it calls GET /api/auth/me to restore the session. This method extracts the email from the token, looks up the fresh user data from DB, and returns it.
    }

    private void attachCookie(String token,HttpServletResponse res) {
        Cookie cookie = new Cookie("operix_token", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setMaxAge(86400);
        res.addCookie(cookie);

//        setHttpOnly(true) — JavaScript cannot read this cookie. document.cookie won't show it. Only the browser knows about it and sends it automatically with every request. XSS attacks cannot steal it.
//        setSecure(false) — in development your app runs on http:// not https://. Secure cookies only travel over HTTPS. Set false for local dev, true in production.
//        setPath("/") — cookie is sent with requests to all paths on your domain. Without this it only goes to /api/auth — other endpoints never receive it.
//        setMaxAge(86400) — 24 hours in seconds. After 24 hours the browser discards the cookie automatically. User must log in again
    }
    // extracted — was duplicated in login and register
    private UserResponse toResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setEmail(user.getEmail());
        userResponse.setRole(user.getRole());
        userResponse.setWorkspace(user.getWorkSpace());
        userResponse.setInitials(
                user.getName().length() >=2 ? user.getName().substring(0, 2).toUpperCase()
                        : user.getName().toUpperCase());

        return userResponse;
    }

//       Session              JWT Cookie
//────────────────────────────────────────────────────────────
//Stored where?       Server memory        Browser cookie
//Server remembers?   Yes                  No
//DB lookup every     Yes                  No
//request?
//
//Multiple servers?   Problem              No problem
//Server restart?     Logged out           Still logged in
//Scalability?        Poor                 Excellent
//Token stolen?       Invalidate session   Can't invalidate
//                    immediately          until expiry
//Memory usage?       Grows with users     Zero
}
