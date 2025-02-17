package org.piha.learning.loandecisionengine.model;

import jakarta.persistence.*;
import lombok.*;
import org.piha.learning.loandecisionengine.enums.LoanStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    private BigDecimal amount;
    private Integer term;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "decision_id")
    private Decision decision;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.status = LoanStatus.PENDING;
    }
}
