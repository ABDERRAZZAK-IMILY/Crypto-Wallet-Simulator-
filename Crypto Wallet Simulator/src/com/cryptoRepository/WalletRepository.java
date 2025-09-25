package com.cryptoRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.model.CryptoType;
import com.model.Wallet;

public class WalletRepository  extends GenericRepositoryImpl<Wallet, Integer> {

	  public WalletRepository(Connection connection) {
	        super(connection, "wallet");
	    }
	
	  @Override
	    public void save(Wallet wallet) {
	        try {
	            PreparedStatement ps = connection.prepareStatement(
	                "INSERT INTO wallet ( balance, crypto_type) VALUES (?, ?, ?)"
	            );
	            ps.setDouble(2, wallet.getBalance());
	            ps.setString(3, wallet.getCryptoType().name());
	            ps.executeUpdate();
	            System.out.println("Wallet saved successfully!");
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    @Override
	    public Wallet findById(Integer id) {
	        try {
	            PreparedStatement ps = connection.prepareStatement(
	                "SELECT * FROM wallet WHERE id = ?"
	            );
	            ps.setInt(1, id);
	            ResultSet rs = ps.executeQuery();
	            if (rs.next()) {
	                return new Wallet(
	                    rs.getString("id"),
	                    rs.getDouble("balance"),
	                    CryptoType.valueOf(rs.getString("crypto_type"))
	                );
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }

	    @Override
	    public void update(Wallet wallet) {
	        try {
	            PreparedStatement ps = connection.prepareStatement(
	                "UPDATE wallet SET  balance = ?, crypto_type = ? WHERE id = ?"
	            );
	            ps.setDouble(2, wallet.getBalance());
	            ps.setString(3, wallet.getCryptoType().name());
	            ps.setString(4, wallet.getId());
	            ps.executeUpdate();
	            System.out.println("Wallet updated successfully!");
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    @Override
	    public List<Wallet> findAll() {
	        List<Wallet> wallets = new ArrayList<>();
	        try {
	            Statement stmt = connection.createStatement();
	            ResultSet rs = stmt.executeQuery("SELECT * FROM wallet");
	            while (rs.next()) {
	                wallets.add(new Wallet(
	                    rs.getString("id"),
	                    rs.getDouble("balance"),
	                    CryptoType.valueOf(rs.getString("crypto_type"))
	                ));
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return wallets;
	    }
	}
	  
