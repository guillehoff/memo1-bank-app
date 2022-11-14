package com.aninfo.service;

import com.aninfo.exceptions.DepositNegativeSumException;
import com.aninfo.exceptions.InsufficientFundsException;
import com.aninfo.model.Account;
import com.aninfo.model.Transaction;
import com.aninfo.repository.AccountRepository;
import com.aninfo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private AccountService accountService;  //service se puede comunicar con service?

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction createTransaction(Transaction transaction) {

        if (transaction.getAmount() <= 0) {
            throw new DepositNegativeSumException("Cannot transaction negative or null sums");
        }

        if (transaction.getType().equals("deposit")) {
            accountService.deposit(transaction.getAccountCbu(), transaction.getAmount());
        }

        if (transaction.getType().equals("withdraw")) {
            accountService.withdraw(transaction.getAccountCbu(), transaction.getAmount());
        }

        return transactionRepository.save(transaction);
    }

    public Collection<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> findById(Long transactionCode) {
        return transactionRepository.findById(transactionCode);
    }

    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public void deleteById(Long transactionCode) {
        transactionRepository.deleteById(transactionCode);
    }

    @Transactional
    public Transaction withdraw(Long cbu, Double sum) {
        Account account = accountRepository.findAccountByCbu(cbu);

        if (account.getBalance() < sum) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        account.setBalance(account.getBalance() - sum);
        accountRepository.save(account);

        return createTransaction(new Transaction(sum, "withdrawal", cbu) );
    }

    @Transactional
    public Transaction deposit(Long cbu, Double sum) {

        if (sum <= 0) {
            throw new DepositNegativeSumException("Cannot deposit negative sums");
        }

        Account account = accountRepository.findAccountByCbu(cbu);
        account.setBalance(account.getBalance() + sum);
        accountRepository.save(account);

        return createTransaction(new Transaction(sum, "deposit", cbu) );
    }

}