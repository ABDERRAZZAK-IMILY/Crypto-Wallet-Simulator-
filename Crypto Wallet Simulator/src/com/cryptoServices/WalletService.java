package com.cryptoServices;

import java.sql.Connection;
import java.util.List;
import java.util.logging.Logger;

import com.cryptoRepository.WalletRepository;
import com.model.Wallet;
import com.util.AppLogger;

public class WalletService {

    private final WalletRepository walletRepository;
    private static final Logger logger = AppLogger.getLogger(WalletService.class.getName());

    public WalletService(Connection connection) {
        this.walletRepository = new WalletRepository(connection);
    }

    public void createWallet(Wallet wallet) {
        if (wallet == null) {
            logger.warning("Cannot create null wallet");
            return;
        }
        walletRepository.save(wallet);
        logger.info("Wallet created: " + wallet.getId());
    }

    public Wallet getWalletById(String id) {
        return walletRepository.findById(id);
    }

    public List<Wallet> getAllWallets() {
        return walletRepository.findAll();
    }

    public void updateWallet(Wallet wallet) {
        walletRepository.update(wallet);
    }

    public boolean transferFunds(String sourceId, String destId, double amount, double fees) {
        Wallet source = getWalletById(sourceId);
        Wallet dest = getWalletById(destId);
        if (source == null || dest == null || source.getBalance() < amount + fees || amount <= 0) {
            logger.warning("Transfer failed: invalid wallets or insufficient balance");
            return false;
        }
        source.setBalance(source.getBalance() - amount - fees);
        dest.setBalance(dest.getBalance() + amount);
        updateWallet(source);
        updateWallet(dest);
        logger.info("Transfer successful from " + sourceId + " to " + destId);
        return true;
    }
    
    
    
    public Wallet getWalletByAddress(String address) {
        return walletRepository.findByAddress(address);
    }
    
    public boolean debitWallet(String walletId, double totalAmount) {
        Wallet source = getWalletById(walletId);
        if (source == null || totalAmount <= 0 || source.getBalance() < totalAmount) {
            logger.warning("Debit failed: invalid wallet or insufficient balance");
            return false;
        }
        source.setBalance(source.getBalance() - totalAmount);
        updateWallet(source);
        logger.info("Debited " + totalAmount + " from wallet " + walletId);
        return true;
    }


    
}