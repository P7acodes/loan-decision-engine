package org.piha.learning.loandecisionengine.repository;

import org.piha.learning.loandecisionengine.model.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, UUID> {
}
