package account.ledger.library.operations

import account.ledger.library.api.response.AccountResponse
import account.ledger.library.enums.TransactionTypeEnum
import account.ledger.library.models.InsertTransactionResult
import common.utils.library.utils.DateTimeUtils

/*
Receives fromAccount, viaAccount, toAccount & Checks them.
Returns 0 if all are available.
    1 if fromAccount unavailable & toAccount available.
    2 if toAccount available.
    3 if transactionType is Via, viaAccount unavailable & toAccount & fromAccounts are there.
  It also executes actions on missing desired account.
*/
fun isAccountsAreAvailable(

    transactionType: TransactionTypeEnum,
    fromAccount: AccountResponse,
    viaAccount: AccountResponse,
    toAccount: AccountResponse,
    fromAccountMissingActions: () -> Unit = {},
    toAccountMissingActions: () -> Unit = {},
    viaAccountMissingActions: () -> Unit = {}

): Int {

    if (toAccount.id == 0u) {

        fromAccountMissingActions.invoke()
        return 1

    } else if (fromAccount.id == 0u) {

        toAccountMissingActions.invoke()
        return 2

    } else if ((transactionType == TransactionTypeEnum.VIA) && (viaAccount.id == 0u)) {

        viaAccountMissingActions.invoke()
        return 3
    }
    return 0
}

