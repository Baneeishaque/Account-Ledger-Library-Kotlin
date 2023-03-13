package account.ledger.library.constants

object Constants {

    const val defaultValueForStringEnvironmentVariables: String = "N/A"
    internal const val defaultValueForIntegerEnvironmentVariables: Int = 0
    const val accountText: String = "Account"
    const val userText: String = "User"
    const val frequencyOfAccountsFileName = "frequencyOfAccounts.json"
    const val transactionText: String = "Transaction"
    const val accountHeaderIdentifier: String = "A/C Ledger "
    const val walletAccountHeaderIdentifier: String = "Wallet"

    //    internal const val bankAccountHeaderIdentifier: String = "Bank"
    const val bankAccountHeaderIdentifier: String = "PNB"
    const val accountHeaderUnderlineCharacter: String = "~"
    const val accountBalanceHolderOpeningBrace: String = "{"
    const val dateUnderlineCharacter: Char = '-'
    const val finalBalancePrefixCharacter: String = "="
}
