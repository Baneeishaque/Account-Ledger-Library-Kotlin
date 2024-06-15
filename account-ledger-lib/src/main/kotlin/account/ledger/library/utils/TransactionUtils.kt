package account.ledger.library.utils

import account.ledger.library.api.response.AccountResponse
import account.ledger.library.api.response.TransactionResponse
import account.ledger.library.models.InsertTransactionResult
import account.ledger.library.models.TransactionLedgerInText
import account.ledger.library.models.TransactionModel
import account.ledger.library.models.TransactionModelForLedger
import common.utils.library.models.IsOkModel
import common.utils.library.utils.DateTimeUtils
import common.utils.library.utils.MysqlUtils
import java.time.LocalDateTime

object TransactionUtils {

    @JvmStatic
    fun prepareUserTransactionsMap(

        transactions: List<TransactionResponse>

    ): LinkedHashMap<UInt, TransactionResponse> {

        val userTransactionsMap = LinkedHashMap<UInt, TransactionResponse>()
        transactions.forEach { currentTransaction: TransactionResponse ->
            userTransactionsMap[currentTransaction.id] = currentTransaction
        }
        return userTransactionsMap
    }

    @JvmStatic
    fun appendToLedgerWithDebitCreditLedgers(

        currentTransactionId: String,
        currentTransactionEventDateTime: String,
        currentTransactionFromAccountFullName: String,
        currentTransactionFromAccountId: UInt,
        currentTransactionToAccountFullName: String,
        currentTransactionParticulars: String,
        currentTransactionAmount: Float,
        currentAccountId: UInt,
        currentLedger: TransactionLedgerInText,
        currentCreditLedger: TransactionLedgerInText,
        currentDebitLedger: TransactionLedgerInText

    ): Triple<Boolean, Triple<TransactionLedgerInText, TransactionLedgerInText, TransactionLedgerInText>, String?> {

        val validatedDateResult: IsOkModel<String> =
            MysqlUtils.mySqlDateTimeTextToNormalDateTimeText(mySqlDateTimeText = currentTransactionEventDateTime)

        if (validatedDateResult.isOK) {

            var localCurrentBalance: Float = currentLedger.balance
            val transactionDirection: String
            val secondAccountName: String

            //TODO : Use flag if balance needed on each rows of credit/debit ledgers
            //TODO : Use flag if secondAccountName needed on each rows of credit/debit ledgers
            //TODO : Use flag if transactionId needed on each rows of credit/debit ledgers
            //TODO : Use flag if eventDateTime needed on each rows of credit/debit ledgers
            //TODO : Use flag if placeholders needed for each item on each rows of credit/debit ledgers
            if (currentTransactionFromAccountId == currentAccountId) {

                localCurrentBalance -= currentTransactionAmount
                transactionDirection = "-"
                secondAccountName = currentTransactionToAccountFullName

                currentCreditLedger.balance += currentTransactionAmount
                currentCreditLedger.text += "${currentTransactionParticulars}\t${currentTransactionAmount}\n"

            } else {

                localCurrentBalance += currentTransactionAmount
                transactionDirection = "+"
                secondAccountName = currentTransactionFromAccountFullName

                currentDebitLedger.balance += currentTransactionAmount
                currentDebitLedger.text += "${currentTransactionParticulars}\t${currentTransactionAmount}\n"
            }

            return Triple(

                first = true,
                second = Triple(

                    first = TransactionLedgerInText(

                        text = "${currentLedger.text}[${currentTransactionId}] [${validatedDateResult.data}]\t[${currentTransactionParticulars}]\t[${transactionDirection}${currentTransactionAmount}]\t[${secondAccountName}]\t[${localCurrentBalance}]\n",
                        balance = localCurrentBalance
                    ),
                    second = currentCreditLedger,
                    third = currentDebitLedger
                ),
                third = null
            )
        }
        return Triple(

            first = false,
            second = Triple(

                first = currentLedger, second = currentCreditLedger, third = currentDebitLedger
            ),
            third = validatedDateResult.error
        )
    }

    @JvmStatic
    fun appendToTextLedger(

        currentTransactionId: String,
        currentTransactionEventDateTime: String,
        currentTransactionFromAccountFullName: String,
        currentTransactionToAccountFullName: String,
        currentTransactionParticulars: String,
        currentTransactionAmount: Float,
        currentTextLedger: String

    ): String =
        "${currentTextLedger}[$currentTransactionId] [$currentTransactionEventDateTime]\t[($currentTransactionFromAccountFullName) -> ($currentTransactionToAccountFullName)]\t[$currentTransactionParticulars]\t[$currentTransactionAmount]\n"

    @JvmStatic
    fun filterTransactionsForUpToDateTime(

        isUpToTimeStamp: Boolean,
        upToTimeStamp: String,
        transactions: List<TransactionResponse>

    ): List<TransactionResponse> = if (isUpToTimeStamp) {

        getTransactionsUpToDateTime(

            upToTimeStamp = upToTimeStamp, transactions = transactions
        )

    } else {

        transactions
    }

    @JvmStatic
    fun getTransactionsUpToDateTime(

        upToTimeStamp: String,
        transactions: List<TransactionResponse>

    ): List<TransactionResponse> {

        val upToTimeStampInDateTime: LocalDateTime =
            DateTimeUtils.normalDateTimeInTextToDateTime(normalDateTimeInText = upToTimeStamp).data!!
        return transactions.filter { transactionResponse: TransactionResponse ->

            MysqlUtils.mySqlDateTimeTextToDateTime(mySqlDateTimeText = transactionResponse.eventDateTime).data!! <= upToTimeStampInDateTime
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

    ): InsertTransactionResult = InsertTransactionResult(

        isSuccess = false,
        dateTimeInText = dateTimeInText,
        transactionParticulars = transactionParticulars,
        transactionAmount = transactionAmount,
        fromAccount = fromAccount,
        viaAccount = viaAccount,
        toAccount = toAccount
    )

    @JvmStatic
    fun transactionsToTextFromList(

        transactions: List<TransactionResponse>

    ): String {

        var result = ""
        transactions.forEachIndexed { index: Int, transaction: TransactionResponse ->

            result += "$index - ${transaction.particulars}\n"
        }
        return result
    }

    fun convertTransactionResponseListToTransactionListForLedger(

        transactions: List<TransactionResponse>

    ): List<TransactionModelForLedger> = transactions.map { transaction: TransactionResponse ->

        TransactionModelForLedger(

            id = transaction.id.toString(),
            eventDateTime = transaction.eventDateTime,
            fromAccountFullName = transaction.fromAccountFullName,
            fromAccountId = transaction.fromAccountId,
            toAccountFullName = transaction.toAccountFullName,
            particulars = transaction.particulars,
            amount = transaction.amount
        )
    }

    fun convertTransactionModelListToToTransactionListForLedger(

        transactions: List<TransactionModel>

    ): List<TransactionModelForLedger> = transactions.map { transaction: TransactionModel ->

        TransactionModelForLedger(

            id = "-",
            eventDateTime = transaction.eventDateTimeInText,
            fromAccountFullName = transaction.fromAccount.fullName,
            fromAccountId = transaction.fromAccount.id,
            toAccountFullName = transaction.toAccount.fullName,
            particulars = transaction.particulars,
            amount = transaction.amount
        )
    }
}
