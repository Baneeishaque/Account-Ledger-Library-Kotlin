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
        isCreditDebitMode: Boolean = false,
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
            var currentCreditLedger = TransactionLedgerInText(text = "", balance = 0.0F)
            var currentDebitLedger = TransactionLedgerInText(text = "", balance = 0.0F)

            transactions.forEach { currentTransaction: TransactionResponse ->

                if (isCreditDebitMode) {

                    val appendToLedgerWithDebitCreditLedgersResult: Triple<Boolean, Triple<TransactionLedgerInText, TransactionLedgerInText, TransactionLedgerInText>, String?> =
                        appendToLedgerWithDebitCreditLedgers(

                            currentTransaction = currentTransaction,
                            currentAccountId = currentAccountId,
                            currentLedger = currentLedger,
                            currentCreditLedger = currentCreditLedger,
                            currentDebitLedger = currentDebitLedger
                        )
                    if (appendToLedgerWithDebitCreditLedgersResult.first) {

                        currentLedger = appendToLedgerWithDebitCreditLedgersResult.second.first
                        currentCreditLedger = appendToLedgerWithDebitCreditLedgersResult.second.second
                        currentDebitLedger = appendToLedgerWithDebitCreditLedgersResult.second.third

                    } else {

                        // TODO : Handle Date Conversion Error
                        currentLedger.text = appendToLedgerWithDebitCreditLedgersResult.third.toString()
                        return@forEach
                    }

                } else {
                    currentLedger = appendToLedger(

                        currentTransaction = currentTransaction,
                        currentAccountId = currentAccountId,
                        currentLedger = currentLedger
                    )
                }
            }
            return if (isCreditDebitMode) {

                "Credit\n----------------\n${currentCreditLedger.text}===============\n${currentCreditLedger.balance}\n\nDebit\n--------------------\n${currentDebitLedger.text}================\n${currentDebitLedger.balance}\n\nBalance => ${currentCreditLedger.balance} - ${currentDebitLedger.balance} = ${currentCreditLedger.balance - currentDebitLedger.balance}\n"

            } else {

                currentLedger.text
            }
        }
    }

    //TODO : Replace this function with appendToLedgerWithDebitCreditLedgers
    @JvmStatic
    fun appendToLedger(

        currentTransaction: TransactionResponse,
        currentAccountId: UInt,
        currentLedger: TransactionLedgerInText

    ): TransactionLedgerInText {

        var localCurrentBalance: Float = currentLedger.balance
        val transactionDirection: String
        val secondAccountName: String

        if (currentTransaction.fromAccountId == currentAccountId) {

            localCurrentBalance -= currentTransaction.amount
            transactionDirection = "-"
            secondAccountName = currentTransaction.toAccountFullName

        } else {

            localCurrentBalance += currentTransaction.amount
            transactionDirection = "+"
            secondAccountName = currentTransaction.fromAccountFullName
        }

        val toNormalDateTimeConversionResult: IsOkModel<String> =
            MysqlUtils.mySqlDateTimeTextToNormalDateTimeText(mySqlDateTimeText = currentTransaction.eventDateTime)
        if (toNormalDateTimeConversionResult.isOK) {

            return TransactionLedgerInText(

                text = "${currentLedger.text}[${currentTransaction.id}] [${toNormalDateTimeConversionResult.data}]\t[${currentTransaction.particulars}]\t[${transactionDirection}${currentTransaction.amount}]\t[${secondAccountName}]\t[${localCurrentBalance}]\n",
                balance = localCurrentBalance
            )
        }
        //TODO : Throw Error
        return TransactionLedgerInText(

            text = "${currentLedger.text}[${currentTransaction.id}] [${currentTransaction.eventDateTime}]\t[${currentTransaction.particulars}]\t[${transactionDirection}${currentTransaction.amount}]\t[${secondAccountName}]\t[${localCurrentBalance}]\n",
            balance = localCurrentBalance
        )
    }

    @JvmStatic
    fun appendToLedgerWithDebitCreditLedgers(

        currentTransaction: TransactionResponse,
        currentAccountId: UInt,
        currentLedger: TransactionLedgerInText,
        currentCreditLedger: TransactionLedgerInText,
        currentDebitLedger: TransactionLedgerInText

    ): Triple<Boolean, Triple<TransactionLedgerInText, TransactionLedgerInText, TransactionLedgerInText>, String?> {

        val validatedDateResult: IsOkModel<String> =
            MysqlUtils.mySqlDateTimeTextToNormalDateTimeText(mySqlDateTimeText = currentTransaction.eventDateTime)

        if (validatedDateResult.isOK) {

            var localCurrentBalance: Float = currentLedger.balance
            val transactionDirection: String
            val secondAccountName: String

            //TODO : Use flag if balance needed on each rows of credit/debit ledgers
            //TODO : Use flag if secondAccountName needed on each rows of credit/debit ledgers
            //TODO : Use flag if transactionId needed on each rows of credit/debit ledgers
            //TODO : Use flag if eventDateTime needed on each rows of credit/debit ledgers
            //TODO : Use flag if placeholders needed for each item on each rows of credit/debit ledgers
            if (currentTransaction.fromAccountId == currentAccountId) {

                localCurrentBalance -= currentTransaction.amount
                transactionDirection = "-"
                secondAccountName = currentTransaction.toAccountFullName

                currentCreditLedger.balance += currentTransaction.amount
                currentCreditLedger.text += "${currentTransaction.particulars}\t${currentTransaction.amount}\n"

            } else {

                localCurrentBalance += currentTransaction.amount
                transactionDirection = "+"
                secondAccountName = currentTransaction.fromAccountFullName

                currentDebitLedger.balance += currentTransaction.amount
                currentDebitLedger.text += "${currentTransaction.particulars}\t${currentTransaction.amount}\n"
            }

            return Triple(
                true,
                Triple(
                    TransactionLedgerInText(
                        text = "${currentLedger.text}[${currentTransaction.id}] [${validatedDateResult.data}]\t[${currentTransaction.particulars}]\t[${transactionDirection}${currentTransaction.amount}]\t[${secondAccountName}]\t[${localCurrentBalance}]\n",
                        balance = localCurrentBalance
                    ),
                    currentCreditLedger,
                    currentDebitLedger
                ),
                null
            )
        }
        return Triple(false, Triple(currentLedger, currentCreditLedger, currentDebitLedger), validatedDateResult.error)
    }

    private fun appendToTextLedger(

        currentTransaction: TransactionResponse,
        currentTextLedger: String

    ): String {

        return "${currentTextLedger}[${currentTransaction.id}] [${currentTransaction.eventDateTime}]\t[(${currentTransaction.fromAccountFullName}) -> (${currentTransaction.toAccountFullName})]\t[${currentTransaction.particulars}]\t[${currentTransaction.amount}]\n"
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
