package com.bugtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bugtracker.entity.User;
import com.bugtracker.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BugTrackerApplication {
    public static void main(String[] args) {
        SpringApplication.run(BugTrackerApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UserRepository repo) {
        return args -> {
            if (repo.findByEmail("user@example.com").isEmpty()) {
                User u = new User();
                u.setName("Default User");
                u.setEmail("user@example.com");
                u.setPassword("password");
                u.setRole("USER");
                repo.save(u);
                // System.out.println("Default user created: user@example.com / password");
            }
        };
    }
}
