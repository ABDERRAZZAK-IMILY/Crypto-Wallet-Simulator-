package com.model;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

public class Mempool {

    private static Mempool instance;
    private final PriorityQueue<Transaction> pool;


    private Mempool() {
        pool = new PriorityQueue<>(Comparator.comparingDouble(Transaction::getFees).reversed());

    }

    public static Mempool getInstance() {
        if (instance == null) {
            instance = new Mempool();
        }
        return instance;
    }

    public void addTransaction(Transaction tx) {
        pool.add(tx);
    }

    public void removeTransaction(Transaction tx) {
        pool.remove(tx);
    }

    public List<Transaction> getPool() {
        return pool.stream()
                   .sorted(Comparator.comparingDouble(Transaction::getFees).reversed())
                   .collect(Collectors.toList());
    }


    public int getPosition(Transaction tx) {
        List<Transaction> sorted = getPool();
        return sorted.indexOf(tx) + 1;
    }


    
    public int estimateTime(int position) {
        return position * 10; // minutes
    }

    public Optional<Transaction> getTransactionById(UUID id) {
        return pool.stream().filter(tx -> tx.getId().equals(id)).findFirst();
    }

    public void generateRandomTransactions(int count) {
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            String sender = "randomSender" + random.nextInt(1000);
            String receiver = "randomReceiver" + random.nextInt(1000);
            double montant = random.nextDouble() * 100;
            PriorityFees priority = PriorityFees.values()[random.nextInt(PriorityFees.values().length)];
            Transaction tx = new Transaction(sender, receiver, montant, priority);

            Wallet wallet;
            if (random.nextBoolean()) {
                wallet = new BitcoinWallet();
            } else {
                wallet = new EthereumWallet();
            }

            tx.calculateFees(wallet);

            addTransaction(tx);
        }
    }


    public void clear() {
        pool.clear();
    }

    
    
    public double totalFees() {
        return pool.stream().mapToDouble(Transaction::getFees).reduce(0, Double::sum);
    }
}