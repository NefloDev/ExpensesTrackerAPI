package com.neflodev.expensestrackerapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Currency;

@Getter
@Setter
@Entity
@Table(name = "TBL_ACCOUNT")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "id")
    private User user;

    @Column(name = "ACCOUNT_NAME")
    private String accountName;

    @Column(name = "CURRENCY")
    private String currency;

    public String getCurrencySymbol(){
        return Currency.getInstance(currency).getSymbol();
    }

}
