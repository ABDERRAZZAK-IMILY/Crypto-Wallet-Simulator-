package com.cryptoRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import com.model.PriorityFees;
import com.model.Transaction;
import com.model.TransactionStatus;
import com.util.AppLogger;

public class TransactionRepository extends GenericRepositoryImpl<Transaction, UUID> {

    private static final Logger logger = AppLogger.getLogger(TransactionRepository.class.getName());

    public TransactionRepository(Connection connection) {
        super(connection, "transactions");
    }

    @Override
    public void save(Transaction tx) {
        String sql = "INSERT INTO transactions " +
                     "(id, source_address, destination_address, amount, creation_date, fees, fee_priority, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, tx.getId());
            ps.setString(2, tx.getSenderAddress());
            ps.setString(3, tx.getReceiverAddress());
            ps.setDouble(4, tx.getMontant());
            ps.setTimestamp(5, Timestamp.valueOf(tx.getCreationDate()));
            ps.setDouble(6, tx.getFees());
            ps.setString(7, tx.getPriorityFees().name());
            ps.setString(8, tx.getStatus().name());
            ps.executeUpdate();
            logger.info("Transaction saved: " + tx.getId());
        } catch (SQLException e) {
            logger.severe("Error saving transaction: " + e.getMessage());
        }
    }

    @Override
    public Transaction findById(UUID id) {
        String sql = "SELECT * FROM transactions WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Transaction tx = new Transaction(
                        rs.getString("source_address"),
                        rs.getString("destination_address"),
                        rs.getDouble("amount"),
                        PriorityFees.valueOf(rs.getString("fee_priority"))
                    );
                    tx.setFees(rs.getDouble("fees"));
                    tx.setStatus(TransactionStatus.valueOf(rs.getString("status")));
                    tx.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());
                    return tx;
                }
            }
        } catch (SQLException e) {
            logger.severe("Error finding transaction: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void update(Transaction tx) {
        String sql = "UPDATE transactions SET " +
                     "source_address = ?, destination_address = ?, amount = ?, creation_date = ?, " +
                     "fees = ?, fee_priority = ?, status = ? " +
                     "WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, tx.getSenderAddress());
            ps.setString(2, tx.getReceiverAddress());
            ps.setDouble(3, tx.getMontant());
            ps.setTimestamp(4, Timestamp.valueOf(tx.getCreationDate()));
            ps.setDouble(5, tx.getFees());
            ps.setString(6, tx.getPriorityFees().name());
            ps.setString(7, tx.getStatus().name());
            ps.setObject(8, tx.getId());
            ps.executeUpdate();
            logger.info("Transaction updated: " + tx.getId());
        } catch (SQLException e) {
            logger.severe("Error updating transaction: " + e.getMessage());
        }
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Transaction tx = new Transaction(
                    rs.getString("source_address"),
                    rs.getString("destination_address"),
                    rs.getDouble("amount"),
                    PriorityFees.valueOf(rs.getString("fee_priority"))
                );
                tx.setFees(rs.getDouble("fees"));
                tx.setStatus(TransactionStatus.valueOf(rs.getString("status")));
                tx.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());
                transactions.add(tx);
            }
        } catch (SQLException e) {
            logger.severe("Error fetching transactions: " + e.getMessage());
        }
        return transactions;
    }
}
