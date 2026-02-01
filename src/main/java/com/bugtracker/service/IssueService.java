
package com.bugtracker.service;

import com.bugtracker.entity.Issue;
import com.bugtracker.repository.IssueRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class IssueService {
    private final IssueRepository repo;

    public IssueService(IssueRepository repo) {
        this.repo = repo;
    }

    public Issue create(Issue i) {
        if (i.getStatus() == null || i.getStatus().isEmpty()) {
            i.setStatus("Open");
        }
        return repo.save(i);
    }

    public void delete(long id) {
        repo.deleteById(id);
    }

    public List<Issue> all() {
        return repo.findAll();
    }

    public Issue update(long id, Issue issue) {
        return repo.findById(id)
                .map(existingIssue -> {
                    existingIssue.setTitle(issue.getTitle());
                    existingIssue.setDescription(issue.getDescription());
                    existingIssue.setStatus(issue.getStatus());
                    existingIssue.setPriority(issue.getPriority());
                    return repo.save(existingIssue);
                })
                .orElseThrow(() -> new RuntimeException("Issue not found with id " + id));
    }
}