fun addTransactionWithAccountAvailabilityCheck(

    userId: UInt,
    username: String,
    transactionType: TransactionTypeEnum,
    fromAccount: AccountResponse,
    viaAccount: AccountResponse,
    toAccount: AccountResponse,
    dateTimeInText: String,
    transactionParticulars: String,
    transactionAmount: Float,
    isConsoleMode: Boolean,
    isDevelopmentMode: Boolean,
    fromAccountMissingActions: () -> Unit = {},
    toAccountMissingActions: () -> Unit = {},
    viaAccountMissingActions: () -> Unit = {},
    addTransactionStep2Operation: (UInt, String, TransactionTypeEnum, AccountResponse, AccountResponse, AccountResponse, Boolean, Boolean, UInt, String, String, Float, Boolean, UInt, Boolean, Boolean) -> InsertTransactionResult

): InsertTransactionResult {

    if (isAccountsAreAvailable(

            transactionType = transactionType,
            fromAccount = fromAccount,
            viaAccount = viaAccount,
            toAccount = toAccount,
            fromAccountMissingActions = fromAccountMissingActions,
            toAccountMissingActions = toAccountMissingActions,
            viaAccountMissingActions = viaAccountMissingActions

        ) == 0
    ) {
        when (transactionType) {

            TransactionTypeEnum.NORMAL -> {

                val addTransactionStep2Result: InsertTransactionResult = addTransaction(

                    addTransactionStep2Operation = addTransactionStep2Operation,
                    userId = userId,
                    username = username,
                    transactionType = transactionType,
                    fromAccount = fromAccount,
                    viaAccount = viaAccount,
                    toAccount = toAccount,
                    dateTimeInText = dateTimeInText,
                    transactionParticulars = transactionParticulars,
                    transactionAmount = transactionAmount,
                    isConsoleMode = isConsoleMode,
                    isDevelopmentMode = isDevelopmentMode
                )

                if (addTransactionStep2Result.isSuccess) {

                    return InsertTransactionResult(

                        isSuccess = true,
                        dateTimeInText = DateTimeUtils.add5MinutesToDateTimeInText(dateTimeInText = addTransactionStep2Result.dateTimeInText),
                        transactionParticulars = addTransactionStep2Result.transactionParticulars,
                        transactionAmount = addTransactionStep2Result.transactionAmount,
                        fromAccount = fromAccount,
                        viaAccount = viaAccount,
                        toAccount = toAccount
                    )
                }
            }

            TransactionTypeEnum.VIA -> {

                var addTransactionStep2Result: InsertTransactionResult = addTransaction(

                    addTransactionStep2Operation = addTransactionStep2Operation,
                    userId = userId,
                    username = username,
                    transactionType = transactionType,
                    fromAccount = fromAccount,
                    viaAccount = viaAccount,
                    toAccount = toAccount,
                    dateTimeInText = dateTimeInText,
                    transactionParticulars = transactionParticulars,
                    transactionAmount = transactionAmount,
                    isConsoleMode = isConsoleMode,
                    isDevelopmentMode = isDevelopmentMode
                )
                if (addTransactionStep2Result.isSuccess) {

                    addTransactionStep2Result = addTransaction(

                        addTransactionStep2Operation = addTransactionStep2Operation,
                        userId = userId,
                        username = username,
                        transactionType = transactionType,
                        fromAccount = fromAccount,
                        viaAccount = viaAccount,
                        toAccount = toAccount,
                        isViaStep = true,
                        dateTimeInText = dateTimeInText,
                        transactionParticulars = transactionParticulars,
                        transactionAmount = transactionAmount,
                        isConsoleMode = isConsoleMode,
                        isDevelopmentMode = isDevelopmentMode
                    )
                    if (addTransactionStep2Result.isSuccess
                    ) {
                        return InsertTransactionResult(

                            isSuccess = true,
                            dateTimeInText = DateTimeUtils.add5MinutesToDateTimeInText(dateTimeInText = addTransactionStep2Result.dateTimeInText),
                            transactionParticulars = addTransactionStep2Result.transactionParticulars,
                            transactionAmount = addTransactionStep2Result.transactionAmount,
                            fromAccount = fromAccount,
                            viaAccount = viaAccount,
                            toAccount = toAccount
                        )
                    }
                }
            }

            TransactionTypeEnum.TWO_WAY -> {

                var addTransactionStep2Result: InsertTransactionResult = addTransaction(

                    addTransactionStep2Operation = addTransactionStep2Operation,
                    userId = userId,
                    username = username,
                    transactionType = transactionType,
                    fromAccount = fromAccount,
                    viaAccount = viaAccount,
                    toAccount = toAccount,
                    dateTimeInText = dateTimeInText,
                    transactionParticulars = transactionParticulars,
                    transactionAmount = transactionAmount,
                    isConsoleMode = isConsoleMode,
                    isDevelopmentMode = isDevelopmentMode
                )
                if (addTransactionStep2Result.isSuccess) {

                    addTransactionStep2Result = addTransaction(

                        addTransactionStep2Operation = addTransactionStep2Operation,
                        userId = userId,
                        username = username,
                        transactionType = transactionType,
                        fromAccount = fromAccount,
                        viaAccount = viaAccount,
                        toAccount = toAccount,
                        isTwoWayStep = true,
                        dateTimeInText = dateTimeInText,
                        transactionParticulars = transactionParticulars,
                        transactionAmount = transactionAmount,
                        isConsoleMode = isConsoleMode,
                        isDevelopmentMode = isDevelopmentMode
                    )
                    if (addTransactionStep2Result.isSuccess) {

                        return InsertTransactionResult(

                            isSuccess = true,
                            dateTimeInText = DateTimeUtils.add5MinutesToDateTimeInText(dateTimeInText = addTransactionStep2Result.dateTimeInText),
                            transactionParticulars = addTransactionStep2Result.transactionParticulars,
                            transactionAmount = addTransactionStep2Result.transactionAmount,
                            fromAccount = fromAccount,
                            viaAccount = viaAccount,
                            toAccount = toAccount
                        )
                    }
                }
            }
        }
    }
    return InsertTransactionResult(

        isSuccess = false,
        dateTimeInText = dateTimeInText,
        transactionParticulars = transactionParticulars,
        transactionAmount = transactionAmount,
        fromAccount = fromAccount,
        viaAccount = viaAccount,
        toAccount = toAccount
    )
}

private fun addTransaction(

    addTransactionStep2Operation: (UInt, String, TransactionTypeEnum, AccountResponse, AccountResponse, AccountResponse, Boolean, Boolean, UInt, String, String, Float, Boolean, UInt, Boolean, Boolean) -> InsertTransactionResult,
    userId: UInt,
    username: String,
    transactionType: TransactionTypeEnum,
    fromAccount: AccountResponse,
    viaAccount: AccountResponse,
    toAccount: AccountResponse,
    isViaStep: Boolean = false,
    isTwoWayStep: Boolean = false,
    dateTimeInText: String,
    transactionParticulars: String,
    transactionAmount: Float,
    isConsoleMode: Boolean,
    isDevelopmentMode: Boolean

) = addTransactionStep2Operation.invoke(

    userId,
    username,
    transactionType,
    fromAccount,
    viaAccount,
    toAccount,
    isViaStep,
    isTwoWayStep,
    0u,
    dateTimeInText,
    transactionParticulars,
    transactionAmount,
    false,
    0u,
    isConsoleMode,
    isDevelopmentMode
)
