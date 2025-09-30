package com.ui;

import java.sql.Connection;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Logger;

import com.cryptoServices.MempoolService;
import com.cryptoServices.TransactionService;
import com.cryptoServices.WalletService;
import com.model.BitcoinWallet;
import com.model.EthereumWallet;
import com.model.PriorityFees;
import com.model.Transaction;
import com.model.Wallet;
import com.postgres.JDBCpostgresconnect;
import com.util.AppLogger;

public class Main {

    private static final Logger logger = AppLogger.getLogger(Main.class.getName());
    private static WalletService walletService;
    private static TransactionService txService;
    private static MempoolService mempoolService;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        logger.info("Application started");

        JDBCpostgresconnect db = JDBCpostgresconnect.getInstance();
        Connection conn = db.getConnection();
        if (conn == null) {
            System.out.println("Failed to connect to database. Exiting.");
            return;
        }

        walletService = new WalletService(conn);
        txService = new TransactionService(conn);
        mempoolService = new MempoolService(conn);

        showMenu();
        db.closeConnection();
    }

    private static void showMenu() {
        while (true) {
            System.out.println("\nCrypto Wallet Simulator Menu:");
            System.out.println("1. Create Wallet");
            System.out.println("2. Create Transaction");
            System.out.println("3. View Position in Mempool");
            System.out.println("4. Compare Fee Levels");
            System.out.println("5. View Mempool State");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = getIntInput();

            switch (choice) {
                case 1:
                    createWallet();
                    break;
                case 2:
                    createTransaction();
                    break;
                case 3:
                    viewPosition();
                    break;
                case 4:
                    compareFees();
                    break;
                case 5:
                    viewMempool();
                    break;
                case 6:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void createWallet() {
        System.out.print("Choose crypto type (1: BITCOIN, 2: ETHEREUM): ");
        int type = getIntInput();
        Wallet wallet = type == 1 ? new BitcoinWallet() : new EthereumWallet();
        walletService.createWallet(wallet);
        System.out.println("Wallet created: Address = " + wallet.getAddress() + ", ID = " + wallet.getId());
    }

    private static void createTransaction() {
        System.out.print("Enter wallet ID: ");
        String walletId = scanner.nextLine();
        Wallet wallet = walletService.getWalletById(walletId);
        if (wallet == null) {
            System.out.println("Wallet not found.");
            return;
        }

        System.out.print("Enter receiver address: ");
        String receiver = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = getDoubleInput();
        System.out.print("Choose priority (1: ECONOMIQUE, 2: STANDARD, 3: RAPIDE): ");
        int prio = getIntInput();
        PriorityFees priority;
        switch (prio) {
            case 1:
                priority = PriorityFees.ECONOMIQUE;
                break;
            case 2:
                priority = PriorityFees.STANDARD;
                break;
            case 3:
                priority = PriorityFees.RAPIDE;
                break;
            default:
                priority = PriorityFees.STANDARD;
        }

        try {
            Transaction tx = txService.createTransaction(wallet, receiver, amount, priority);
            mempoolService.addTransaction(tx);
            System.out.println("Transaction created: ID = " + tx.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewPosition() {
        System.out.print("Enter transaction ID: ");
        String txIdStr = scanner.nextLine();
        UUID txId;
        try {
            txId = UUID.fromString(txIdStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID.");
            return;
        }
        mempoolService.getPool().stream()
        .filter(tx -> tx.getId().equals(txId))
        .findFirst()
        .ifPresent(tx -> {
            int pos = mempoolService.getPosition(tx);
            int time = mempoolService.estimateTime(tx);
            System.out.println("Your transaction is in position " + pos + " of " + mempoolService.getPool().size());
            System.out.println("Estimated time: " + time + " minutes");
        });
        
        if (!mempoolService.getPool().stream().anyMatch(tx -> tx.getId().equals(txId))) {
            System.out.println("Transaction not found in mempool.");
        }
    }

    private static void compareFees() {
        System.out.print("Enter base transaction ID for comparison: ");
        String txIdStr = scanner.nextLine();
        UUID txId;
        try {
            txId = UUID.fromString(txIdStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID.");
            return;
        }
        mempoolService.getPool().stream()
                .filter(tx -> tx.getId().equals(txId))
                .findFirst()
                .ifPresent(mempoolService::compareFeeLevels);
    }

    private static void viewMempool() {
        mempoolService.generateRandomTransactions(15);
        System.out.print("Enter your transaction ID (optional): ");
        String txIdStr = scanner.nextLine();
        Transaction userTx = null;
        if (!txIdStr.isEmpty()) {
            UUID txId = UUID.fromString(txIdStr);
            userTx = mempoolService.getPool().stream()
                    .filter(tx -> tx.getId().equals(txId))
                    .findFirst().orElse(null);
        }
        mempoolService.displayMempool(userTx);
    }

    private static int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Enter a number.");
            return getIntInput();
        }
    }

    private static double getDoubleInput() {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Enter a number.");
            return getDoubleInput();
        }
    }
}