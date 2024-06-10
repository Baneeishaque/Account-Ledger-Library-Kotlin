package account.ledger.library.operations

import account.ledger.library.api.response.AccountResponse
import account.ledger.library.api.response.TransactionResponse
import account.ledger.library.enums.TransactionTypeEnum
import account.ledger.library.models.InsertTransactionResult
import account.ledger.library.models.SpecialTransactionTypeModel
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
        addTransactionOperation: (UInt, String, TransactionTypeEnum, AccountResponse, AccountResponse, AccountResponse, Boolean, Boolean, UInt, String, String, Float, Boolean, UInt, Boolean, Boolean, Boolean, TransactionResponse?, SpecialTransactionTypeModel?) -> InsertTransactionResult,
        chosenTransactionForSpecial: TransactionResponse?,
        chosenSpecialTransactionType: SpecialTransactionTypeModel?

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

                TransactionTypeEnum.NORMAL, TransactionTypeEnum.SPECIAL, TransactionTypeEnum.BAJAJ_COINS, TransactionTypeEnum.BAJAJ_COINS_WITHOUT_SOURCE, TransactionTypeEnum.BAJAJ_SUB_WALLET, TransactionTypeEnum.BAJAJ_SUB_WALLET_WITHOUT_SOURCE -> {

                    val addTransactionResult: InsertTransactionResult = addTransactionAfterAvailabilitySuccess(

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
                        chosenTransactionForSpecial = chosenTransactionForSpecial,
                        chosenSpecialTransactionType = chosenSpecialTransactionType,
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
                        furtherStepIndicator = 1u,
                        chosenTransactionForSpecial = chosenTransactionForSpecial,
                        chosenSpecialTransactionType = chosenSpecialTransactionType
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
                        furtherStepIndicator = 2u,
                        chosenTransactionForSpecial = chosenTransactionForSpecial,
                        chosenSpecialTransactionType = chosenSpecialTransactionType
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
                    furtherStepIndicator = 3u,
                    chosenTransactionForSpecial = chosenTransactionForSpecial,
                    chosenSpecialTransactionType = chosenSpecialTransactionType
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
    // 1: Via.
    // 2: Two Ways
    // 3: Cyclic Via.
    @JvmStatic
    private fun actionWithFurtherActions(

        addTransactionOperation: (UInt, String, TransactionTypeEnum, AccountResponse, AccountResponse, AccountResponse, Boolean, Boolean, UInt, String, String, Float, Boolean, UInt, Boolean, Boolean, Boolean, TransactionResponse?, SpecialTransactionTypeModel?) -> InsertTransactionResult,
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
        furtherStepIndicator: UInt,
        chosenTransactionForSpecial: TransactionResponse?,
        chosenSpecialTransactionType: SpecialTransactionTypeModel?

    ): InsertTransactionResult {

        var addTransactionResult: InsertTransactionResult = addTransactionAfterAvailabilitySuccess(

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
            chosenTransactionForSpecial = chosenTransactionForSpecial,
            chosenSpecialTransactionType = chosenSpecialTransactionType
        )
        if (addTransactionResult.isSuccess) {

            when (furtherStepIndicator) {

                1u, 2u -> {

                    addTransactionResult = addTransactionAfterAvailabilitySuccess(

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
                        isDevelopmentMode = isDevelopmentMode,
                        chosenTransactionForSpecial = chosenTransactionForSpecial,
                        chosenSpecialTransactionType = chosenSpecialTransactionType
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

                    addTransactionResult = addTransactionAfterAvailabilitySuccess(

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
                        isDevelopmentMode = isDevelopmentMode,
                        chosenTransactionForSpecial = chosenTransactionForSpecial,
                        chosenSpecialTransactionType = chosenSpecialTransactionType
                    )
                    if (addTransactionResult.isSuccess) {

                        addTransactionResult = addTransactionAfterAvailabilitySuccess(

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
                            isDevelopmentMode = isDevelopmentMode,
                            chosenTransactionForSpecial = chosenTransactionForSpecial,
                            chosenSpecialTransactionType = chosenSpecialTransactionType
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
    private fun addTransactionAfterAvailabilitySuccess(

        addTransactionOperation: (UInt, String, TransactionTypeEnum, AccountResponse, AccountResponse, AccountResponse, Boolean, Boolean, UInt, String, String, Float, Boolean, UInt, Boolean, Boolean, Boolean, TransactionResponse?, SpecialTransactionTypeModel?) -> InsertTransactionResult,
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
        isCyclicViaStep: Boolean = false,
        chosenTransactionForSpecial: TransactionResponse?,
        chosenSpecialTransactionType: SpecialTransactionTypeModel?

    ): InsertTransactionResult = addTransactionOperation.invoke(

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
        isCyclicViaStep,
        chosenTransactionForSpecial,
        chosenSpecialTransactionType
    )
}
