
package com.bugtracker.controller;

import com.bugtracker.entity.Issue;
import com.bugtracker.service.IssueService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/issues")
@CrossOrigin
public class IssueController {
    private final IssueService service;
    private final com.bugtracker.repository.UserRepository userRepo;
    private final com.bugtracker.security.JwtUtil jwtUtil;

    public IssueController(IssueService service, com.bugtracker.repository.UserRepository userRepo,
            com.bugtracker.security.JwtUtil jwtUtil) {
        this.service = service;
        this.userRepo = userRepo;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public List<Issue> all() {
        return service.all();
    }

    @PostMapping
    public Issue create(@RequestHeader(value = "Authorization", required = false) String token, @RequestBody Issue i) {
        System.out.println("Processing create issue request. Token present: " + (token != null));
        if (token != null && token.startsWith("Bearer ")) {
            try {
                String fullToken = token.substring(7);
                // System.out.println("Token: " + fullToken); // Do not log full tokens in prod,
                // but for debug ok locally if needed.
                String email = jwtUtil.extractEmail(fullToken);
                System.out.println("Extracted email: " + email);
                if (email != null) {
                    java.util.Optional<com.bugtracker.entity.User> u = userRepo.findByEmail(email);
                    System.out.println("User found: " + u.isPresent());
                    u.ifPresent(user -> {
                        i.setCreator(user);
                        System.out.println("Creator set to: " + user.getName());
                    });
                }
            } catch (Exception e) {
                System.err.println("Error extracting user from token: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("No valid Bearer token found.");
        }
        return service.create(i);
    }

    @PutMapping("/{id}")
    public Issue update(@PathVariable Long id, @RequestBody Issue i) {
        return service.update(id, i);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
