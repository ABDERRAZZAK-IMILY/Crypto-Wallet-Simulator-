package com.model;

import java.security.SecureRandom;

public class EthereumWallet extends Wallet {

    public EthereumWallet() {
        super(CryptoType.ETHEREUM);
    }

    @Override
    public String generateAddress() {
        return "0x" + randomString(40);
    }

    private String randomString(int length) {
        String chars = "abcdef0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
