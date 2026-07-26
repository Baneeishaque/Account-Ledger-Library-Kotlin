# 📒 Account Ledger Library - Kotlin

<div align="center">

![Kotlin](https://img.shields.io/badge/Kotlin-2.2.20-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-9.1.0-02303A?style=for-the-badge&logo=gradle&logoColor=white)
![License](https://img.shields.io/github/license/Baneeishaque/Account-Ledger-Library-Kotlin?style=for-the-badge)
![Build Status](https://img.shields.io/github/actions/workflow/status/Baneeishaque/Account-Ledger-Library-Kotlin/gradle.yml?style=for-the-badge)

**A comprehensive, multi-platform Kotlin library for managing financial transactions, accounts, and ledger operations with RESTful API integration.**

[Features](#-features) • [Installation](#-installation) • [Quick Start](#-quick-start) • [Documentation](#-documentation) • [Contributing](#-contributing)

</div>

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Architecture](#-architecture)
- [Installation](#-installation)
- [Quick Start](#-quick-start)
- [API Documentation](#-api-documentation)
- [Usage Examples](#-usage-examples)
- [Development](#-development)
- [Testing](#-testing)
- [Contributing](#-contributing)
- [Project Structure](#-project-structure)
- [Dependencies](#-dependencies)
- [Roadmap](#-roadmap)
- [License](#-license)
- [Acknowledgments](#-acknowledgments)

---

## 🌟 Overview

The **Account Ledger Library for Kotlin** is a powerful, feature-rich library designed for building accounting and financial management applications. It provides a clean, type-safe API for managing users, accounts, transactions, and generating financial reports such as balance sheets and ledgers.

### Key Highlights

- 🎯 **Multi-Platform Support**: JVM, Android, and native platforms (via Kotlin Multiplatform)
- 🔒 **Type-Safe**: Leverages Kotlin's type system for compile-time safety
- 🌐 **REST API Integration**: Built-in HTTP client with Retrofit and Ktor
- 📊 **Financial Reports**: Balance sheets, ledgers, and transaction analysis
- 🔄 **Async Operations**: Coroutines-based asynchronous API calls
- 💾 **Serialization**: JSON serialization with kotlinx.serialization
- 🎨 **Interactive CLI**: Console-mode utilities for interactive operations
- 🧩 **Modular Design**: Clean separation of concerns with multiple modules

---

## ✨ Features

### Core Features

#### 🔐 **User Management**
- User authentication and authorization
- Multi-user support with user selection utilities
- User credential management
- User-specific account and transaction operations

#### 💰 **Account Management**
- Hierarchical account structure (parent-child relationships)
- Account balance tracking and calculations
- Account frequency analysis
- Full account tree navigation
- Account type classification (FROM, TO, VIA)

#### 💸 **Transaction Operations**
- **Insert**: Create new financial transactions
- **Update**: Modify existing transactions
- **Delete**: Remove transactions with proper validation
- **Query**: Advanced transaction filtering and retrieval
- **Special Transaction Types**: Support for complex transaction patterns
  - Normal transactions
  - Two-way transactions
  - Via (intermediary) transactions
  - Cyclic via transactions
  - Special transaction patterns with custom rules
  - Bajaj Coins/Cashback transactions (Flat and Up To variants)

#### 📊 **Financial Reporting**
- **Balance Sheets**: Generate comprehensive balance statements
- **Ledger Sheets**: Detailed account-wise transaction ledgers
- **Transaction History**: Complete audit trail with date/time stamps
- **Account Balances**: Real-time and historical balance calculations
- **Frequency Analysis**: Track most-used accounts and transaction patterns
- **Multiple Output Formats**: Support for various report formats
- **Refine Levels**: Configurable detail levels for reports

#### 🔄 **Advanced Operations**
- Transaction pattern recognition
- Automated transaction categorization
- Date/time range filtering
- Transaction amount validation
- Account exchange operations
- Interactive transaction wizards

### Technical Features

#### 🏗️ **Architecture**
- **Clean Architecture**: Separation of concerns across layers
- **Repository Pattern**: Data access abstraction
- **Retrofit Integration**: Type-safe HTTP client
- **Ktor Client**: Async HTTP operations with coroutines
- **Dependency Injection**: Modular component design

#### 🌐 **API Integration**
- RESTful API client with automatic retry logic
- Configurable server endpoints
- Request/response models with GSON and kotlinx.serialization
- Error handling and recovery mechanisms
- Development and production mode support
- Optional console mode for debugging

#### 🛠️ **Developer Tools**
- Environment file support (dotenv)
- Interactive console utilities
- CSV import/export capabilities
- Text table formatting (j-text-utils)
- Comprehensive logging (Logback)
- CLI argument parsing (kotlinx-cli)

---

## 🏛️ Architecture

### Module Structure

```
Account-Ledger-Library-Kotlin/
├── account-ledger-lib/              # Main JVM library
│   ├── api/                          # REST API definitions
│   │   ├── response/                 # API response models
│   │   ├── Api.kt                    # Retrofit API interface
│   │   └── ApiConstants.kt           # API configuration
│   ├── constants/                    # Application constants
│   ├── enums/                        # Enumeration types
│   ├── models/                       # Data models
│   ├── operations/                   # Business logic operations
│   │   ├── ServerOperations.kt       # API communication
│   │   ├── InsertOperations.kt       # Transaction insertion
│   │   ├── LedgerSheetOperations.kt  # Report generation
│   │   ├── DataOperations.kt         # Data processing
│   │   ├── FileOperations.kt         # File I/O
│   │   └── CheckingOperations.kt     # Validation logic
│   ├── retrofit/                     # Retrofit data sources
│   └── utils/                        # Utility functions
├── account-ledger-lib-multi-platform/ # Kotlin Multiplatform module (submodule)
└── common-lib/                       # Common utilities (submodule)
```

### Data Flow

```
┌─────────────────┐
│   User Input    │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  Utils/Interactive │ ← Validation & User Interaction
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│   Operations    │ ← Business Logic
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  Data Sources   │ ← Retrofit/Ktor API Calls
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│   REST API      │ ← External Server
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│   Response      │
│   Models        │
└─────────────────┘
```

### Technology Stack

| Layer | Technology |
|-------|------------|
| **Language** | Kotlin 2.2.20 |
| **Build Tool** | Gradle 9.1.0 |
| **HTTP Client** | Retrofit 3.0.0, Ktor 3.3.1 |
| **Serialization** | kotlinx.serialization, GSON |
| **Async** | Kotlin Coroutines 1.10.2 |
| **Logging** | Logback 1.5.19 |
| **CLI** | kotlinx-cli 0.3.6 |
| **CSV** | kotlin-csv-jvm 1.10.0 |
| **Environment** | dotenv-kotlin 6.5.1 |
| **Target JVM** | Java 21 |

---

## 📦 Installation

### Prerequisites

- **Java Development Kit (JDK)**: Version 21 or higher
- **Gradle**: 9.1.0+ (or use the included wrapper)
- **Git**: For cloning the repository and submodules

### Option 1: Clone from GitHub

```bash
# Clone the repository
git clone https://github.com/Baneeishaque/Account-Ledger-Library-Kotlin.git
cd Account-Ledger-Library-Kotlin

# Initialize and update submodules
git submodule update --init --recursive
```

### Option 2: Add as Dependency (Future)

_Note: The library is currently under development. Once published, you can add it to your project:_

**Gradle Kotlin DSL** (`build.gradle.kts`):
```kotlin
dependencies {
    implementation("io.github.baneeishaque:account-ledger-lib:1.0.0")
}
```

**Gradle Groovy DSL** (`build.gradle`):
```groovy
dependencies {
    implementation 'io.github.baneeishaque:account-ledger-lib:1.0.0'
}
```

**Maven** (`pom.xml`):
```xml
<dependency>
    <groupId>io.github.baneeishaque</groupId>
    <artifactId>account-ledger-lib</artifactId>
    <version>1.0.0</version>
</dependency>
```

---

## 🚀 Quick Start

### 1. Configure Environment

Create a `.env` file in your project root:

```bash
# API Configuration
SERVER_API_URL=http://localhost/http_API/

# Development Settings
IS_DEVELOPMENT_MODE=true
IS_CONSOLE_MODE=true
```

### 2. Basic Usage Example

```kotlin
import account.ledger.library.api.response.AuthenticationResponse
import account.ledger.library.operations.ServerOperations
import account.ledger.library.operations.InsertOperations
import io.github.cdimascio.dotenv.dotenv

fun main() {
    // Load environment configuration
    val dotEnv = dotenv()
    val isDevelopmentMode = dotEnv["IS_DEVELOPMENT_MODE"].toBoolean()
    val isConsoleMode = dotEnv["IS_CONSOLE_MODE"].toBoolean()
    
    // Example: Get user accounts
    val userId = 1u
    val accountsResult = ServerOperations.getAccounts(
        userId = userId,
        parentAccountId = 0u,
        isConsoleMode = isConsoleMode,
        isDevelopmentMode = isDevelopmentMode
    )
    
    accountsResult.fold(
        onSuccess = { response ->
            println("Accounts retrieved: ${response.accounts.size}")
            response.accounts.forEach { account ->
                println("- ${account.fullName} (ID: ${account.id})")
            }
        },
        onFailure = { error ->
            println("Error: ${error.message}")
        }
    )
    
    // Example: Insert a transaction
    val success = InsertOperations.insertTransaction(
        userId = userId,
        eventDateTime = "2024-12-02 15:30:00",
        particulars = "Office Supplies Purchase",
        amount = 1500.50f,
        fromAccountId = 10u,
        toAccountId = 20u,
        isConsoleMode = isConsoleMode,
        isDevelopmentMode = isDevelopmentMode
    )
    
    if (success) {
        println("Transaction inserted successfully!")
    } else {
        println("Transaction insertion failed.")
    }
}
```

### 3. Interactive Account Selection

```kotlin
import account.ledger.library.utils.AccountUtilsInteractive
import account.ledger.library.api.response.AccountResponse

fun selectAccount() {
    AccountUtilsInteractive.processUserAccountsMap(
        userId = 1u,
        isConsoleMode = true,
        isDevelopmentMode = true,
        successActions = { accountsMap: LinkedHashMap<UInt, AccountResponse> ->
            println("Available Accounts:")
            accountsMap.forEach { (id, account) ->
                println("[$id] ${account.fullName}")
            }
        },
        failureActions = {
            println("Failed to retrieve accounts")
        }
    )
}
```

### 4. Generate Balance Sheet

```kotlin
import account.ledger.library.operations.LedgerSheetOperations
import account.ledger.library.utils.UserUtils
import io.github.cdimascio.dotenv.dotenv

fun generateBalanceSheet() {
    val dotEnv = dotenv()
    val usersMap = // ... obtain users map
    
    LedgerSheetOperations.balanceSheetOfUser(
        usersMap = usersMap,
        isConsoleMode = true,
        isDevelopmentMode = true,
        dotEnv = dotEnv
    )
}
```

---

## 📚 API Documentation

### Core Operations

#### ServerOperations

Operations for communicating with the REST API server.

```kotlin
object ServerOperations {
    // Get user accounts
    fun getAccounts(
        userId: UInt,
        parentAccountId: UInt = 0u,
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean
    ): Result<AccountsResponse>
    
    // Get transactions for an account
    fun getUserTransactionsForAnAccount(
        userId: UInt,
        accountId: UInt,
        isNotFromBalanceSheet: Boolean = true,
        isDevelopmentMode: Boolean
    ): Result<MultipleTransactionResponse>
}
```

#### InsertOperations

Operations for inserting and manipulating transactions.

```kotlin
object InsertOperations {
    // Insert a new transaction
    fun insertTransaction(
        userId: UInt,
        eventDateTime: String,
        particulars: String,
        amount: Float,
        fromAccountId: UInt,
        toAccountId: UInt,
        isConsoleMode: Boolean = false,
        isDevelopmentMode: Boolean,
        eventDateTimeConversionFunction: () -> IsOkModel<String> = { /* default */ },
        transactionManipulationSuccessActions: () -> Unit = {}
    ): Boolean
}
```

#### LedgerSheetOperations

Operations for generating financial reports.

```kotlin
object LedgerSheetOperations {
    // Generate balance sheet for a user
    fun balanceSheetOfUser(
        usersMap: LinkedHashMap<UInt, UserResponse>,
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean,
        dotEnv: Dotenv
    )
    
    // Print sheet with custom operations
    fun printSheetOfUser(
        currentUserName: String,
        currentUserId: UInt,
        getDesiredAccountIdsForSheetOfUser: (MultipleTransactionResponse) -> IsOkModel<MutableMap<UInt, String>>,
        sheetTitle: String,
        isNotApiCall: Boolean = true,
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean,
        operationsAfterPrint: (List<BalanceSheetDataRowModel>) -> Unit = {},
        operationsWithData: (List<BalanceSheetDataRowModel>, Boolean, String, String, (List<BalanceSheetDataRowModel>) -> Unit) -> List<BalanceSheetDataRowModel>
    ): IsOkModel<List<BalanceSheetDataRowModel>>
}
```

### Data Models

#### TransactionResponse

```kotlin
data class TransactionResponse(
    val id: UInt,
    val eventDateTime: String,
    val particulars: String,
    val amount: Float,
    val fromAccountName: String,
    val fromAccountFullName: String,
    val fromAccountId: UInt,
    val toAccountName: String,
    val toAccountFullName: String,
    val toAccountId: UInt
)
```

#### AccountResponse

```kotlin
@Serializable
data class AccountResponse(
    val id: UInt,
    val fullName: String,
    val name: String,
    val parentAccountId: UInt,
    val accountType: String,
    // ... additional fields
)
```

#### TransactionModel

```kotlin
@Serializable
data class TransactionModel(
    val fromAccount: AccountResponse,
    val toAccount: AccountResponse,
    val eventDateTimeInText: String,
    val particulars: String,
    val amount: Float
)
```

### Enumerations

#### TransactionTypeEnum

```kotlin
enum class TransactionTypeEnum(val value: BajajDiscountTypeEnum?) {
    NORMAL,
    TWO_WAY,
    VIA,
    CYCLIC_VIA,
    SPECIAL,
    BAJAJ_COINS_FLAT,
    BAJAJ_CASHBACK_FLAT,
    // ... and more
}
```

#### AccountTypeEnum

```kotlin
enum class AccountTypeEnum {
    TO, FROM, VIA
}
```

---

## 💡 Usage Examples

### Example 1: User Authentication and Account Retrieval

```kotlin
import account.ledger.library.api.response.AccountsResponse
import account.ledger.library.operations.ServerOperations
import kotlinx.coroutines.runBlocking

fun authenticateAndGetAccounts() = runBlocking {
    val userId = 1u
    
    // Fetch accounts for the user
    val accountsResult = ServerOperations.getAccounts(
        userId = userId,
        parentAccountId = 0u,
        isConsoleMode = true,
        isDevelopmentMode = true
    )
    
    accountsResult.onSuccess { accountsResponse ->
        println("Total accounts: ${accountsResponse.accounts.size}")
        accountsResponse.accounts.forEach { account ->
            println("Account: ${account.name} - Balance: [fetch separately]")
        }
    }
}
```

### Example 2: Recording a Transaction

```kotlin
import account.ledger.library.operations.InsertOperations

fun recordExpense() {
    val transactionInserted = InsertOperations.insertTransaction(
        userId = 1u,
        eventDateTime = "2024-12-02 10:00:00",
        particulars = "Grocery Shopping - December",
        amount = 2500.75f,
        fromAccountId = 101u,  // Bank Account
        toAccountId = 201u,     // Groceries Expense
        isConsoleMode = true,
        isDevelopmentMode = true,
        transactionManipulationSuccessActions = {
            println("✓ Transaction recorded successfully!")
        }
    )
    
    if (!transactionInserted) {
        println("✗ Failed to record transaction")
    }
}
```

### Example 3: Calculate Account Balance

```kotlin
import account.ledger.library.utils.AccountUtilsInteractive
import java.time.LocalDateTime

fun getAccountBalance() {
    val userId = 1u
    val accountId = 101u
    
    val balanceResult = AccountUtilsInteractive.getAccountBalance(
        userId = userId,
        desiredAccountId = accountId,
        isDevelopmentMode = true,
        upToDateTime = LocalDateTime.now()  // Optional: balance up to a specific date
    )
    
    if (balanceResult.isOK) {
        println("Account Balance: ${balanceResult.data}")
    } else {
        println("Error: ${balanceResult.error}")
    }
}
```

### Example 4: Generate and Export Balance Sheet

```kotlin
import account.ledger.library.operations.LedgerSheetOperations
import account.ledger.library.models.BalanceSheetDataRowModel
import io.github.cdimascio.dotenv.dotenv

fun exportBalanceSheet() {
    val dotEnv = dotenv()
    val userId = 1u
    val userName = "John Doe"
    
    val sheetResult = LedgerSheetOperations.printSheetOfUser(
        currentUserName = userName,
        currentUserId = userId,
        getDesiredAccountIdsForSheetOfUser = { /* filtering logic */ },
        sheetTitle = "Monthly Balance Sheet - December 2024",
        isConsoleMode = true,
        isDevelopmentMode = true,
        operationsAfterPrint = { dataRows: List<BalanceSheetDataRowModel> ->
            // Export to CSV or other format
            println("Balance Sheet Generated with ${dataRows.size} entries")
            dataRows.forEach { row ->
                println("${row.accountName}: ${row.accountBalance}")
            }
        }
    )
}
```

### Example 5: Working with Special Transaction Types

```kotlin
import account.ledger.library.enums.TransactionTypeEnum
import account.ledger.library.operations.InsertOperations

fun recordSpecialTransaction() {
    // Example: Bajaj Coins cashback transaction
    val success = InsertOperations.insertTransaction(
        userId = 1u,
        eventDateTime = "2024-12-02 14:30:00",
        particulars = "Bajaj EMI Cashback - December",
        amount = 500.0f,
        fromAccountId = 301u,   // Bajaj Account
        toAccountId = 101u,      // Bank Account
        isConsoleMode = true,
        isDevelopmentMode = true
    )
    
    println(if (success) "Cashback recorded" else "Failed to record cashback")
}
```

---

## 🛠️ Development

### Setting Up Development Environment

#### 1. Clone and Setup

```bash
# Clone the repository with submodules
git clone --recursive https://github.com/Baneeishaque/Account-Ledger-Library-Kotlin.git
cd Account-Ledger-Library-Kotlin

# If you already cloned without --recursive
git submodule update --init --recursive
```

#### 2. Install Dependencies

Ensure you have:
- **JDK 21** (Oracle or OpenJDK)
- **Gradle 9.1.0+** (or use `./gradlew`)
- **IntelliJ IDEA** (recommended) or any Kotlin-compatible IDE

#### 3. Configure IDE

**IntelliJ IDEA**:
1. Open the project: `File > Open` → Select the project directory
2. Gradle will automatically import the project
3. Configure JDK: `File > Project Structure > Project` → Set SDK to JDK 21
4. Enable Kotlin compiler: `Settings > Build, Execution, Deployment > Compiler > Kotlin Compiler`

**VS Code**:
1. Install extensions: "Kotlin" and "Gradle for Java"
2. Open project folder
3. Trust workspace and let Gradle sync

#### 4. Environment Configuration

Create `.env` file in project root:

```bash
# Server Configuration
SERVER_API_URL=http://localhost/http_API/

# Development Flags
IS_DEVELOPMENT_MODE=true
IS_CONSOLE_MODE=true

# Optional: MySQL Database (if using local DB)
DB_HOST=localhost
DB_PORT=3306
DB_NAME=account_ledger
DB_USER=root
DB_PASSWORD=password
```

### Building the Project

```bash
# Build all modules
./gradlew build

# Build only the main library
./gradlew :account-ledger-lib:build

# Clean build
./gradlew clean build

# Assemble without tests
./gradlew assemble
```

### Running the Application

```bash
# If there's a main function
./gradlew :account-ledger-lib:run

# With arguments
./gradlew :account-ledger-lib:run --args="arg1 arg2"
```

### Code Style and Linting

```bash
# Check Kotlin code style
./gradlew ktlintCheck

# Auto-format code (if ktlint is configured)
./gradlew ktlintFormat

# Detekt static analysis (if configured)
./gradlew detekt
```

### Dependency Management

```bash
# View dependency tree
./gradlew :account-ledger-lib:dependencies

# Check for dependency updates
./gradlew dependencyUpdates
```

**Renovate Bot** is configured to automatically create PRs for dependency updates.

### Debugging

#### IntelliJ IDEA
1. Set breakpoints in the code
2. Right-click on a test or main function
3. Select "Debug 'ClassName'"

#### CLI Debugging
```bash
# Run with debug logging
./gradlew :account-ledger-lib:run --debug

# Enable Gradle daemon debugging
./gradlew :account-ledger-lib:run --debug-jvm
```

---

## 🧪 Testing

### Running Tests

```bash
# Run all tests
./gradlew test

# Run tests for specific module
./gradlew :account-ledger-lib:test

# Run tests with detailed output
./gradlew test --info

# Run specific test class
./gradlew test --tests "account.ledger.library.OperationsTest"

# Run with coverage report (if configured)
./gradlew test jacocoTestReport
```

### Test Structure

_Note: Test infrastructure is currently being developed._

Planned test structure:
```
account-ledger-lib/
└── src/
    └── test/
        └── kotlin/
            └── account/ledger/library/
                ├── api/           # API tests
                ├── operations/    # Business logic tests
                ├── utils/         # Utility tests
                └── models/        # Model tests
```

### Writing Tests

Example test structure:

```kotlin
import kotlin.test.Test
import kotlin.test.assertEquals

class TransactionOperationsTest {
    
    @Test
    fun `should insert transaction successfully`() {
        // Arrange
        val userId = 1u
        val amount = 100.0f
        
        // Act
        val result = InsertOperations.insertTransaction(
            userId = userId,
            eventDateTime = "2024-12-02 10:00:00",
            particulars = "Test transaction",
            amount = amount,
            fromAccountId = 1u,
            toAccountId = 2u,
            isDevelopmentMode = true
        )
        
        // Assert
        assertEquals(true, result)
    }
}
```

---

## 🤝 Contributing

We welcome contributions from the community! Whether it's bug reports, feature requests, documentation improvements, or code contributions, we appreciate your help.

### How to Contribute

#### 1. Fork the Repository

Click the "Fork" button at the top right of this repository.

#### 2. Clone Your Fork

```bash
git clone https://github.com/YOUR_USERNAME/Account-Ledger-Library-Kotlin.git
cd Account-Ledger-Library-Kotlin
git submodule update --init --recursive
```

#### 3. Create a Feature Branch

```bash
# Create and switch to a new branch
git checkout -b feature/your-feature-name

# Or for bug fixes
git checkout -b fix/bug-description
```

#### 4. Make Your Changes

- Write clean, readable code
- Follow the existing code style
- Add comments for complex logic
- Update documentation if needed
- Add tests for new functionality

#### 5. Test Your Changes

```bash
# Build the project
./gradlew build

# Run tests
./gradlew test

# Check code style (if configured)
./gradlew ktlintCheck
```

#### 6. Commit Your Changes

```bash
git add .
git commit -m "feat: add new feature description"
```

**Commit Message Guidelines**:
- `feat:` New feature
- `fix:` Bug fix
- `docs:` Documentation changes
- `style:` Code style changes (formatting, etc.)
- `refactor:` Code refactoring
- `test:` Adding or updating tests
- `chore:` Maintenance tasks

#### 7. Push to Your Fork

```bash
git push origin feature/your-feature-name
```

#### 8. Create a Pull Request

1. Go to your fork on GitHub
2. Click "Compare & pull request"
3. Fill in the PR template with:
   - Description of changes
   - Related issue numbers (if any)
   - Screenshots (if UI changes)
   - Checklist of completed items

### Contribution Guidelines

#### Code Style

- **Kotlin Conventions**: Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- **Naming**: Use descriptive names for variables, functions, and classes
- **Documentation**: Add KDoc comments for public APIs
- **Formatting**: Use consistent indentation (4 spaces)

#### Pull Request Checklist

- [ ] Code builds without errors
- [ ] All tests pass
- [ ] New tests added for new functionality
- [ ] Documentation updated (README, KDoc)
- [ ] Commit messages follow convention
- [ ] No merge conflicts with main branch
- [ ] Code reviewed and self-checked

#### Areas for Contribution

We especially welcome contributions in:

- 🧪 **Testing**: Writing unit and integration tests
- 📝 **Documentation**: Improving docs, adding examples
- 🐛 **Bug Fixes**: Fixing reported issues
- ✨ **Features**: Implementing new features from the roadmap
- 🌐 **Internationalization**: Adding multi-language support
- 🎨 **UI/UX**: Improving console output and error messages
- 🔒 **Security**: Identifying and fixing security issues
- ⚡ **Performance**: Optimizing slow operations

### Reporting Bugs

**Before submitting a bug report**:
- Check if the bug has already been reported
- Collect information about your environment

**Bug Report Template**:
```markdown
**Description**
A clear description of the bug.

**Steps to Reproduce**
1. Step one
2. Step two
3. ...

**Expected Behavior**
What you expected to happen.

**Actual Behavior**
What actually happened.

**Environment**
- OS: [e.g., Windows 11, macOS 14, Ubuntu 22.04]
- JDK Version: [e.g., OpenJDK 21]
- Library Version: [e.g., 1.0.0]
- Kotlin Version: [e.g., 2.2.20]

**Additional Context**
Stack traces, logs, screenshots, etc.
```

### Feature Requests

We love new ideas! Submit feature requests via GitHub Issues.

**Feature Request Template**:
```markdown
**Feature Description**
Clear description of the feature.

**Use Case**
Why is this feature needed?

**Proposed Solution**
How would you implement it?

**Alternatives Considered**
Other approaches you've thought about.

**Additional Context**
Mockups, examples, etc.
```

### Code of Conduct

- Be respectful and inclusive
- Welcome newcomers and help them get started
- Accept constructive criticism gracefully
- Focus on what's best for the community
- Show empathy towards other community members

---

## 📂 Project Structure

### Detailed Directory Layout

```
Account-Ledger-Library-Kotlin/
│
├── .github/                          # GitHub configuration
│   ├── workflows/
│   │   └── gradle.yml                # CI/CD workflow
│   └── delete-merged-branch-config.yml
│
├── account-ledger-lib/               # Main JVM library (72 source files, ~4,282 LOC)
│   ├── build.gradle.kts              # Build configuration
│   └── src/main/kotlin/account/ledger/library/
│       ├── api/                      # REST API layer
│       │   ├── response/             # API response models
│       │   │   ├── AccountResponse.kt
│       │   │   ├── AccountsResponse.kt
│       │   │   ├── AuthenticationResponse.kt
│       │   │   ├── TransactionResponse.kt
│       │   │   ├── MultipleTransactionResponse.kt
│       │   │   ├── TransactionManipulationResponse.kt
│       │   │   ├── UserResponse.kt
│       │   │   └── MultipleUserResponse.kt
│       │   ├── Api.kt                # Retrofit interface
│       │   ├── ApiConstants.kt       # API endpoint constants
│       │   └── ProjectApiUtils.kt    # API utility functions
│       │
│       ├── constants/                # Application constants
│       │   ├── AccountFrequencyJsonObjectFields.kt
│       │   ├── EnvironmentalFileEntries.kt
│       │   ├── FrequencyOfAccountsJsonObjectFields.kt
│       │   ├── PatternQuestionsJsonArrayMemberFields.kt
│       │   ├── SpecialTransactionTypeJsonObjectFields.kt
│       │   ├── SpecialTransactionTypesJsonObjectFields.kt
│       │   └── UserJsonObjectFields.kt
│       │
│       ├── enums/                    # Enumeration types
│       │   ├── AccountExchangeTypeEnum.kt
│       │   ├── AccountTypeEnum.kt
│       │   ├── AccountsListSortMode.kt
│       │   ├── BajajDiscountTypeEnum.kt
│       │   ├── BalanceSheetOutputFormatsEnum.kt
│       │   ├── BalanceSheetRefineLevelEnum.kt
│       │   ├── EnvironmentFileEntryEnum.kt
│       │   ├── FunctionCallSourceEnum.kt
│       │   ├── HandleAccountsApiResponseResult.kt
│       │   └── TransactionTypeEnum.kt
│       │
│       ├── models/                   # Data models
│       │   ├── AccountFrequencyModel.kt
│       │   ├── BalanceSheetDataRowModel.kt
│       │   ├── ChooseAccountResult.kt
│       │   ├── ChooseSpecialTransactionTypeResultModel.kt
│       │   ├── ChooseTransactionResultModel.kt
│       │   ├── ChooseUserResult.kt
│       │   ├── FrequencyOfAccountsModel.kt
│       │   ├── InsertTransactionResult.kt
│       │   ├── PatternQuestionModel.kt
│       │   ├── SpecialTransactionTypeModel.kt
│       │   ├── SpecialTransactionTypesModel.kt
│       │   ├── TransactionLedgerInText.kt
│       │   ├── TransactionModel.kt
│       │   ├── TransactionModelForLedger.kt
│       │   ├── UserCredentials.kt
│       │   ├── UserModel.kt
│       │   └── ViewTransactionsOutput.kt
│       │
│       ├── operations/               # Business logic operations
│       │   ├── CheckingOperations.kt  # Validation operations
│       │   ├── DataOperations.kt      # Data processing
│       │   ├── FileOperations.kt      # File I/O operations
│       │   ├── InsertOperations.kt    # Transaction insertion
│       │   ├── LedgerSheetOperations.kt  # Report generation
│       │   └── ServerOperations.kt    # API communication
│       │
│       ├── retrofit/                 # Retrofit data layer
│       │   └── data/                 # Data sources
│       │       ├── AccountsDataSource.kt
│       │       ├── MultipleTransactionDataSource.kt
│       │       └── TransactionDataSource.kt
│       │
│       └── utils/                    # Utility functions
│           ├── AccountUtilsInteractive.kt
│           ├── TransactionUtilsInteractive.kt
│           └── ... (other utility classes)
│
├── account-ledger-lib-multi-platform/ # Kotlin Multiplatform module (Git submodule)
│   └── lib/
│       ├── build.gradle.kts          # Multiplatform build config
│       └── src/
│           ├── commonMain/           # Common code
│           ├── jvmMain/              # JVM-specific code
│           ├── androidMain/          # Android-specific code
│           └── nativeMain/           # Native-specific code
│
├── common-lib/                       # Common utilities library (Git submodule)
│   └── common-lib/
│       ├── build.gradle.kts
│       └── src/                      # Common utility functions
│
├── gradle/                           # Gradle wrapper files
│   ├── libs.versions.toml            # Version catalog
│   └── wrapper/
│
├── build.gradle.kts                  # Root build configuration
├── settings.gradle.kts               # Gradle settings
├── gradle.properties                 # Gradle properties
├── gradlew                           # Gradle wrapper (Unix)
├── gradlew.bat                       # Gradle wrapper (Windows)
├── .gitignore                        # Git ignore rules
├── .gitmodules                       # Git submodule configuration
├── renovate.json                     # Renovate bot configuration
├── azure-pipelines-windows.yml       # Azure Pipelines config
└── README.md                         # This file
```

### Key Files Explained

| File | Purpose |
|------|---------|
| `build.gradle.kts` | Root build configuration with plugin declarations |
| `settings.gradle.kts` | Multi-project structure and repository configuration |
| `gradle.properties` | Gradle daemon and compiler settings |
| `libs.versions.toml` | Centralized dependency version management |
| `.gitmodules` | Git submodule references (common-lib, multi-platform lib) |
| `renovate.json` | Automated dependency update configuration |

---

## 📦 Dependencies

### Production Dependencies

#### Core Kotlin Libraries
| Library | Version | Purpose |
|---------|---------|---------|
| `kotlin-bom` | Platform BOM | Kotlin version alignment |
| `kotlinx-cli` | 0.3.6 | Command-line argument parsing |
| `kotlinx-coroutines-core` | 1.10.2 | Asynchronous programming |
| `kotlinx-serialization-json` | 1.9.0 | JSON serialization/deserialization |

#### HTTP & API
| Library | Version | Purpose |
|---------|---------|---------|
| `retrofit2` | 3.0.0 | Type-safe HTTP client |
| `retrofit2:converter-gson` | 3.0.0 | GSON converter for Retrofit |
| `ktor-client-core` | 3.3.1 | Ktor HTTP client core |
| `ktor-client-cio` | 3.3.1 | Coroutine-based I/O engine |
| `ktor-client-logging` | 3.3.1 | HTTP request/response logging |
| `ktor-client-auth` | 3.3.1 | Authentication support |
| `ktor-client-content-negotiation` | 3.3.1 | Content negotiation |
| `ktor-serialization-kotlinx-json` | 3.3.1 | JSON serialization for Ktor |

#### Utilities
| Library | Version | Purpose |
|---------|---------|---------|
| `kotlin-csv-jvm` | 1.10.0 | CSV file parsing and generation |
| `j-text-utils` | 0.3.4 | Text table formatting |
| `dotenv-kotlin` | 6.5.1 | Environment variable management |
| `logback-classic` | 1.5.19 | Logging framework |

#### Project Modules
- `common-lib:common-lib` - Common utilities (submodule)
- `account-ledger-lib-multi-platform:lib` - Multiplatform library (submodule)

### Build Dependencies

| Plugin | Version | Purpose |
|--------|---------|---------|
| `kotlin-jvm` | 2.2.20 | Kotlin JVM plugin |
| `kotlin-multiplatform` | 2.2.20 | Kotlin Multiplatform support |
| `kotlin-serialization` | 2.2.20 | Kotlin serialization plugin |
| `android-library` | 8.13.0 | Android library plugin |

### Development Tools

- **Gradle**: 9.1.0
- **Java**: Target JVM 21
- **Kotlin Language**: 2.2.20
- **API Version**: Kotlin 2.2

### Dependency Management

#### Version Catalog (`gradle/libs.versions.toml`)

```toml
[versions]
kotlin = "2.2.20"
ktor = "3.3.1"
agp = "8.13.0"

[libraries]
kotlinx-cli = { module = "org.jetbrains.kotlinx:kotlinx-cli", version = "0.3.6" }
# ... more libraries

[plugins]
kotlin-jvm = { id = "org.jetbrains.kotlin.jvm", version.ref = "kotlin" }
# ... more plugins
```

#### Updating Dependencies

```bash
# Check for updates
./gradlew dependencyUpdates

# Renovate bot automatically creates PRs for updates
```

---

## 🗺️ Roadmap

### Current Status: v1.0-SNAPSHOT

### Short-term Goals (Q1 2025)

- [ ] **Testing Infrastructure**
  - [ ] Add unit tests for all operations
  - [ ] Integration tests for API calls
  - [ ] Mock server for testing
  - [ ] Code coverage reports (>80%)

- [ ] **Documentation**
  - [x] Comprehensive README
  - [ ] API documentation (KDoc)
  - [ ] User guide with examples
  - [ ] Architecture decision records (ADRs)

- [ ] **Code Quality**
  - [ ] Set up ktlint for code formatting
  - [ ] Configure Detekt for static analysis
  - [ ] Enable all compiler warnings as errors
  - [ ] Implement code review guidelines

### Mid-term Goals (Q2-Q3 2025)

- [ ] **Feature Enhancements**
  - [ ] Advanced transaction filtering
  - [ ] Multi-currency support
  - [ ] Recurring transaction templates
  - [ ] Budget tracking and alerts
  - [ ] Export to PDF/Excel
  - [ ] Import from bank statements

- [ ] **Performance**
  - [ ] Optimize API calls with caching
  - [ ] Lazy loading for large datasets
  - [ ] Database connection pooling
  - [ ] Parallel transaction processing

- [ ] **Platform Support**
  - [ ] Complete Android library
  - [ ] iOS support via Kotlin Multiplatform
  - [ ] Desktop application (Compose Desktop)
  - [ ] Web assembly support

### Long-term Goals (Q4 2025 and beyond)

- [ ] **Enterprise Features**
  - [ ] Multi-tenant support
  - [ ] Role-based access control (RBAC)
  - [ ] Audit logging
  - [ ] Data encryption
  - [ ] Compliance reports (GAAP, IFRS)

- [ ] **Ecosystem**
  - [ ] Plugin system for extensions
  - [ ] REST API server implementation
  - [ ] GraphQL API
  - [ ] Webhook support
  - [ ] OAuth2 authentication

- [ ] **Community**
  - [ ] Public Maven Central release
  - [ ] Example applications
  - [ ] Video tutorials
  - [ ] Community forum
  - [ ] Plugin marketplace

---

## 📄 License

_License information not currently specified in the repository._

**Recommended**: Add a LICENSE file with an appropriate open-source license:
- **MIT License**: Permissive, allows commercial use
- **Apache 2.0**: Permissive with patent grant
- **GPL v3**: Copyleft, requires derivative works to be open source

---

## 🙏 Acknowledgments

### Technologies & Libraries

This project is built on the shoulders of giants. We extend our gratitude to:

- **[JetBrains](https://www.jetbrains.com/)** - For Kotlin, IntelliJ IDEA, and amazing tools
- **[Square](https://square.github.io/)** - For Retrofit and OkHttp
- **[Ktor](https://ktor.io/)** - For the excellent async HTTP client
- **Gradle** - For the powerful build system
- **[Kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization)** - For type-safe serialization

### Contributors

_This section will list all contributors once the project has community contributions._

Want to see your name here? [Start contributing!](#-contributing)

### Special Thanks

- The Kotlin community for continuous support and resources
- Stack Overflow contributors for solutions and guidance
- All early adopters and testers of this library

---

## 📞 Support & Contact

### Getting Help

- **Documentation**: Start with this README and the API docs
- **Issues**: [GitHub Issues](https://github.com/Baneeishaque/Account-Ledger-Library-Kotlin/issues)
- **Discussions**: [GitHub Discussions](https://github.com/Baneeishaque/Account-Ledger-Library-Kotlin/discussions) _(if enabled)_

### Maintainer

**Baneeishaque**
- GitHub: [@Baneeishaque](https://github.com/Baneeishaque)
- Repository: [Account-Ledger-Library-Kotlin](https://github.com/Baneeishaque/Account-Ledger-Library-Kotlin)

### Related Projects

- [Common-Utils-Library-Kotlin-Gradle](https://github.com/Baneeishaque/Common-Utils-Library-Kotlin-Gradle)
- [Account-Ledger-Library-Kotlin-Native](https://github.com/Baneeishaque/Account-Ledger-Library-Kotlin-Native)

---

## 🔖 Version History

### v1.0-SNAPSHOT (Current Development)
- Initial library structure
- Core API integration
- Account and transaction management
- Balance sheet generation
- Multiplatform module integration

---

<div align="center">

**Made with ❤️ using Kotlin**

If you find this project useful, please consider giving it a ⭐ on GitHub!

[⬆ Back to Top](#-account-ledger-library---kotlin)

</div>
