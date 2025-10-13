package com.neflodev.expensestrackerapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Currency;

@Getter
@Setter
@Entity
@Table(name = "TBL_ACCOUNTS")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    @Column(name = "ACCOUNT_NAME", unique = true, nullable = false)
    private String accountName;

    @Column(name = "BALANCE")
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "CURRENCY", nullable = false)
    private String currency;

    public String getCurrencySymbol(){
        return Currency.getInstance(currency).getSymbol();
    }

}
