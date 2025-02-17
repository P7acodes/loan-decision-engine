package org.piha.learning.loandecisionengine.repository;

import org.piha.learning.loandecisionengine.model.Decision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DecisionRepository extends JpaRepository<Decision, UUID> {
}
