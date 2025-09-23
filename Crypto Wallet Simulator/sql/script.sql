-- sql script

CREATE TABLE IF NOT EXISTS wallets (
    id VARCHAR(255) PRIMARY KEY,
    address VARCHAR(255) NOT NULL UNIQUE,
    balance DOUBLE PRECISION NOT NULL,
    crypto_type VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS transactions (
    id VARCHAR(255) PRIMARY KEY,
    source_address VARCHAR(255) NOT NULL,
    destination_address VARCHAR(255) NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    fees DOUBLE PRECISION NOT NULL,
    fee_priority VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    crypto_type VARCHAR(50) NOT NULL
);
