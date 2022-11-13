package com.aninfo.model;

import javax.persistence.*;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long transactionCode;

    private Double amount;

    private String type;

    private Long accountCbu;

    public Transaction(){
    }

    public Transaction(Double amount, String type, Long accountCbu){
        this.amount = amount;
        this.type = type;
        this.accountCbu = accountCbu;
    }

    public Long getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(Long transactionCode) {
        this.transactionCode = transactionCode;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getAccountCbu() {
        return accountCbu;
    }

    public void setAccountCbu(Long accountCbu) {
        this.accountCbu = accountCbu;
    }
}
