package account.ledger.library.cli

import account.ledger.library.enums.EnvironmentFileEntryEnum
import common.utils.library.models.EnvironmentFileEntryModel

object EnvironmentalFileEntries {

    private const val accountIdFormalName: String = "Account Index No."
    private const val frequentText: String = "Frequent"

    val walletAccountId = EnvironmentFileEntryModel(
        entryName = EnvironmentFileEntryEnum.WALLET_ACCOUNT_ID,
        entryFormalName = "Wallet $accountIdFormalName"
    )

    val frequent1AccountId = EnvironmentFileEntryModel(
        entryName = EnvironmentFileEntryEnum.FREQUENT_1_ACCOUNT_ID,
        entryFormalName = "$frequentText 1 $accountIdFormalName"
    )

    val frequent2AccountId = EnvironmentFileEntryModel(
        entryName = EnvironmentFileEntryEnum.FREQUENT_2_ACCOUNT_ID,
        entryFormalName = "$frequentText 2 $accountIdFormalName"
    )

    val frequent3AccountId = EnvironmentFileEntryModel(
        entryName = EnvironmentFileEntryEnum.FREQUENT_3_ACCOUNT_ID,
        entryFormalName = "$frequentText 3 $accountIdFormalName"
    )

    val bankAccountId = EnvironmentFileEntryModel(
        entryName = EnvironmentFileEntryEnum.BANK_ACCOUNT_ID,
        entryFormalName = "Bank $accountIdFormalName"
    )

    val isDevelopmentMode = EnvironmentFileEntryModel(entryName = EnvironmentFileEntryEnum.IS_DEVELOPMENT_MODE)
}
