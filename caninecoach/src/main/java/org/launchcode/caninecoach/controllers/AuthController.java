package org.launchcode.caninecoach.controllers;

import org.launchcode.caninecoach.entities.User;
import org.launchcode.caninecoach.entities.VerificationToken;
import org.launchcode.caninecoach.security.jwt.JwtUtils;
import org.launchcode.caninecoach.services.EmailService;
import org.launchcode.caninecoach.services.UserService;
import org.launchcode.caninecoach.services.VerificationTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;

    @Value("${app.baseUrl}")
    private String baseUrl;

    @Autowired
    public AuthController(UserService userService, PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager, JwtUtils jwtUtils,
                          VerificationTokenService verificationTokenService, EmailService emailService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.verificationTokenService = verificationTokenService;
        this.emailService = emailService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // Create a JSON response object
        Map<String, Object> response = new HashMap<>();
        response.put("token", jwt);
        response.put("email", email);
        response.put("roles", roles);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestParam String email, @RequestParam String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // Returned to the response entity
        return ResponseEntity.ok(String.format("JWT Token: %s, Email: %s, Roles: %s", jwt, email, String.join(", ", roles)));
    }

    @GetMapping("/verify-account")
    public ResponseEntity<String> verifyAccount(@RequestParam("token") String token) {
        Optional<VerificationToken> verificationTokenOpt = verificationTokenService.validateVerificationToken(token);
        if (verificationTokenOpt.isPresent()) {
            VerificationToken verificationToken = verificationTokenOpt.get();
            User user = verificationToken.getUser();
            userService.setVerifiedTrue(user);
            return ResponseEntity.ok("Account verified successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired verification token.");
        }
    }
}
