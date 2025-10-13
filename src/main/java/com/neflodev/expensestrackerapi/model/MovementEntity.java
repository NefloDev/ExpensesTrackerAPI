package com.neflodev.expensestrackerapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "TBL_MOVEMENTS")
public class MovementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "ID", nullable = false)
    private AccountEntity account;

    @Column(name = "AMOUNT", nullable = false)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "ID", nullable = false)
    private CategoryEntity category;

    @Column(name = "COMMENT")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "DESTINATION_ACCOUNT_ID", referencedColumnName = "ID")
    private AccountEntity destination;

    @CreationTimestamp
    @Column(name = "EFFECTIVE_DATE")
    private LocalDate effectiveDate;

}
