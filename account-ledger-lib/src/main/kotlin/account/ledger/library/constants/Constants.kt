package account.ledger.library.constants

import common.utils.library.constants.Constants as CommonConstants

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

    const val timeResetCommandIndicator = "Tr"
    val timeResetPatternRegex: Regex = Regex("$timeResetCommandIndicator${CommonConstants.railwayTimeRegexPattern}");

    const val hourIncrementOrDecrementCommandIndicator = "H"
    val hourIncrementOrDecrementPatternRegex: Regex =
        Regex("$hourIncrementOrDecrementCommandIndicator${CommonConstants.digitIncrementOrDecrementRegexPattern}")

    const val minuteIncrementOrDecrementCommandIndicator = "M"
    val minuteIncrementOrDecrementPatternRegex: Regex =
        Regex("$minuteIncrementOrDecrementCommandIndicator${CommonConstants.digitIncrementOrDecrementRegexPattern}")

    const val secondIncrementOrDecrementCommandIndicator = "S"
    val secondIncrementOrDecrementPatternRegex: Regex =
        Regex("$secondIncrementOrDecrementCommandIndicator${CommonConstants.digitIncrementOrDecrementRegexPattern}")

    const val dayIncrementOrDecrementCommandIndicator = "D"
    val dayIncrementOrDecrementPatternRegex: Regex =
        Regex("$dayIncrementOrDecrementCommandIndicator${CommonConstants.digitIncrementOrDecrementRegexPattern}")

    val dayIncrementOrDecrementWithTimeResetPatternRegex =
        Regex("($dayIncrementOrDecrementCommandIndicator${CommonConstants.digitIncrementOrDecrementRegexPattern})($timeResetCommandIndicator${CommonConstants.railwayTimeRegexPattern})")
}
