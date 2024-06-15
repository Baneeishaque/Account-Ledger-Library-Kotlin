package account.ledger.library.utils

import account.ledger.library.models.TransactionLedgerInText
import account.ledger.library.models.TransactionModelForLedger
import common.utils.library.models.IsOkModel
import common.utils.library.utils.ErrorUtilsInteractive

object TransactionUtilsInteractive {

    @JvmStatic
    fun userTransactionsToTextFromListForLedger(

        transactions: List<TransactionModelForLedger>,
        currentAccountId: UInt = 0u,
        isCreditDebitMode: Boolean = false,
        isDevelopmentMode: Boolean

    ): IsOkModel<String> {

        if (isDevelopmentMode) {

            println("transactions = $transactions")
        }
        if (currentAccountId == 0u) {

            var currentTextLedger = ""
            transactions.forEach { currentTransaction: TransactionModelForLedger ->

                currentTextLedger = TransactionUtils.appendToTextLedger(

                    currentTransactionId = currentTransaction.id,
                    currentTransactionEventDateTime = currentTransaction.eventDateTime,
                    currentTransactionFromAccountFullName = currentTransaction.fromAccountFullName,
                    currentTransactionToAccountFullName = currentTransaction.toAccountFullName,
                    currentTransactionParticulars = currentTransaction.particulars,
                    currentTransactionAmount = currentTransaction.amount,
                    currentTextLedger = currentTextLedger
                )
            }
            return IsOkModel(

                isOK = true,
                data = currentTextLedger
            )

        } else {

            var currentLedger = TransactionLedgerInText(text = "", balance = 0.0F)
            var currentCreditLedger = TransactionLedgerInText(text = "", balance = 0.0F)
            var currentDebitLedger = TransactionLedgerInText(text = "", balance = 0.0F)

            transactions.forEach { currentTransaction: TransactionModelForLedger ->

                val appendToLedgerWithDebitCreditLedgersResult: Triple<Boolean, Triple<TransactionLedgerInText, TransactionLedgerInText, TransactionLedgerInText>, String?> =
                    TransactionUtils.appendToLedgerWithDebitCreditLedgers(

                        currentTransactionId = currentTransaction.id,
                        currentTransactionEventDateTime = currentTransaction.eventDateTime,
                        currentTransactionFromAccountFullName = currentTransaction.fromAccountFullName,
                        currentTransactionFromAccountId = currentTransaction.fromAccountId,
                        currentTransactionToAccountFullName = currentTransaction.toAccountFullName,
                        currentTransactionParticulars = currentTransaction.particulars,
                        currentTransactionAmount = currentTransaction.amount,
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

                    return IsOkModel(

                        isOK = false,
                        error = appendToLedgerWithDebitCreditLedgersResult.third.toString()
                    )
                }
            }
            return if (isCreditDebitMode) {

                IsOkModel(

                    isOK = true,
                    data = "Credit\n----------------\n${currentCreditLedger.text}===============\n${currentCreditLedger.balance}\n\nDebit\n--------------------\n${currentDebitLedger.text}================\n${currentDebitLedger.balance}\n\nBalance => ${currentCreditLedger.balance} - ${currentDebitLedger.balance} = ${currentCreditLedger.balance - currentDebitLedger.balance}\n"
                )


            } else {

                IsOkModel(

                    isOK = true,
                    data = currentLedger.text
                )
            }
        }
    }

    @JvmStatic
    fun printUserTransactionsToTextFromListForLedgerError(

        dataSpecification: String = "userTransactionsToTextFromListForLedger",
        userTransactionsToTextFromListForLedgerInstance: IsOkModel<*>
    ) {

        if (userTransactionsToTextFromListForLedgerInstance.isOK) {

            // TODO: Throw Exception

        } else {

            // TODO: Create child class for userTransactionsToTextFromListForLedger
            if (userTransactionsToTextFromListForLedgerInstance.error != null) {

                ErrorUtilsInteractive.printErrorMessage(

                    dataSpecification = dataSpecification,
                    message = userTransactionsToTextFromListForLedgerInstance.error!!
                )
            }
        }
    }
}
