# 📒 Account Ledger Library - Kotlin

[![Java CI with Gradle](https://github.com/Baneeishaque/Account-Ledger-Library-Kotlin/actions/workflows/gradle.yml/badge.svg)](https://github.com/Baneeishaque/Account-Ledger-Library-Kotlin/actions/workflows/gradle.yml)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.20-blue.svg)](https://kotlinlang.org)
[![Gradle](https://img.shields.io/badge/Gradle-9.1.0-brightgreen.svg)](https://gradle.org)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A comprehensive, multi-platform Kotlin library for managing financial account ledgers, transactions, and balance sheets. This library provides a robust API client for interacting with account ledger backends, supporting features like user authentication, account management, transaction handling, and financial reporting.

---

## 📋 Table of Contents

- [Features](#-features)
- [Architecture](#-architecture)
- [Project Structure](#-project-structure)
- [Prerequisites](#-prerequisites)
- [Getting Started](#-getting-started)
  - [Clone the Repository](#clone-the-repository)
  - [Build the Project](#build-the-project)
  - [Run Tests](#run-tests)
- [Configuration](#-configuration)
- [API Reference](#-api-reference)
- [Usage Examples](#-usage-examples)
- [Multi-Platform Support](#-multi-platform-support)
- [Dependencies](#-dependencies)
- [Contributing](#-contributing)
- [Code Style](#-code-style)
- [CI/CD](#-cicd)
- [Troubleshooting](#-troubleshooting)
- [License](#-license)
- [Acknowledgments](#-acknowledgments)

---

## ✨ Features

### Core Functionality
- **User Authentication**: Secure user login and session management
- **Account Management**: Create, retrieve, and manage hierarchical account structures
- **Transaction Operations**: Full CRUD operations for financial transactions
  - Insert, update, and delete transactions
  - Support for multiple transaction types (Normal, Two-Way, Via, Cyclic Via, Special)
- **Balance Sheet Generation**: Automated financial reports including:
  - Balance sheets
  - Income statements
  - Expense reports
  - Debit/Credit summaries
  - Asset tracking

### Advanced Features
- **Account Frequency Tracking**: Smart suggestions based on frequently used accounts
- **Multi-User Support**: Isolated data management per user
- **Date-Range Filtering**: Query transactions within specific date ranges
- **Environment-Based Configuration**: Flexible configuration via `.env` files
- **Console & API Modes**: Works both as an interactive CLI tool and as a library

### Technical Highlights
- **Kotlin Multiplatform Ready**: Core components available for JVM, Native, and JS targets
- **Coroutine Support**: Fully asynchronous API operations using Kotlin Coroutines
- **Retrofit Integration**: Type-safe HTTP client for backend communication
- **Ktor Client**: Modern HTTP client with content negotiation and logging
- **Kotlinx Serialization**: JSON serialization/deserialization with full type safety

---

## 🏗 Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                    Application Layer                            │
├─────────────────────────────────────────────────────────────────┤
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────────┐  │
│  │  Operations  │  │    Utils     │  │      Models          │  │
│  │  - Server    │  │  - Account   │  │  - Transaction       │  │
│  │  - Insert    │  │  - User      │  │  - Account           │  │
│  │  - Data      │  │  - Special   │  │  - BalanceSheet      │  │
│  │  - Ledger    │  │    Transaction│  │  - User              │  │
│  │  - File      │  │              │  │                      │  │
│  │  - Checking  │  │              │  │                      │  │
│  └──────────────┘  └──────────────┘  └──────────────────────┘  │
├─────────────────────────────────────────────────────────────────┤
│                      API Layer                                   │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  Retrofit API Interface                                   │   │
│  │  - authenticateUser()    - selectUserAccounts()          │   │
│  │  - selectUsers()         - insertTransaction()           │   │
│  │  - updateTransaction()   - deleteTransaction()           │   │
│  │  - selectUserTransactions()                              │   │
│  └──────────────────────────────────────────────────────────┘   │
├─────────────────────────────────────────────────────────────────┤
│                    Data Sources                                  │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────────────────┐   │
│  │ Accounts    │ │Transaction  │ │ Authentication          │   │
│  │ DataSource  │ │ DataSource  │ │ DataSource              │   │
│  └─────────────┘ └─────────────┘ └─────────────────────────┘   │
├─────────────────────────────────────────────────────────────────┤
│                    Network Layer                                 │
│  ┌─────────────────────┐  ┌──────────────────────────────────┐ │
│  │   Retrofit Client   │  │         Ktor Client              │ │
│  │   (REST API)        │  │   (Alternative HTTP Client)      │ │
│  └─────────────────────┘  └──────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

---

## 📁 Project Structure

```
Account-Ledger-Library-Kotlin/
├── account-ledger-lib/                 # Main JVM library module
│   ├── build.gradle.kts                # Module build configuration
│   └── src/main/kotlin/account/ledger/library/
│       ├── api/                        # API interfaces and constants
│       │   ├── Api.kt                  # Retrofit API interface
│       │   ├── ApiConstants.kt         # API configuration constants
│       │   ├── ProjectApiUtils.kt      # API utility functions
│       │   └── response/               # API response models
│       │       ├── AccountResponse.kt
│       │       ├── AccountsResponse.kt
│       │       ├── AuthenticationResponse.kt
│       │       ├── MultipleTransactionResponse.kt
│       │       ├── MultipleUserResponse.kt
│       │       ├── TransactionManipulationResponse.kt
│       │       ├── TransactionResponse.kt
│       │       └── UserResponse.kt
│       ├── constants/                  # Application constants
│       │   ├── EnvironmentalFileEntries.kt
│       │   ├── UserJsonObjectFields.kt
│       │   └── ...
│       ├── enums/                      # Enumeration types
│       │   ├── AccountTypeEnum.kt
│       │   ├── TransactionTypeEnum.kt
│       │   ├── BalanceSheetRefineLevelEnum.kt
│       │   └── ...
│       ├── models/                     # Data models
│       │   ├── TransactionModel.kt
│       │   ├── AccountFrequencyModel.kt
│       │   ├── BalanceSheetDataRowModel.kt
│       │   └── ...
│       ├── operations/                 # Business logic operations
│       │   ├── ServerOperations.kt     # Server communication
│       │   ├── InsertOperations.kt     # Transaction insertions
│       │   ├── LedgerSheetOperations.kt# Balance sheet generation
│       │   ├── DataOperations.kt       # Data processing
│       │   ├── FileOperations.kt       # File handling
│       │   └── CheckingOperations.kt   # Validation logic
│       ├── retrofit/                   # Retrofit configuration
│       │   ├── ProjectRetrofitClient.kt
│       │   ├── ResponseHolder.kt
│       │   └── data/                   # Data source implementations
│       │       ├── AccountsDataSource.kt
│       │       ├── AuthenticationDataSource.kt
│       │       ├── MultipleTransactionDataSource.kt
│       │       └── ...
│       └── utils/                      # Utility functions
│           ├── AccountUtils.kt
│           ├── TransactionUtils.kt
│           ├── UserUtils.kt
│           └── ...
│
├── account-ledger-lib-multi-platform/  # Kotlin Multiplatform module (submodule)
│   └── lib/                            # Multiplatform library code
│
├── common-lib/                         # Common utilities library (submodule)
│   └── common-lib/                     # Shared utilities and models
│
├── gradle/
│   ├── libs.versions.toml              # Version catalog for dependencies
│   └── wrapper/                        # Gradle wrapper files
│
├── .github/
│   └── workflows/
│       └── gradle.yml                  # GitHub Actions CI workflow
│
├── build.gradle.kts                    # Root build configuration
├── settings.gradle.kts                 # Project settings
├── gradle.properties                   # Gradle properties
├── gradlew                             # Gradle wrapper (Unix)
├── gradlew.bat                         # Gradle wrapper (Windows)
├── azure-pipelines-windows.yml         # Azure Pipelines configuration
└── renovate.json                       # Renovate dependency updates config
```

---

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

| Requirement | Version | Description |
|-------------|---------|-------------|
| **JDK** | 21+ | Oracle JDK or OpenJDK |
| **Kotlin** | 2.2.20 | Bundled with Gradle |
| **Gradle** | 9.1.0 | Use the included wrapper |
| **Git** | Latest | For version control |

### Verify Installation

```bash
# Check Java version
java -version

# Check Gradle (using wrapper)
./gradlew --version
```

---

## 🚀 Getting Started

### Clone the Repository

```bash
# Clone with submodules
git clone --recursive https://github.com/Baneeishaque/Account-Ledger-Library-Kotlin.git

# Navigate to project directory
cd Account-Ledger-Library-Kotlin

# If you already cloned without --recursive, initialize submodules
git submodule update --init --recursive
```

### Build the Project

```bash
# Unix/macOS
./gradlew assemble

# Windows
gradlew.bat assemble

# Build with parallel execution (faster)
./gradlew assemble --parallel

# Clean and rebuild
./gradlew clean assemble
```

### Run Tests

```bash
# Run all tests
./gradlew test

# Run tests with verbose output
./gradlew test --info

# Run specific module tests
./gradlew :account-ledger-lib:test
```

---

## ⚙ Configuration

### Environment Variables

Create a `.env` file in your project root with the following configuration:

```env
# API Configuration
API_BASE_URL=https://your-api-server.com/api/

# Account IDs for Balance Sheet Generation
OPEN_BALANCE_ACCOUNT_IDS=1,2,3
MISC_INCOME_ACCOUNT_IDS=4,5
INVESTMENT_RETURNS_ACCOUNT_IDS=6,7
FAMILY_ACCOUNT_IDS=8,9
EXPENSE_ACCOUNT_IDS=10,11,12

# Sheet Configuration
INCOME_ACCOUNT_IDS_FOR_SHEET=20,21,22
EXPENSE_ACCOUNT_IDS_FOR_SHEET=30,31,32
ASSET_ACCOUNT_IDS_FOR_SHEET=40,41,42
DEBIT_OR_CREDIT_ACCOUNT_IDS_FOR_SHEET=50,51,52
EXPENSE_INCOME_IGNORE_ACCOUNT_IDS_FOR_SHEET=60,61
EXPENSE_INCOME_DEBIT_CREDIT_IGNORE_ACCOUNT_IDS_FOR_SHEET=70,71
EXPENSE_INCOME_DEBIT_CREDIT_ASSET_IGNORE_ACCOUNT_IDS_FOR_SHEET=80,81
```

### API Constants

Configure the API endpoint in `ApiConstants.kt`:

```kotlin
object ApiConstants {
    const val serverApiAddress = "https://your-api-server.com/api/"
    const val serverFileExtension = "php"
    
    // API Methods
    const val selectUserMethod = "select_User"
    const val selectUsersMethod = "select_Users"
    const val selectUserAccountsV2Method = "select_User_Accounts_v2"
    const val selectUserAccountsFullMethod = "select_User_Accounts_Full"
    // ... more methods
}
```

---

## 📚 API Reference

### Authentication

```kotlin
// Authenticate a user
suspend fun authenticateUser(username: String, password: String): Response<AuthenticationResponse>
```

### Account Operations

```kotlin
// Get all user accounts
suspend fun selectUserAccountsFull(userId: UInt): Response<AccountsResponse>

// Get accounts by parent
suspend fun selectUserAccounts(userId: UInt, parentAccountId: UInt): Response<AccountsResponse>
```

### Transaction Operations

```kotlin
// Insert a new transaction
suspend fun insertTransaction(
    eventDateTimeString: String,
    userId: UInt,
    particulars: String,
    amount: Float,
    fromAccountId: UInt,
    toAccountId: UInt
): Response<TransactionManipulationResponse>

// Update an existing transaction
suspend fun updateTransaction(
    eventDateTimeString: String,
    particulars: String,
    amount: Float,
    fromAccountId: UInt,
    toAccountId: UInt,
    transactionId: UInt
): Response<TransactionManipulationResponse>

// Delete a transaction
suspend fun deleteTransaction(transactionId: UInt): Response<TransactionManipulationResponse>

// Get user transactions
suspend fun selectUserTransactionsV2M(userId: UInt, accountId: UInt): Response<MultipleTransactionResponse>
```

---

## 💡 Usage Examples

### Basic Transaction Insertion

```kotlin
import account.ledger.library.operations.InsertOperations

// Insert a transaction
val success = InsertOperations.insertTransaction(
    userId = 1u,
    eventDateTime = "2024-01-15 10:30:00",
    particulars = "Office Supplies",
    amount = 150.50f,
    fromAccountId = 5u,
    toAccountId = 12u,
    isConsoleMode = true,
    isDevelopmentMode = false
)

if (success) {
    println("Transaction inserted successfully!")
}
```

### Generate Balance Sheet

```kotlin
import account.ledger.library.operations.LedgerSheetOperations
import io.github.cdimascio.dotenv.dotenv

val dotEnv = dotenv()

// Print balance sheet for a user
LedgerSheetOperations.printBalanceSheetOfUser(
    currentUserName = "john_doe",
    currentUserId = 1u,
    isConsoleMode = true,
    isDevelopmentMode = false,
    dotEnv = dotEnv
)
```

### Fetch User Accounts

```kotlin
import account.ledger.library.operations.ServerOperations

val result = ServerOperations.getAccounts(
    userId = 1u,
    parentAccountId = 0u,
    isConsoleMode = true,
    isDevelopmentMode = false
)

result.onSuccess { accountsResponse ->
    accountsResponse.accounts.forEach { account ->
        println("${account.id}: ${account.fullName}")
    }
}
```

### Track Account Frequency

```kotlin
import account.ledger.library.utils.AccountUtils

// Get frequently used accounts for suggestions
val frequentAccounts = AccountUtils.getFrequentlyUsedTop10Accounts(
    userId = 1u,
    isDevelopmentMode = false
)
println(frequentAccounts)
```

---

## 🌐 Multi-Platform Support

This project includes Kotlin Multiplatform modules for cross-platform compatibility:

| Platform | Module | Status |
|----------|--------|--------|
| JVM | `account-ledger-lib` | ✅ Full Support |
| Native (Windows) | `account-ledger-lib-multi-platform` | ✅ Available |
| Native (Linux) | `account-ledger-lib-multi-platform` | 🔄 In Progress |
| Native (macOS) | `account-ledger-lib-multi-platform` | 🔄 In Progress |
| Android | Planned | 📋 Roadmap |

### Building Native Binaries

```bash
# Windows (MinGW)
./gradlew mingwX64Binaries

# Linux
./gradlew linuxX64Binaries

# macOS
./gradlew macosX64Binaries
```

---

## 📦 Dependencies

### Core Dependencies

| Dependency | Version | Purpose |
|------------|---------|---------|
| Kotlin | 2.2.20 | Programming language |
| Ktor Client | 3.3.1 | HTTP client |
| Retrofit | 3.0.0 | REST API client |
| Kotlinx Coroutines | 1.10.2 | Asynchronous programming |
| Kotlinx Serialization | 1.9.0 | JSON serialization |
| Logback | 1.5.19 | Logging framework |
| Dotenv Kotlin | 6.5.1 | Environment configuration |
| Kotlin CSV | 1.10.0 | CSV file handling |

### Development Dependencies

| Dependency | Version | Purpose |
|------------|---------|---------|
| Kotlin Test | Bundled | Testing framework |
| Gradle | 9.1.0 | Build system |

---

## 🤝 Contributing

We welcome contributions! Here's how you can help:

### Getting Started

1. **Fork the repository**
   ```bash
   # Fork via GitHub, then clone your fork
   git clone https://github.com/YOUR_USERNAME/Account-Ledger-Library-Kotlin.git
   ```

2. **Create a feature branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **Make your changes**
   - Write clean, documented code
   - Follow the existing code style
   - Add tests for new functionality

4. **Commit your changes**
   ```bash
   git commit -m "feat: add your feature description"
   ```

5. **Push and create a Pull Request**
   ```bash
   git push origin feature/your-feature-name
   ```

### Contribution Guidelines

- **Code Quality**: Ensure your code compiles without warnings
- **Testing**: Add unit tests for new functionality
- **Documentation**: Update README or docs if needed
- **Commits**: Use conventional commit messages
  - `feat:` for new features
  - `fix:` for bug fixes
  - `docs:` for documentation
  - `refactor:` for code refactoring
  - `test:` for test additions

### Areas for Contribution

- [ ] Add more comprehensive unit tests
- [ ] Improve error handling and messages
- [ ] Add iOS/Android platform support
- [ ] Create sample applications
- [ ] Improve documentation
- [ ] Add internationalization support
- [ ] Performance optimizations

---

## 📝 Code Style

This project follows the [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html).

### Key Style Points

- **Naming**: Use camelCase for functions and variables, PascalCase for classes
- **Formatting**: 4-space indentation
- **Documentation**: Use KDoc for public APIs
- **Null Safety**: Prefer non-null types when possible

### Example

```kotlin
/**
 * Inserts a new transaction into the ledger.
 *
 * @param userId The ID of the user performing the transaction
 * @param amount The transaction amount
 * @return `true` if insertion was successful, `false` otherwise
 */
fun insertTransaction(userId: UInt, amount: Float): Boolean {
    // Implementation
}
```

---

## 🔄 CI/CD

### GitHub Actions

The project uses GitHub Actions for continuous integration:

```yaml
# .github/workflows/gradle.yml
name: Java CI with Gradle

on:
  push:
    branches: [ master ]
  pull_request:
    branches: [ master ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
        with:
          submodules: 'recursive'
      - name: Set up JDK 21
        uses: actions/setup-java@v4
        with:
          distribution: 'oracle'
          java-version: '21'
      - name: Build with Gradle
        run: ./gradlew assemble
```

### Azure Pipelines

Windows builds are configured via Azure Pipelines for native binary generation.

---

## 🔧 Troubleshooting

### Common Issues

#### 1. Submodule Not Initialized

```bash
# Error: Module not found
git submodule update --init --recursive
```

#### 2. Java Version Mismatch

```bash
# Ensure JDK 21 is installed and set
export JAVA_HOME=/path/to/jdk-21
```

#### 3. Gradle Build Cache Issues

```bash
# Clear Gradle cache
./gradlew clean
rm -rf ~/.gradle/caches
```

#### 4. Network/API Connection Issues

- Verify your `.env` configuration
- Check API server availability
- Ensure proper network connectivity

### Getting Help

- 📧 Open an [Issue](https://github.com/Baneeishaque/Account-Ledger-Library-Kotlin/issues)
- 💬 Start a [Discussion](https://github.com/Baneeishaque/Account-Ledger-Library-Kotlin/discussions)

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🙏 Acknowledgments

- [Kotlin](https://kotlinlang.org/) - The amazing programming language
- [JetBrains](https://www.jetbrains.com/) - For Kotlin and IntelliJ IDEA
- [Ktor](https://ktor.io/) - Asynchronous HTTP client
- [Square](https://square.github.io/) - Retrofit and OkHttp
- All contributors who help improve this project

---

<div align="center">

**Made with ❤️ in Kotlin**

[⬆ Back to Top](#-account-ledger-library---kotlin)

</div>
