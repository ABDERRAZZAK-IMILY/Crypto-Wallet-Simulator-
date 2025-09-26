package com.model;

import java.util.UUID;

public abstract class Wallet {

	
	private String id;
	private String address;
	private double balance;
	private CryptoType cryptotype;
	
	
	public Wallet(CryptoType cryptotype ) {
		this.setId(UUID.randomUUID().toString());
		this.address = generateAddress();
		this.balance = 0;
		this.cryptotype = cryptotype;
		}

	
	public abstract String generateAddress();

   public String getId() {
	   return id;
   }
	
   public void setId(String id) {
	    this.id = id;
   }
   
   public String getAddress() {
	   return address;
   }
   
   public void setAddress(String address) {
	   this.address = address;
   }
   
   public double getBalance() {
	   return balance;
   }
   
   public CryptoType getCryptoType() {
	   return cryptotype;
   }
	
   public void setBalance(double balance) {
	   this.balance = balance;
   }
   
   public void setCryptoType(CryptoType cryptotype) {
	   this.cryptotype = cryptotype;
   }
	
}
