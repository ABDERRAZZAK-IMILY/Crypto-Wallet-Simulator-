# Crypto Wallet Simulator

This is a console-based Java 8 application that simulates a crypto wallet with a mempool, allowing users to create wallets, manage transactions, and optimize transaction fees.

## Table of Contents
- [Description](#description)
- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
- [Prerequisites and Installation](#prerequisites-and-installation)
- [Database Setup](#database-setup)
- [How to Compile and Run](#how-to-compile-and-run)
- [Usage Guide](#usage-guide)
- [UML Diagram](#uml-diagram)

## Description
In the cryptocurrency ecosystem, every transaction requires fees to be validated by the network. These fees vary based on network congestion and desired urgency. This application simulates a crypto wallet with a mempool, helping users understand and optimize their transaction fees for Bitcoin and Ethereum.

Key features include:
- Creating Bitcoin or Ethereum wallets.
- Creating transactions with different fee priorities (Economique, Standard, Rapide).
- Simulating a mempool where transactions are prioritized by fees.
- Calculating a transaction's position in the mempool and estimating confirmation time.
- Comparing the cost and speed trade-offs of different fee levels.
- Viewing the current state of the mempool.

## Technologies Used
- **Java 8**: The core programming language.
- **PostgreSQL**: Relational database for persistent storage of wallets and transactions.
- **JDBC**: Java Database Connectivity for interacting with PostgreSQL.
- **java.util.logging**: For application logging.
- **Java Time API**: For date and time management.

## Project Structure
`
src/
├── com/
│ ├── model/ # Wallet and Transaction classes
│ ├── service/ # Business logic
│ ├── repository/ # Database interactions
│ └── main/ # Main application entry point
docker-compose.yml # Docker configuration
`
