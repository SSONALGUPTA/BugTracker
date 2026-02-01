
package com.bugtracker.service;

import com.bugtracker.entity.User;
import com.bugtracker.repository.UserRepository;
import com.bugtracker.security.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final UserRepository repo;
    private final JwtUtil jwt;

    public UserService(UserRepository repo, JwtUtil jwt) {
        this.repo = repo;
        this.jwt = jwt;
    }

    public String register(User u) {
        if (repo.findByEmail(u.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        repo.save(u);
        return jwt.generateToken(u.getEmail());
    }

    public String login(String email, String password) {
        Optional<User> u = repo.findByEmail(email);
        if (u.isPresent() && u.get().getPassword().equals(password)) {
            return jwt.generateToken(email);
        }
        return null;
    }
}
