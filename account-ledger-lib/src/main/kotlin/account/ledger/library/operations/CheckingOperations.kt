package account.ledger.library.operations

import account.ledger.library.api.response.AccountResponse
import account.ledger.library.enums.TransactionTypeEnum
import account.ledger.library.models.InsertTransactionResult
import account.ledger.library.utils.TransactionUtils
import common.utils.library.utils.DateTimeUtils

object CheckingOperations {

    /*
    Receives fromAccount, viaAccount, toAccount & Checks them.
    Returns 0 if all are available.
        1 if fromAccount is unavailable & toAccount available.
        2 if toAccount available.
        3 if transactionType is Via, viaAccount unavailable & toAccount & fromAccounts are there.
      It also executes actions on missing desired account.
    */
    @JvmStatic
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

        } else if (((transactionType == TransactionTypeEnum.VIA) || (transactionType == TransactionTypeEnum.CYCLIC_VIA)) && (viaAccount.id == 0u)) {

            viaAccountMissingActions.invoke()
            return 3
        }
        return 0
    }

    @JvmStatic
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
        addTransactionOperation: (UInt, String, TransactionTypeEnum, AccountResponse, AccountResponse, AccountResponse, Boolean, Boolean, UInt, String, String, Float, Boolean, UInt, Boolean, Boolean, Boolean) -> InsertTransactionResult

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

                    val addTransactionResult: InsertTransactionResult = addTransaction(

                        addTransactionOperation = addTransactionOperation,
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

                    if (addTransactionResult.isSuccess) {

                        return InsertTransactionResult(

                            isSuccess = true,
                            dateTimeInText = DateTimeUtils.add5MinutesToNormalDateTimeInText(dateTimeInText = addTransactionResult.dateTimeInText),
                            transactionParticulars = addTransactionResult.transactionParticulars,
                            transactionAmount = addTransactionResult.transactionAmount,
                            fromAccount = fromAccount,
                            viaAccount = viaAccount,
                            toAccount = toAccount
                        )
                    }
                }

                TransactionTypeEnum.VIA -> {

                    return actionWithFurtherActions(

                        addTransactionOperation = addTransactionOperation,
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
                        isDevelopmentMode = isDevelopmentMode,
                        furtherStepIndicator = 1u
                    )
                }

                TransactionTypeEnum.TWO_WAY -> {

                    return actionWithFurtherActions(

                        addTransactionOperation = addTransactionOperation,
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
                        isDevelopmentMode = isDevelopmentMode,
                        furtherStepIndicator = 2u
                    )
                }

                TransactionTypeEnum.CYCLIC_VIA -> return actionWithFurtherActions(

                    addTransactionOperation = addTransactionOperation,
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
                    isDevelopmentMode = isDevelopmentMode,
                    furtherStepIndicator = 3u
                )
            }
        }
        return TransactionUtils.getFailedInsertTransactionResult(

            dateTimeInText = dateTimeInText,
            transactionParticulars = transactionParticulars,
            transactionAmount = transactionAmount,
            fromAccount = fromAccount,
            viaAccount = viaAccount,
            toAccount = toAccount
        )
    }

    // Further Step Indicators
    // --------------------------
    // 1 : Via.
    // 2 : Two Way
    // 3 : Cyclic Via.
    @JvmStatic
    private fun actionWithFurtherActions(

        addTransactionOperation: (UInt, String, TransactionTypeEnum, AccountResponse, AccountResponse, AccountResponse, Boolean, Boolean, UInt, String, String, Float, Boolean, UInt, Boolean, Boolean, Boolean) -> InsertTransactionResult,
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
        furtherStepIndicator: UInt

    ): InsertTransactionResult {

        var addTransactionResult: InsertTransactionResult = addTransaction(

            addTransactionOperation = addTransactionOperation,
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
        if (addTransactionResult.isSuccess) {

            when (furtherStepIndicator) {

                1u, 2u -> {

                    addTransactionResult = addTransaction(

                        addTransactionOperation = addTransactionOperation,
                        userId = userId,
                        username = username,
                        transactionType = transactionType,
                        fromAccount = fromAccount,
                        viaAccount = viaAccount,
                        toAccount = toAccount,
                        isViaStep = furtherStepIndicator == 1u,
                        isTwoWayStep = furtherStepIndicator == 2u,
                        dateTimeInText = DateTimeUtils.add5MinutesToNormalDateTimeInText(addTransactionResult.dateTimeInText),
                        transactionParticulars = addTransactionResult.transactionParticulars,
                        transactionAmount = addTransactionResult.transactionAmount,
                        isConsoleMode = isConsoleMode,
                        isDevelopmentMode = isDevelopmentMode
                    )
                    if (addTransactionResult.isSuccess) {

                        return InsertTransactionResult(

                            isSuccess = true,
                            dateTimeInText = DateTimeUtils.add5MinutesToNormalDateTimeInText(dateTimeInText = addTransactionResult.dateTimeInText),
                            transactionParticulars = addTransactionResult.transactionParticulars,
                            transactionAmount = addTransactionResult.transactionAmount,
                            fromAccount = fromAccount,
                            viaAccount = viaAccount,
                            toAccount = toAccount
                        )
                    }
                }

                3u -> {

                    addTransactionResult = addTransaction(

                        addTransactionOperation = addTransactionOperation,
                        userId = userId,
                        username = username,
                        transactionType = transactionType,
                        fromAccount = fromAccount,
                        viaAccount = viaAccount,
                        toAccount = toAccount,
                        isViaStep = true,
                        dateTimeInText = DateTimeUtils.add5MinutesToNormalDateTimeInText(addTransactionResult.dateTimeInText),
                        transactionParticulars = addTransactionResult.transactionParticulars,
                        transactionAmount = addTransactionResult.transactionAmount,
                        isConsoleMode = isConsoleMode,
                        isDevelopmentMode = isDevelopmentMode
                    )
                    if (addTransactionResult.isSuccess) {

                        addTransactionResult = addTransaction(

                            addTransactionOperation = addTransactionOperation,
                            userId = userId,
                            username = username,
                            transactionType = transactionType,
                            fromAccount = fromAccount,
                            viaAccount = viaAccount,
                            toAccount = toAccount,
                            isCyclicViaStep = true,
                            dateTimeInText = DateTimeUtils.add5MinutesToNormalDateTimeInText(addTransactionResult.dateTimeInText),
                            transactionParticulars = addTransactionResult.transactionParticulars,
                            transactionAmount = addTransactionResult.transactionAmount,
                            isConsoleMode = isConsoleMode,
                            isDevelopmentMode = isDevelopmentMode
                        )

                        if (addTransactionResult.isSuccess) {

                            return InsertTransactionResult(

                                isSuccess = true,
                                dateTimeInText = DateTimeUtils.add5MinutesToNormalDateTimeInText(dateTimeInText = addTransactionResult.dateTimeInText),
                                transactionParticulars = addTransactionResult.transactionParticulars,
                                transactionAmount = addTransactionResult.transactionAmount,
                                fromAccount = fromAccount,
                                viaAccount = viaAccount,
                                toAccount = toAccount
                            )
                        }
                    }
                }
            }
        }
        return TransactionUtils.getFailedInsertTransactionResult(

            dateTimeInText = dateTimeInText,
            transactionParticulars = transactionParticulars,
            transactionAmount = transactionAmount,
            fromAccount = fromAccount,
            viaAccount = viaAccount,
            toAccount = toAccount
        )
    }

    @JvmStatic
    private fun addTransaction(

        addTransactionOperation: (UInt, String, TransactionTypeEnum, AccountResponse, AccountResponse, AccountResponse, Boolean, Boolean, UInt, String, String, Float, Boolean, UInt, Boolean, Boolean, Boolean) -> InsertTransactionResult,
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
        isDevelopmentMode: Boolean,
        isCyclicViaStep: Boolean = false

    ) = addTransactionOperation.invoke(

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
        isDevelopmentMode,
        isCyclicViaStep
    )
}
