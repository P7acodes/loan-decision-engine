package org.piha.learning.loandecisionengine.repository;

import org.piha.learning.loandecisionengine.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRespository extends JpaRepository<Customer, UUID> {
}
