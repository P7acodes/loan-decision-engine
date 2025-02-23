package org.piha.learning.loandecisionengine.service;

import lombok.RequiredArgsConstructor;
import org.piha.learning.loandecisionengine.enums.Role; // ✅ Ensure this import is correct
import org.piha.learning.loandecisionengine.model.User;
import org.piha.learning.loandecisionengine.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        System.out.println("🔍 DEBUG: Roles found for user " + username + ": " + user.getRoles());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())) // ✅ Ensure ROLE_ prefix
                        .collect(Collectors.toList()))
                .build();
    }
}
