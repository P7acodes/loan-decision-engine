package org.piha.learning.loandecisionengine.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.piha.learning.loandecisionengine.enums.LoanStatus;
import org.piha.learning.loandecisionengine.model.Decision;
import org.piha.learning.loandecisionengine.model.LoanApplication;
import org.piha.learning.loandecisionengine.repository.DecisionRepository;
import org.piha.learning.loandecisionengine.repository.LoanApplicationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoanDecisionService {

    private final DecisionRepository decisionRepository;
    private final LoanApplicationRepository loanApplicationRepository;

    @Transactional
    public Decision evaluateLoanApplication(LoanApplication loanApplication) {
        boolean approved = true;
        String reason = "Approuvé";

        // loan score verification
        if (loanApplication.getCustomer().getCreditScore() < 600) {
            approved = false;
            reason = "Score de crédit trop bas";
        } else if (loanApplication.getCustomer().getIncome().doubleValue() < 2000) {
            approved = false;
            reason = "Revenu insuffisant";
        } else if (loanApplication.getAmount().doubleValue() / loanApplication.getCustomer().getIncome().doubleValue() > 0.4) {
            approved = false;
            reason = "Ratio prêt/revenu trop élevé";
        }


        Decision decision = Decision.builder()
                .loanApplication(loanApplication)
                .approved(approved)
                .reason(reason)
                .reviewer("Système Automatisé")
                .createdAt(LocalDateTime.now())
                .build();

        decisionRepository.save(decision);

        loanApplication.setDecision(decision);
        loanApplication.setStatus(approved ? LoanStatus.APPROVED : LoanStatus.REJECTED);
        loanApplicationRepository.save(loanApplication);

        return decision;
    }

}
