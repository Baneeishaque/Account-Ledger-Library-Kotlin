package account.ledger.library.utils

import account.ledger.library.api.response.AccountResponse
import account.ledger.library.enums.TransactionTypeEnum
import account.ledger.library.models.TransactionModel
import common.utils.library.utils.DateTimeUtils

object TransactionForBajajWalletUtils {

    @JvmStatic
    val bajajWalletTransactionTypes: List<TransactionTypeEnum> =
        TransactionTypeEnum.entries.filter { transactionTypeEnum: TransactionTypeEnum ->
            transactionTypeEnum in listOf<TransactionTypeEnum>(

                TransactionTypeEnum.BAJAJ_SUB_WALLET,
                TransactionTypeEnum.BAJAJ_SUB_WALLET_WITHOUT_SOURCE
            )
        }

    @JvmStatic
    fun prepareTransactions(

        isSourceTransactionPresent: Boolean = true,
        sourceAccount: AccountResponse,
        secondPartyAccount: AccountResponse,
        bajajSubWalletIncomeAccount: AccountResponse,
        bajajSubWalletAccount: AccountResponse,
        amountToSpendForBajajSubWalletReward: UInt,
        perTransactionAmountForBajajSubWalletReward: UInt,
        totalNumberOfTransactionsForBajajSubWalletReward: UInt,
        listOfBajajSubWalletRewardingTransactionIndexes: List<UInt>,
        listOfBajajSubWalletRewards: List<UInt>,
        eventDateTimeInText: String

    ): List<TransactionModel> {

        val transactions: MutableList<TransactionModel> = mutableListOf()
        var localEventDateTimeInText: String = eventDateTimeInText

        val bajajSubWalletRewardsSum: UInt = listOfBajajSubWalletRewards.sum()
        val listOfBajajSubWalletRewardingTransactionIndexesInText: String =
            listOfBajajSubWalletRewardingTransactionIndexes.joinToString(separator = ", ")

        val bajajSubWalletRewardDescription =
            "Flat $bajajSubWalletRewardsSum on ${perTransactionAmountForBajajSubWalletReward}x$totalNumberOfTransactionsForBajajSubWalletReward, Receive on $listOfBajajSubWalletRewardingTransactionIndexesInText Transactions"

        if (isSourceTransactionPresent) {

            transactions.add(
                element = TransactionModel(

                    fromAccount = secondPartyAccount,
                    toAccount = sourceAccount,
                    amount = amountToSpendForBajajSubWalletReward.toFloat(),
                    particulars = "To Spend for Bajaj Finserv UPI Transactions to get reward - $bajajSubWalletRewardDescription",
                    eventDateTimeInText = localEventDateTimeInText
                )
            )
        }

        // Prepare the next Y transactions
        for (i: Int in 0 until totalNumberOfTransactionsForBajajSubWalletReward.toInt()) {

            localEventDateTimeInText =
                DateTimeUtils.add5MinutesToNormalDateTimeInText(dateTimeInText = localEventDateTimeInText)

            transactions.add(
                element = TransactionModel(

                    fromAccount = sourceAccount,
                    toAccount = secondPartyAccount,
                    amount = perTransactionAmountForBajajSubWalletReward.toFloat(),
                    particulars = "To ${secondPartyAccount.name} for Bajaj Finserv UPI Reward - $bajajSubWalletRewardDescription - ${i + 1}th",
                    eventDateTimeInText = localEventDateTimeInText
                )
            )

            // Check the current transaction index is one of the rewarding transactions.
            if (listOfBajajSubWalletRewardingTransactionIndexes.contains((i + 1).toUInt())) {

                localEventDateTimeInText = DateTimeUtils.add5MinutesToNormalDateTimeInText(localEventDateTimeInText)
                val bajajSubWalletReward: UInt =
                    listOfBajajSubWalletRewards[listOfBajajSubWalletRewardingTransactionIndexes.indexOf((i + 1).toUInt())]
                val listOfBajajSubWalletRewardsInText: String =
                    listOfBajajSubWalletRewards.joinToString(separator = ", ")

                transactions.add(
                    element = TransactionModel(

                        fromAccount = bajajSubWalletIncomeAccount,
                        toAccount = bajajSubWalletAccount,
                        amount = bajajSubWalletReward.toFloat(),
                        particulars = "Bajaj Finserv UPI Reward: ${sourceAccount.name} -> ${secondPartyAccount.name} => ${perTransactionAmountForBajajSubWalletReward}x$totalNumberOfTransactionsForBajajSubWalletReward times, Flat $bajajSubWalletRewardsSum as $listOfBajajSubWalletRewardsInText on $listOfBajajSubWalletRewardingTransactionIndexesInText transactions",
                        eventDateTimeInText = localEventDateTimeInText
                    )
                )
            }
        }
        return transactions
    }
}
