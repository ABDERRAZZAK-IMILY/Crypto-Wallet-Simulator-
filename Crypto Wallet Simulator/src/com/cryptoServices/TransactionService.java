package com.cryptoServices;

import java.sql.Connection;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.cryptoRepository.TransactionRepository;
import com.model.CryptoType;
import com.model.PriorityFees;
import com.model.Transaction;
import com.model.TransactionStatus;
import com.model.Wallet;
import com.util.AppLogger;

public class TransactionService {

 private final TransactionRepository txRepo;
 private final WalletService walletService;
 
 private static final Logger logger = AppLogger.getLogger(TransactionService.class.getName());

 private static final Pattern BTC_ADDRESS_PATTERN = Pattern.compile("^[13][a-km-zA-HJ-NP-Z1-9]{25,34}$|^bc1[a-zA-HJ-NP-Z0-9]{39,59}$");
 private static final Pattern ETH_ADDRESS_PATTERN = Pattern.compile("^0x[a-fA-F0-9]{40}$");

 public TransactionService(Connection connection , WalletService walletService) {
     this.txRepo = new TransactionRepository(connection);
     this.walletService = walletService;
 }

 
 
 public Transaction createTransaction(Wallet wallet, String receiverAddress, double montant, PriorityFees priority) {
	    if (montant <= 0) throw new IllegalArgumentException("Montant must be positive");
	    String sender = wallet.getAddress();
	    validateAddress(wallet.getCryptoType(), sender);
       
	    if (receiverAddress != null && !receiverAddress.isEmpty()) {
	        validateAddress(wallet.getCryptoType(), receiverAddress);
	    }
	    

	    Transaction tx = new Transaction(sender, receiverAddress, montant, priority);
	    double fees = tx.calculateFees(wallet);
	    tx.setFees(fees);

	    double total = montant + fees;
	    if (wallet.getBalance() < total) {
	        throw new IllegalArgumentException("Insufficient balance");
	    }

	    Wallet dest = walletService.getWalletByAddress(receiverAddress);

	    boolean ok;
	    if (dest != null) {
	        ok = walletService.transferFunds(wallet.getId(), dest.getId(), montant, fees);
	    } else {
	        ok = walletService.debitWallet(wallet.getId(), total);
	    }

	    if (!ok) {
	        throw new IllegalArgumentException("Failed to update wallets (insufficient funds or DB error)");
	    }

	    txRepo.save(tx);
	    logger.info("Transaction created: " + tx.getId());
	    return tx;
	}
 
 
 
 
 
 

 private void validateAddress(CryptoType type, String address) {
     Pattern pattern = type == CryptoType.BITCOIN ? BTC_ADDRESS_PATTERN : ETH_ADDRESS_PATTERN;
     if (!pattern.matcher(address).matches()) {
         throw new IllegalArgumentException("Invalid address format for " + type);
     }
 }

 public void confirmTransaction(Transaction tx) {
     tx.setStatus(TransactionStatus.CONFIRMED);
     txRepo.update(tx);
 }

 public void rejectTransaction(Transaction tx) {
     tx.setStatus(TransactionStatus.REJECTED);
     txRepo.update(tx);
 }

 public List<Transaction> getAllTransactions() {
     return txRepo.findAll();
 }
}