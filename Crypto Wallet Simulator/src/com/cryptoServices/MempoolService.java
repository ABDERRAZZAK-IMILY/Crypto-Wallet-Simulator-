package com.cryptoServices;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import com.cryptoRepository.TransactionRepository;
import com.model.BitcoinWallet;
import com.model.Mempool;
import com.model.PriorityFees;
import com.model.Transaction;
import com.model.TransactionStatus;
import com.model.Wallet;
import com.util.AppLogger;

public class MempoolService {

 private final Mempool mempool = Mempool.getInstance();
 private final TransactionRepository txRepo;
 private static final Logger logger = AppLogger.getLogger(MempoolService.class.getName());

 private final Timer scheduler = new Timer(true);
 
 public MempoolService(Connection connection) {
     this.txRepo = new TransactionRepository(connection);
     loadFromDB();
     startAutoConfirmTask();
 }

 
 private void startAutoConfirmTask() {
     scheduler.scheduleAtFixedRate(new TimerTask() {
         @Override
         public void run() {
             autoConfirmTransactions();
         }
     }, 0, 60_000);
 }

 
 private void autoConfirmTransactions() {
     List<Transaction> pool = new ArrayList<>(mempool.getPool());
     for (Transaction tx : pool) {
         int estimatedTime = estimateTime(tx);
         if (estimatedTime <= 0 && tx.getStatus() == TransactionStatus.PENDING) {
             tx.setStatus(TransactionStatus.CONFIRMED);
             txRepo.update(tx);
             mempool.removeTransaction(tx);
             logger.info("Transaction auto-confirmed: " + tx.getId());
         }
     }
 }
 
 
 private void loadFromDB() {
	    mempool.clear();
	    txRepo.findAll().stream()
	            .filter(tx -> tx.getStatus() == TransactionStatus.PENDING)
	            .forEach(mempool::addTransaction);
	    logger.info("Mempool loaded from DB");
	}

 public void addTransaction(Transaction tx) {
     if (tx.getStatus() == TransactionStatus.PENDING) {
         mempool.addTransaction(tx);
         logger.info("Transaction added to mempool: " + tx.getId());
     }
 }

 public int getPosition(Transaction tx) {
     return mempool.getPosition(tx);
 }

 public int estimateTime(Transaction tx) {
     return mempool.estimateTime(getPosition(tx));
 }

 public void generateRandomTransactions(int count) {
     mempool.generateRandomTransactions(count);
 }

 public void displayMempool(Transaction userTx) {
     System.out.println("=== ÉTAT DU MEMPOOL ===");
     List<Transaction> sorted = mempool.getPool();
     System.out.println("Transactions en attente : " + sorted.size());
     System.out.println("──────────────────────────────────┬────────-");
     System.out.println(" Transaction ID                   │ Frais  ");
     System.out.println("──────────────────────────────────┼────────");
     for (Transaction tx : sorted) {
         String label = tx.equals(userTx) ? "> vOTRE TX: " + tx.getId() : tx.getId() + "";
         System.out.printf("│ %-32s │ %9f$│%n", label, tx.getFees());
     }
     System.out.println("──────────────────────────────────┴────────");
 }

 public void compareFeeLevels(Transaction baseTx) {
     System.out.println("Comparaison des niveaux de frais:");
     System.out.println("───────────┬────────┬──────────┬──────────");
     System.out.println(" Niveau    │ Frais  │ Position │ Temps    ");
     System.out.println("───────────┼────────┼──────────┼──────────");

     for (PriorityFees level : PriorityFees.values()) {
         Transaction tempTx = new Transaction(baseTx.getSenderAddress(), baseTx.getReceiverAddress(), baseTx.getMontant(), level);
         Wallet dummyWallet = new BitcoinWallet();
         double fees = tempTx.calculateFees(dummyWallet);
         tempTx.setFees(fees);
         mempool.addTransaction(tempTx);
         int pos = getPosition(tempTx);
         int time = estimateTime(tempTx);
         System.out.printf("│ %-9s │ %9f$ │ %8d │ %8d min │%n", level, fees, pos, time);
         mempool.removeTransaction(tempTx);
     }
     System.out.println("└───────────┴────────┴──────────┴──────────┘");
 }
 
 public List<Transaction> getPool() {
     return new ArrayList<>(mempool.getPool());
 }
 
}