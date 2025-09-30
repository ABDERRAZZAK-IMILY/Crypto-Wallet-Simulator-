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
 private static final Logger logger = AppLogger.getLogger(TransactionService.class.getName());

 private static final Pattern BTC_ADDRESS_PATTERN = Pattern.compile("^[13][a-km-zA-HJ-NP-Z1-9]{25,34}$|^bc1[a-zA-HJ-NP-Z0-9]{39,59}$");
 private static final Pattern ETH_ADDRESS_PATTERN = Pattern.compile("^0x[a-fA-F0-9]{40}$");

 public TransactionService(Connection connection) {
     this.txRepo = new TransactionRepository(connection);
 }

 public Transaction createTransaction(Wallet wallet, String receiverAddress, double montant, PriorityFees priority) {
     if (montant <= 0) {
         throw new IllegalArgumentException("Montant must be positive");
     }
     String sender = wallet.getAddress();
     validateAddress(wallet.getCryptoType(), sender);
     validateAddress(wallet.getCryptoType(), receiverAddress);

     Transaction tx = new Transaction(sender, receiverAddress, montant, priority);
     double fees = tx.calculateFees(wallet);
     if (wallet.getBalance() < montant + fees) {
         throw new IllegalArgumentException("Insufficient balance");
     }
     tx.setFees(fees);
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