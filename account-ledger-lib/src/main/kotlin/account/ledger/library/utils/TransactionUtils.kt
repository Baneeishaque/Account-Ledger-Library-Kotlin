package account.ledger.library.utils

import account.ledger.library.api.response.AccountResponse
import account.ledger.library.api.response.TransactionResponse
import account.ledger.library.models.InsertTransactionResult
import account.ledger.library.models.TransactionLedgerInText
import common.utils.library.models.IsOkModel
import common.utils.library.utils.DateTimeUtils
import common.utils.library.utils.MysqlUtils

import java.time.LocalDateTime

object TransactionUtils {

    @JvmStatic
    fun prepareUserTransactionsMap(transactions: List<TransactionResponse>): LinkedHashMap<UInt, TransactionResponse> {

        val userTransactionsMap = LinkedHashMap<UInt, TransactionResponse>()
        transactions.forEach { currentTransaction: TransactionResponse ->
            userTransactionsMap[currentTransaction.id] = currentTransaction
        }
        return userTransactionsMap
    }

    @JvmStatic
    fun userTransactionsToTextFromList(

        transactions: List<TransactionResponse>,
        currentAccountId: UInt,
        isDevelopmentMode: Boolean

    ): String {

        if (isDevelopmentMode) {

            println("transactions = $transactions")
        }
        if (currentAccountId == 0u) {

            var currentTextLedger = ""
            transactions.forEach { currentTransaction: TransactionResponse ->

                currentTextLedger = appendToTextLedger(

                    currentTransaction = currentTransaction,
                    currentTextLedger = currentTextLedger
                )
            }
            return currentTextLedger

        } else {

            var currentLedger = TransactionLedgerInText(text = "", balance = 0.0F)
            transactions.forEach { currentTransaction: TransactionResponse ->

                currentLedger = appendToLedger(

                    currentTransaction = currentTransaction,
                    currentAccountId = currentAccountId,
                    currentLedger = currentLedger
                )
            }
            return currentLedger.text
        }
    }

    @JvmStatic
    fun appendToLedger(

        currentTransaction: TransactionResponse,
        currentAccountId: UInt,
        currentLedger: TransactionLedgerInText

    ): TransactionLedgerInText {

        var localCurrentBalance: Float = currentLedger.balance
        val transactionDirection: String
        val secondAccountName: String

        if (currentTransaction.from_account_id == currentAccountId) {

            localCurrentBalance -= currentTransaction.amount
            transactionDirection = "-"
            secondAccountName = currentTransaction.to_account_full_name

        } else {

            localCurrentBalance += currentTransaction.amount
            transactionDirection = "+"
            secondAccountName = currentTransaction.from_account_full_name
        }

        val toNormalDateTimeConversionResult: IsOkModel<String> =
            MysqlUtils.mySqlDateTimeTextToNormalDateTimeText(mySqlDateTimeText = currentTransaction.event_date_time)
        if (toNormalDateTimeConversionResult.isOK) {

            return TransactionLedgerInText(

                text = "${currentLedger.text}[${currentTransaction.id}] [${toNormalDateTimeConversionResult.data}]\t[${currentTransaction.particulars}]\t[${transactionDirection}${currentTransaction.amount}]\t[${secondAccountName}]\t[${localCurrentBalance}]\n",
                balance = localCurrentBalance
            )
        }
        return TransactionLedgerInText(

            text = "${currentLedger.text}[${currentTransaction.id}] [${currentTransaction.event_date_time}]\t[${currentTransaction.particulars}]\t[${transactionDirection}${currentTransaction.amount}]\t[${secondAccountName}]\t[${localCurrentBalance}]\n",
            balance = localCurrentBalance
        )
    }

    private fun appendToTextLedger(

        currentTransaction: TransactionResponse,
        currentTextLedger: String

    ): String {

        return "${currentTextLedger}[${currentTransaction.id}] [${currentTransaction.event_date_time}]\t[(${currentTransaction.from_account_full_name}) -> (${currentTransaction.to_account_full_name})]\t[${currentTransaction.particulars}]\t[${currentTransaction.amount}]\n"
    }

    @JvmStatic
    fun filterTransactionsForUpToDateTime(
        isUpToTimeStamp: Boolean,
        upToTimeStamp: String,
        transactions: List<TransactionResponse>
    ): List<TransactionResponse> {

        return if (isUpToTimeStamp) {

            getTransactionsUpToDateTime(upToTimeStamp = upToTimeStamp, transactions = transactions)

        } else {

            transactions
        }
    }

    @JvmStatic
    fun getTransactionsUpToDateTime(
        upToTimeStamp: String,
        transactions: List<TransactionResponse>
    ): List<TransactionResponse> {

        val upToTimeStampInDateTime: LocalDateTime =
            DateTimeUtils.normalDateTimeTextToDateTime(normalDateTimeText = upToTimeStamp).data!!
        return transactions.filter { transactionResponse: TransactionResponse ->

            MysqlUtils.mySqlDateTimeTextToDateTime(mySqlDateTimeText = transactionResponse.event_date_time).data!! <= upToTimeStampInDateTime
        }

    }

    @JvmStatic
    fun getFailedInsertTransactionResult(

        dateTimeInText: String,
        transactionParticulars: String,
        transactionAmount: Float,
        fromAccount: AccountResponse,
        viaAccount: AccountResponse,
        toAccount: AccountResponse
        
    ) = InsertTransactionResult(

        isSuccess = false,
        dateTimeInText = dateTimeInText,
        transactionParticulars = transactionParticulars,
        transactionAmount = transactionAmount,
        fromAccount = fromAccount,
        viaAccount = viaAccount,
        toAccount = toAccount
    )
}