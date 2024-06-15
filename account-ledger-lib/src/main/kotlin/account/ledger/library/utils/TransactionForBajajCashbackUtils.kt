package account.ledger.library.utils

import account.ledger.library.api.response.AccountResponse
import account.ledger.library.enums.BajajDiscountTypeEnum
import account_ledger_library.enums.BajajRewardTypeEnum
import account.ledger.library.enums.TransactionTypeEnum
import account.ledger.library.models.TransactionModel

object TransactionForBajajCashbackUtils {

    @JvmStatic
    val bajajCashbackTransactionTypes: List<TransactionTypeEnum> =
        TransactionTypeEnum.entries.filter { transactionTypeEnum: TransactionTypeEnum ->

            transactionTypeEnum in listOf(

                TransactionTypeEnum.BAJAJ_CASHBACK_FLAT,
                TransactionTypeEnum.BAJAJ_CASHBACK_FLAT_WITHOUT_SOURCE,
                TransactionTypeEnum.BAJAJ_CASHBACK_FLAT_WITHOUT_BALANCE_CHECK,

                TransactionTypeEnum.BAJAJ_CASHBACK_UP_TO,
                TransactionTypeEnum.BAJAJ_CASHBACK_UP_TO_WITHOUT_SOURCE,
                TransactionTypeEnum.BAJAJ_CASHBACK_UP_TO_WITHOUT_BALANCE_CHECK
            )
        }

    @JvmStatic
    fun prepareTransactionsForCashback(

        isSourceTransactionPresent: Boolean,
        sourceAccount: AccountResponse,
        secondPartyAccount: AccountResponse,
        rewardIncomeAccount: AccountResponse,
        rewardCollectionAccount: AccountResponse,
        amountToSpendForReward: UInt,
        perTransactionAmountForReward: UInt,
        totalNumberOfTransactionsForReward: UInt,
        listOfRewardingTransactionIndexes: List<UInt>,
        listOfRewards: List<UInt>,
        eventDateTimeInText: String,
        discountType: BajajDiscountTypeEnum,
        upToValue: UInt?

    ): List<TransactionModel> = TransactionForBajajUtils.prepareTransactionsBase(

        isSourceTransactionPresent = isSourceTransactionPresent,
        sourceAccount = sourceAccount,
        secondPartyAccount = secondPartyAccount,
        rewardIncomeAccount = rewardIncomeAccount,
        rewardCollectionAccount = rewardCollectionAccount,
        amountToSpendForReward = amountToSpendForReward,
        perTransactionAmountForReward = perTransactionAmountForReward,
        totalNumberOfTransactionsForRewards = totalNumberOfTransactionsForReward,
        listOfRewardingTransactionIndexes = listOfRewardingTransactionIndexes,
        listOfRewards = listOfRewards,
        eventDateTimeInText = eventDateTimeInText,
        calculateTransactionAmount = fun(rewardedCashback: UInt): Float {

            return rewardedCashback.toFloat()
        },
        generateTransactionParticularsSuffix = fun(_: UInt): String {

            return ""
        },
        rewardSpecifier = BajajRewardTypeEnum.CASHBACK,
        discountType = discountType,
        upToValue = upToValue
    )
}
