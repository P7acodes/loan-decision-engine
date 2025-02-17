package org.piha.learning.loandecisionengine.controller;

import lombok.RequiredArgsConstructor;
import org.piha.learning.loandecisionengine.model.Decision;
import org.piha.learning.loandecisionengine.model.LoanApplication;
import org.piha.learning.loandecisionengine.repository.LoanApplicationRepository;
import org.piha.learning.loandecisionengine.service.LoanDecisionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanApplicationController {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanDecisionService loanDecisionService;

    // Loan application creation request
    @PostMapping
    public ResponseEntity<LoanApplication> createLoanApplication(@RequestBody LoanApplication loanApplication) {
        LoanApplication savedLoan = loanApplicationRepository.save(loanApplication);
        return ResponseEntity.ok(savedLoan);
    }
    // Loan evaluation request
    @PostMapping("/{id}/evaluate")
    public ResponseEntity<Decision> evaluateLoan(@PathVariable UUID id) {
        return loanApplicationRepository.findById(id)
                .map(loanApplication -> {
                    Decision decision = loanDecisionService.evaluateLoanApplication(loanApplication);
                    return ResponseEntity.ok(decision);
                }).orElse(ResponseEntity.notFound().build());
    }

    // Retrieve all existing loan applications in repository
    @GetMapping
    public ResponseEntity<List<LoanApplication>> getLoanApplications() {
        return ResponseEntity.ok(loanApplicationRepository.findAll());
    }
}
