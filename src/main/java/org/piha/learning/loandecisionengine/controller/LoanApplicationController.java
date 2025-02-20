package org.piha.learning.loandecisionengine.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.piha.learning.loandecisionengine.model.Customer;
import org.piha.learning.loandecisionengine.model.Decision;
import org.piha.learning.loandecisionengine.model.LoanApplication;
import org.piha.learning.loandecisionengine.repository.CustomerRespository;
import org.piha.learning.loandecisionengine.repository.LoanApplicationRepository;
import org.piha.learning.loandecisionengine.service.LoanDecisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/loans")
public class LoanApplicationController {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanDecisionService loanDecisionService;
    private final CustomerRespository customerRespository;

    public LoanApplicationController(LoanApplicationRepository loanApplicationRepository, LoanDecisionService loanDecisionService, CustomerRespository customerRespository) {
        this.loanApplicationRepository = loanApplicationRepository;
        this.loanDecisionService = loanDecisionService;
        this.customerRespository = customerRespository;
    }

    // Loan application creation request
    @PostMapping
    public ResponseEntity<?> createLoanApplication(@RequestBody LoanApplication loanApplication) {
        UUID customerId = loanApplication.getCustomer().getId();
        Optional<Customer> customer = customerRespository.findById(customerId);
        if (customer.isEmpty()) {
            return ResponseEntity.badRequest().body("Client introuvable avec l'ID: " + customerId);
        }

        loanApplication.setCustomer(customer.get());
        LoanApplication savedLoan = loanApplicationRepository.save(loanApplication);
        return ResponseEntity.ok(savedLoan);
    }
    // Loan evaluation request
    @PostMapping("/{id}/evaluate")
    public ResponseEntity<?> evaluateLoan(@PathVariable UUID id) {
        Optional<LoanApplication> loanApplication = loanApplicationRepository.findById(id);
        if (loanApplication.isEmpty()) {
            return ResponseEntity.badRequest().body("Votre demande avec l'ID " + id + " est introuvable ! ");
        }

        Decision decision = loanDecisionService.evaluateLoanApplication(loanApplication.get());
        return ResponseEntity.ok(decision);
    }

    // Retrieve all existing loan applications in repository
    @GetMapping
    public ResponseEntity<List<LoanApplication>> getLoanApplications()
    {   List<LoanApplication> loanApplications = loanApplicationRepository.findAll();
        return ResponseEntity.ok(loanApplications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLoanById(@PathVariable UUID id) {
        Optional<LoanApplication> loanApplication = loanApplicationRepository.findById(id);
        return loanApplication.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
