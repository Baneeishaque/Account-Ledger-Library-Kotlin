package account.ledger.library.utils

import account.ledger.library.api.response.AccountResponse
import account.ledger.library.enums.TransactionTypeEnum
import account.ledger.library.models.TransactionModel
import common.utils.library.utils.DateTimeUtils

object TransactionForBajajCoinsUtils {

    @JvmStatic
    val bajajCoinTransactionTypes: List<TransactionTypeEnum> =
        TransactionTypeEnum.entries.filter { transactionTypeEnum: TransactionTypeEnum ->
            transactionTypeEnum in listOf<TransactionTypeEnum>(

                TransactionTypeEnum.BAJAJ_COINS,
                TransactionTypeEnum.BAJAJ_COINS_WITHOUT_SOURCE
            )
        }

    @JvmStatic
    fun prepareTransactions(

        isSourceTransactionPresent: Boolean = true,
        sourceAccount: AccountResponse,
        secondPartyAccount: AccountResponse,
        bajajCoinsIncomeAccount: AccountResponse,
        bajajCoinsCollectionAccount: AccountResponse,
        amountToSpendForBajajCoinRewards: UInt,
        perTransactionAmountForBajajCoins: UInt,
        totalNumberOfTransactionsForBajajCoins: UInt,
        listOfCoinRewardingTransactionIndexes: List<UInt>,
        listOfRewardedCoins: List<UInt>,
        bajajCoinsCollectionAccountBalance: Float,
        eventDateTimeInText: String,
        bajajCoinConversionRate: UInt

    ): List<TransactionModel> {

        val transactions: MutableList<TransactionModel> = mutableListOf()
        var localEventDateTimeInText: String = eventDateTimeInText

        val rewardCoinsSum: UInt = listOfRewardedCoins.sum()
        val listOfCoinRewardingTransactionIndexesInText: String =
            listOfCoinRewardingTransactionIndexes.joinToString(separator = ", ")

        if (isSourceTransactionPresent) {

            transactions.add(
                element = TransactionModel(

                    fromAccount = sourceAccount,
                    toAccount = secondPartyAccount,
                    amount = amountToSpendForBajajCoinRewards.toFloat(),
                    particulars = "To Spend for Bajaj Finserv UPI Transactions to get rewards - Flat $rewardCoinsSum Coins on ${perTransactionAmountForBajajCoins}x$totalNumberOfTransactionsForBajajCoins, Receive on $listOfCoinRewardingTransactionIndexesInText Transactions",
                    eventDateTimeInText = localEventDateTimeInText
                )
            )
        }

        // Prepare the next Y transactions
        var actualBajajCoinsCollectionAccountBalance: UInt =
            (bajajCoinsCollectionAccountBalance * bajajCoinConversionRate.toInt()).toUInt()
        for (i in 0 until totalNumberOfTransactionsForBajajCoins.toInt()) {

            localEventDateTimeInText =
                DateTimeUtils.add5MinutesToNormalDateTimeInText(dateTimeInText = localEventDateTimeInText)

            transactions.add(
                element = TransactionModel(

                    fromAccount = secondPartyAccount,
                    toAccount = sourceAccount,
                    amount = perTransactionAmountForBajajCoins.toFloat(),
                    particulars = "To ${sourceAccount.name} for Bajaj Finserv UPI Reward - Flat $rewardCoinsSum Coins on ${perTransactionAmountForBajajCoins}x$totalNumberOfTransactionsForBajajCoins, Receive on $listOfCoinRewardingTransactionIndexesInText Transactions - ${i + 1}th",
                    eventDateTimeInText = localEventDateTimeInText
                )
            )

            // If the current transaction index is one of the rewarding transactions
            if (listOfCoinRewardingTransactionIndexes.contains((i + 1).toUInt())) {

                localEventDateTimeInText = DateTimeUtils.add5MinutesToNormalDateTimeInText(localEventDateTimeInText)
                val rewardedCoins: UInt =
                    listOfRewardedCoins[listOfCoinRewardingTransactionIndexes.indexOf((i + 1).toUInt())]
                actualBajajCoinsCollectionAccountBalance += rewardedCoins
                val listOfRewardedCoinsInText = listOfRewardedCoins.joinToString(separator = ", ")

                transactions.add(
                    element = TransactionModel(

                        fromAccount = bajajCoinsIncomeAccount,
                        toAccount = bajajCoinsCollectionAccount,
                        amount = (rewardedCoins / bajajCoinConversionRate).toFloat(),
                        particulars = "Bajaj Finserv UPI Reward: ${secondPartyAccount.name} -> ${sourceAccount.name} => ${perTransactionAmountForBajajCoins}x$totalNumberOfTransactionsForBajajCoins times, Flat $rewardCoinsSum Coins as $listOfRewardedCoinsInText on $listOfCoinRewardingTransactionIndexesInText transactions, Actual $rewardedCoins, Conversion Rs. 1 for $bajajCoinConversionRate Points, Balance $actualBajajCoinsCollectionAccountBalance",
                        eventDateTimeInText = localEventDateTimeInText
                    )
                )
            }
        }
        return transactions
    }
}
