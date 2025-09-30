package com.cryptoRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.model.BitcoinWallet;
import com.model.CryptoType;
import com.model.EthereumWallet;
import com.model.Wallet;
import com.ui.Main;
import com.util.AppLogger;

public class WalletRepository extends GenericRepositoryImpl<Wallet, String> {

    private static final Logger logger = AppLogger.getLogger(Main.class.getName());

    public WalletRepository(Connection connection) {
        super(connection, "wallets");
    }

    @Override
    public void save(Wallet wallet) {
        String sql = "INSERT INTO wallets (id, address, balance, crypto_type) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, wallet.getId());
            ps.setString(2, wallet.getAddress());
            ps.setDouble(3, wallet.getBalance());
            ps.setString(4, wallet.getCryptoType().name());
            ps.executeUpdate();
            logger.info("Wallet saved successfully!");
        } catch (SQLException e) {
            logger.severe("Error saving wallet: " + e.getMessage());
        }
    }

    @Override
    public Wallet findById(String id) {
        String sql = "SELECT * FROM wallets WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    CryptoType type = CryptoType.valueOf(rs.getString("crypto_type"));
                    Wallet wallet;
                    if (type == CryptoType.BITCOIN) {
                        wallet = new BitcoinWallet();
                    } else {
                        wallet = new EthereumWallet();
                    }
                    wallet.setAddress(rs.getString("address"));
                    wallet.setBalance(rs.getDouble("balance"));
                    return wallet;
                }
            }
        } catch (SQLException e) {
            logger.severe("Error finding wallet: " + e.getMessage());
        }
        return null;
    }

    
    
    public Wallet findByAddress(String address) {
        String sql = "SELECT * FROM wallets WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, address);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    CryptoType type = CryptoType.valueOf(rs.getString("crypto_type"));
                    Wallet wallet;
                    if (type == CryptoType.BITCOIN) {
                        wallet = new BitcoinWallet();
                    } else {
                        wallet = new EthereumWallet();
                    }
                    wallet.setAddress(rs.getString("address"));
                    wallet.setBalance(rs.getDouble("balance"));
                    return wallet;
                }
            }
        } catch (SQLException e) {
            logger.severe("Error finding wallet: " + e.getMessage());
        }
        return null;
    }
    
    
    
    
    @Override
    public void update(Wallet wallet) {
        String sql = "UPDATE wallets SET address = ?, balance = ?, crypto_type = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, wallet.getAddress());
            ps.setDouble(2, wallet.getBalance());
            ps.setString(3, wallet.getCryptoType().name());
            ps.setString(4, wallet.getId());
            int updated = ps.executeUpdate();
            if (updated > 0) {
                logger.info("Wallet updated successfully!");
            } else {
                logger.warning("Wallet not found for update: " + wallet.getId());
            }
        } catch (SQLException e) {
            logger.severe("Error updating wallet: " + e.getMessage());
        }
    }

    @Override
    public List<Wallet> findAll() {
        List<Wallet> wallets = new ArrayList<>();
        String sql = "SELECT * FROM wallets";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                CryptoType type = CryptoType.valueOf(rs.getString("crypto_type"));
                Wallet wallet;
                if (type == CryptoType.BITCOIN) {
                    wallet = new BitcoinWallet();
                } else {
                    wallet = new EthereumWallet();
                }
                wallet.setAddress(rs.getString("address"));
                wallet.setBalance(rs.getDouble("balance"));
                wallets.add(wallet);
            }
        } catch (SQLException e) {
            logger.severe("Error fetching wallets: " + e.getMessage());
        }
        return wallets;
    }
}
