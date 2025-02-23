package org.piha.learning.loandecisionengine.controller;

import lombok.RequiredArgsConstructor;
import org.piha.learning.loandecisionengine.model.User;
import org.piha.learning.loandecisionengine.repository.UserRepository;
import org.piha.learning.loandecisionengine.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // ✅ Hash password
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        System.out.println("🔍 DEBUG: Attempting authentication for user: " + user.getUsername());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
            System.out.println("✅ DEBUG: Authentication successful!");

        } catch (Exception e) {
            System.out.println("❌ DEBUG: Authentication failed! Reason: " + e.getMessage());
            return ResponseEntity.status(401).body("Authentication failed: " + e.getMessage());
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtUtil.generateToken(userDetails);

        System.out.println("🔍 DEBUG: Generated Token: " + token);

        return ResponseEntity.ok("{\"token\": \"" + token + "\"}");
    }

}
