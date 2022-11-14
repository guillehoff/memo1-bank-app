package com.aninfo.service;

import com.aninfo.exceptions.DepositNegativeSumException;
import com.aninfo.exceptions.InsufficientFundsException;
import com.aninfo.model.Account;
import com.aninfo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Service
public class AccountService {

    final Double MAX_PROMO = 5000.0;
    final Double MIN_PROMO = 2000.0;
    final Double MAX_AMOUNT_PROMO = 500.0;


    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Collection<Account> getAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> findById(Long cbu) {
        return accountRepository.findById(cbu);
    }

    public void save(Account account) {
        accountRepository.save(account);
    }

    public void deleteById(Long cbu) {
        accountRepository.deleteById(cbu);
    }

    @Transactional
    public Account withdraw(Long cbu, Double sum) {
        Account account = accountRepository.findAccountByCbu(cbu);

        if (account.getBalance() < sum) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        account.setBalance(account.getBalance() - sum);
        accountRepository.save(account);

        return account;
    }

    @Transactional
    public Account deposit(Long cbu, Double sum) {

        if (sum <= 0) {
            throw new DepositNegativeSumException("Cannot deposit negative sums");
        }

        Account account = accountRepository.findAccountByCbu(cbu);
        account.setBalance(account.getBalance() + (sum + amountPromo(sum)));
        accountRepository.save(account);

        return account;
    }

    public Double amountPromo(Double sum) {

        Double amountPromo = 0.0;

        if (sum >= MIN_PROMO && sum < MAX_PROMO) {
            amountPromo = (sum * 0.1);
        }

        if (sum >= MAX_PROMO) {
            amountPromo = MAX_AMOUNT_PROMO;
        }

        return amountPromo;
    }

}
