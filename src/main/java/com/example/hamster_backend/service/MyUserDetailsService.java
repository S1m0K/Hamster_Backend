package com.example.hamster_backend.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface MyUserDetailsService {
    UserDetails loadUserByUsername(String username);
}
