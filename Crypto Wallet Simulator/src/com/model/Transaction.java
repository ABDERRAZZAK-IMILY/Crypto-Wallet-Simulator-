package com.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private UUID id;
    private String senderAddress;
    private String receiverAddress;
    private double montant;
    private LocalDateTime creationDate;
    private double fees;
    private PriorityFees priorityFees;
    private TransactionStatus status;
    

    public Transaction(String senderAddress, String receiverAddress, double montant, PriorityFees priorityFees) {
        this.id = UUID.randomUUID();
        this.senderAddress = senderAddress;
        this.receiverAddress = receiverAddress;
        this.montant = montant;
        this.creationDate = LocalDateTime.now();
        this.priorityFees = priorityFees;
        this.status = TransactionStatus.PENDING;
        this.fees = 0.0;
    }

    public double calculateFees(Wallet wallet) {
        if (wallet.getCryptoType() == CryptoType.BITCOIN) {
            int sizeInBytes = 250; 
            double satPerByte;
            switch (priorityFees) {
                case ECONOMIQUE:
                    satPerByte = 1.0;
                    break;
                case STANDARD:
                    satPerByte = 5.0;
                    break;
                case RAPIDE:
                    satPerByte = 10.0;
                    break;
                default:
                    satPerByte = 5.0;
            }
            fees = sizeInBytes * satPerByte / 100000000.0;
        } else if (wallet.getCryptoType() == CryptoType.ETHEREUM) {
            long gasLimit = 21000;
            double gasPriceGwei;
            switch (priorityFees) {
                case ECONOMIQUE:
                    gasPriceGwei = 1.0;
                    break;
                case STANDARD:
                    gasPriceGwei = 5.0;
                    break;
                case RAPIDE:
                    gasPriceGwei = 10.0;
                    break;
                default:
                    gasPriceGwei = 5.0;
            }
            fees = gasLimit * gasPriceGwei * 1e-9;
        }
        return fees;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public double getFees() {
        return fees;
    }

    public void setFees(double fees) {
        this.fees = fees;
    }

    public PriorityFees getPriorityFees() {
        return priorityFees;
    }

    public void setPriorityFees(PriorityFees priorityFees) {
        this.priorityFees = priorityFees;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", sender='" + senderAddress + '\'' +
                ", receiver='" + receiverAddress + '\'' +
                ", montant=" + montant +
                ", fees=" + fees +
                ", priority=" + priorityFees +
                ", status=" + status +
                '}';
    }
}