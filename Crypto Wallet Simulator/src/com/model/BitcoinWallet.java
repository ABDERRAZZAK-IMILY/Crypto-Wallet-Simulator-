package com.model;

import java.security.SecureRandom;

public class BitcoinWallet extends Wallet {
	
	public  BitcoinWallet() {
		super(CryptoType.BITCOIN);
		
	}
	
	@Override
	public String generateAddress() {
	    SecureRandom random = new SecureRandom();
	    int length = 39 + random.nextInt(21);
	    return "bc1" + randomString(length).toLowerCase();
	}

	   private String randomString(int length) {
	        String chars = "ABCDEFGHJKLMNOPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz123456789";
	        SecureRandom random = new SecureRandom();
	        StringBuilder sb = new StringBuilder();
	        for (int i = 0; i < length; i++) {
	            sb.append(chars.charAt(random.nextInt(chars.length())));
	        }
	        return sb.toString();
	    }
}
