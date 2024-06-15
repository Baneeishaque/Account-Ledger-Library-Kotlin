package account.ledger.library.utils

import account.ledger.library.api.response.AccountResponse
import account.ledger.library.enums.BajajDiscountTypeEnum
import account_ledger_library.enums.BajajRewardTypeEnum
import account.ledger.library.enums.TransactionTypeEnum
import account.ledger.library.models.TransactionModel

object TransactionForBajajCoinsUtils {

    @JvmStatic
    val bajajCoinTransactionTypes: List<TransactionTypeEnum> =
        TransactionTypeEnum.entries.filter { transactionTypeEnum: TransactionTypeEnum ->

            transactionTypeEnum in listOf(

                TransactionTypeEnum.BAJAJ_COINS_FLAT,
                TransactionTypeEnum.BAJAJ_COINS_FLAT_WITHOUT_SOURCE,
                TransactionTypeEnum.BAJAJ_COINS_FLAT_WITHOUT_BALANCE_CHECK,

                TransactionTypeEnum.BAJAJ_COINS_UP_TO,
                TransactionTypeEnum.BAJAJ_COINS_UP_TO_WITHOUT_SOURCE,
                TransactionTypeEnum.BAJAJ_COINS_UP_TO_WITHOUT_BALANCE_CHECK
            )
        }

    @JvmStatic
    fun prepareTransactionsForCoins(

        isSourceTransactionPresent: Boolean,
        sourceAccount: AccountResponse,
        secondPartyAccount: AccountResponse,
        rewardIncomeAccount: AccountResponse,
        rewardCollectionAccount: AccountResponse,
        amountToSpendForReward: UInt,
        perTransactionAmountForReward: UInt,
        totalNumberOfTransactionsForRewards: UInt,
        listOfRewardingTransactionIndexes: List<UInt>,
        listOfRewards: List<UInt>,
        eventDateTimeInText: String,
        coinsCollectionAccountBalance: Float,
        coinConversionRate: UInt,
        discountType: BajajDiscountTypeEnum,
        upToValue: UInt?

    ): List<TransactionModel> {

        var localCoinsCollectionAccountBalance: UInt =
            (coinsCollectionAccountBalance * coinConversionRate.toInt()).toUInt()

        return TransactionForBajajUtils.prepareTransactionsBase(

            isSourceTransactionPresent = isSourceTransactionPresent,
            sourceAccount = sourceAccount,
            secondPartyAccount = secondPartyAccount,
            rewardIncomeAccount = rewardIncomeAccount,
            rewardCollectionAccount = rewardCollectionAccount,
            amountToSpendForReward = amountToSpendForReward,
            perTransactionAmountForReward = perTransactionAmountForReward,
            totalNumberOfTransactionsForRewards = totalNumberOfTransactionsForRewards,
            listOfRewardingTransactionIndexes = listOfRewardingTransactionIndexes,
            listOfRewards = listOfRewards,
            eventDateTimeInText = eventDateTimeInText,
            calculateTransactionAmount = fun(rewardedCoins: UInt): Float {

                return (rewardedCoins / coinConversionRate).toFloat()
            },
            generateTransactionParticularsSuffix = fun(rewardedCoins: UInt): String {

                localCoinsCollectionAccountBalance += rewardedCoins
                return ", Actual $rewardedCoins, Conversion Rs. 1 for $coinConversionRate Points, Balance $localCoinsCollectionAccountBalance"
            },
            rewardSpecifier = BajajRewardTypeEnum.COINS,
            discountType = discountType,
            upToValue = upToValue
        )
    }
}
