package account.ledger.library.operations

import account.ledger.library.api.response.AccountResponse
import account.ledger.library.api.response.TransactionManipulationResponse
import account.ledger.library.enums.TransactionTypeEnum
import account.ledger.library.models.AccountFrequencyModel
import account.ledger.library.models.FrequencyOfAccountsModel
import account.ledger.library.models.UserModel
import account.ledger.library.retrofit.data.TransactionDataSource
import common.utils.library.constants.CommonConstants
import common.utils.library.models.IsOkModel
import common.utils.library.utils.ApiUtilsCommon
import common.utils.library.utils.MysqlUtils
import kotlinx.coroutines.runBlocking

object InsertOperations {

    @JvmStatic
    fun insertTransaction(

        userId: UInt,
        eventDateTime: String,
        particulars: String,
        amount: Float,
        fromAccountId: UInt,
        toAccountId: UInt,
        isConsoleMode: Boolean = false,
        isDevelopmentMode: Boolean,
        eventDateTimeConversionFunction: () -> IsOkModel<String> = {

            MysqlUtils.dateTimeTextConversion(

                dateTimeTextConversionFunction = fun(): IsOkModel<String> {

                    return MysqlUtils.normalDateTimeTextToMySqlDateTimeText(

                        normalDateTimeText = eventDateTime,
                    )
                },
            )
        },
        transactionManipulationSuccessActions: () -> Unit = {}

    ): Boolean {

        val eventDateTimeConversionResult: IsOkModel<String> = eventDateTimeConversionFunction.invoke()

        if (eventDateTimeConversionResult.isOK) {

            return manipulateTransaction(

                transactionManipulationApiRequest = fun(): Result<TransactionManipulationResponse> {

                    return runBlocking {

                        TransactionDataSource().insertTransaction(
                            userId = userId,
                            fromAccountId = fromAccountId,
                            eventDateTimeString = eventDateTimeConversionResult.data!!,
                            particulars = particulars,
                            amount = amount,
                            toAccountId = toAccountId
                        )
                    }
                },
                transactionManipulationSuccessActions = transactionManipulationSuccessActions,
                isConsoleMode = isConsoleMode,
                isDevelopmentMode = isDevelopmentMode
            )
        }
        return false
    }

    @JvmStatic
    fun manipulateTransaction(

        transactionManipulationApiRequest: () -> Result<TransactionManipulationResponse>,
        transactionManipulationSuccessActions: () -> Unit = {},
        transactionManipulationFailureActions: (String) -> Unit = {},
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean

    ): Boolean {

        val transactionManipulationApiRequestResult: IsOkModel<TransactionManipulationResponse> =
            ApiUtilsCommon.makeApiRequestWithOptionalRetries(

                apiCallFunction = transactionManipulationApiRequest,
                isConsoleMode = isConsoleMode,
                isDevelopmentMode = isDevelopmentMode
            )

        if (transactionManipulationApiRequestResult.isOK) {

            val transactionManipulationResponseResult: TransactionManipulationResponse =
                transactionManipulationApiRequestResult.data!!
            if (transactionManipulationResponseResult.status == 0u) {

                transactionManipulationSuccessActions.invoke()
                return true

            } else {

                transactionManipulationFailureActions.invoke(transactionManipulationResponseResult.error)
            }
        }
        return false
    }

    @JvmStatic
    fun updateAccountFrequency(

        user: UserModel,
        account: AccountResponse,
        frequencyOfAccounts: FrequencyOfAccountsModel,
        userId: UInt,
        isDevelopmentMode: Boolean

    ): FrequencyOfAccountsModel {

        if (isDevelopmentMode) {

            println("frequencyOfAccounts : $frequencyOfAccounts")
        }

        val accountFrequency: AccountFrequencyModel? =
            user.accountFrequencies.find { accountFrequency: AccountFrequencyModel ->
                accountFrequency.accountID == account.id
            }
        if (isDevelopmentMode) {

            println("accountFrequency : $accountFrequency")
        }
        if (accountFrequency == null) {

            frequencyOfAccounts.users.find { localUser: UserModel -> localUser.id == userId }!!.accountFrequencies =

                frequencyOfAccounts.users.find { localUser: UserModel -> localUser.id == userId }!!.accountFrequencies.plusElement(
                    element = AccountFrequencyModel(
                        accountID = account.id, accountName = account.fullName, countOfRepetition = 1u
                    )
                )

        } else {

            frequencyOfAccounts.users.find { localUser: UserModel -> localUser.id == userId }!!.accountFrequencies.find { localAccountFrequency: AccountFrequencyModel -> localAccountFrequency.accountID == account.id }!!.countOfRepetition++
        }

        if (isDevelopmentMode) {

            println("frequencyOfAccounts : $frequencyOfAccounts")
        }
        return frequencyOfAccounts
    }

    @JvmStatic
    fun getInitialAccountFrequencyForUser(

        userId: UInt,
        fromAccount: AccountResponse,
        toAccount: AccountResponse

    ) = UserModel(

        id = userId,
        accountFrequencies = listOf(

            AccountFrequencyModel(

                accountID = fromAccount.id,
                accountName = fromAccount.fullName,
                countOfRepetition = 1u

            ), AccountFrequencyModel(

                accountID = toAccount.id,
                accountName = toAccount.fullName,
                countOfRepetition = 1u
            )
        )
    )

    @JvmStatic
    fun updateTransaction(

        transactionId: UInt,
        eventDateTime: String,
        particulars: String,
        amount: Float,
        fromAccountId: UInt,
        toAccountId: UInt,
        isDateTimeUpdateOperation: Boolean = false,
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean,
        transactionManipulationSuccessActions: () -> Unit = {},
        transactionManipulationFailureActions: (String) -> Unit = {},
        manipulateTransactionOperation: (() -> Result<TransactionManipulationResponse>, () -> Unit, (String) -> Unit, Boolean, Boolean) -> Boolean = InsertOperations::manipulateTransaction,
        eventDateTimeConversionOperation: () -> IsOkModel<String> = {

            MysqlUtils.dateTimeTextConversion(dateTimeTextConversionFunction = fun(): IsOkModel<String> {

                return MysqlUtils.normalDateTimeTextToMySqlDateTimeText(

                    normalDateTimeText = eventDateTime
                )
            })
        }

    ): Boolean {

        if (isDateTimeUpdateOperation) {

            return manipulateTransactionOperation.invoke(

                fun(): Result<TransactionManipulationResponse> {

                    return runBlocking {

                        TransactionDataSource().updateTransaction(

                            transactionId = transactionId,
                            fromAccountId = fromAccountId,
                            eventDateTimeString = eventDateTime,
                            particulars = particulars,
                            amount = amount,
                            toAccountId = toAccountId
                        )
                    }
                },
                transactionManipulationSuccessActions,
                transactionManipulationFailureActions,
                isConsoleMode,
                isDevelopmentMode
            )
        } else {

            val eventDateTimeConversionResult: IsOkModel<String> = eventDateTimeConversionOperation.invoke()
            if (eventDateTimeConversionResult.isOK) {

                return manipulateTransactionOperation(

                    fun(): Result<TransactionManipulationResponse> {
                        return runBlocking {

                            TransactionDataSource().updateTransaction(
                                transactionId = transactionId,
                                fromAccountId = fromAccountId,
                                eventDateTimeString = eventDateTimeConversionResult.data!!,
                                particulars = particulars,
                                amount = amount,
                                toAccountId = toAccountId
                            )
                        }
                    },
                    transactionManipulationSuccessActions,
                    transactionManipulationFailureActions,
                    isConsoleMode,
                    isDevelopmentMode
                )
            }
        }
        return false
    }

    @JvmStatic
    fun insertTransactionVariants(

        userId: UInt,
        transactionType: TransactionTypeEnum,
        fromAccountId: UInt,
        viaAccountId: UInt,
        toAccountId: UInt,
        isViaStep: Boolean = false,
        isTwoWayStep: Boolean = false,
        transactionId: UInt = 0u,
        dateTimeInText: String,
        transactionParticulars: String,
        transactionAmount: Float,
        isEditStep: Boolean = false,
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean,
        isCyclicViaStep: Boolean

    ): IsOkModel<String> {

        if (isEditStep) {

            when (transactionType) {

                TransactionTypeEnum.NORMAL, TransactionTypeEnum.SPECIAL -> {

                    return IsOkModel(

                        isOK = updateTransaction(

                            transactionId = transactionId,
                            eventDateTime = dateTimeInText,
                            particulars = transactionParticulars,
                            amount = transactionAmount,
                            fromAccountId = fromAccountId,
                            toAccountId = toAccountId,
                            isConsoleMode = isConsoleMode,
                            isDevelopmentMode = isDevelopmentMode
                        )
                    )
                }

                TransactionTypeEnum.VIA -> return IsOkModel(isOK = false, data = CommonConstants.notImplementedMessage)
                TransactionTypeEnum.TWO_WAY -> return IsOkModel(isOK = false, data = CommonConstants.notImplementedMessage)
                TransactionTypeEnum.CYCLIC_VIA -> return IsOkModel(isOK = false, data = CommonConstants.notImplementedMessage)
            }
        } else if (isTwoWayStep) {

            return IsOkModel(

                isOK = insertTransaction(
                    userId = userId,
                    eventDateTime = dateTimeInText,
                    particulars = transactionParticulars,
                    amount = transactionAmount,
                    fromAccountId = toAccountId,
                    toAccountId = fromAccountId,
                    isConsoleMode = isConsoleMode,
                    isDevelopmentMode = isDevelopmentMode
                )
            )
        } else if (isViaStep) {

            return IsOkModel(

                isOK = insertTransaction(

                    userId = userId,
                    eventDateTime = dateTimeInText,
                    particulars = transactionParticulars,
                    amount = transactionAmount,
                    fromAccountId = viaAccountId,
                    toAccountId = toAccountId,
                    isConsoleMode = isConsoleMode,
                    isDevelopmentMode = isDevelopmentMode
                )
            )
        } else if (isCyclicViaStep) {

            return IsOkModel(

                isOK = insertTransaction(

                    userId = userId,
                    eventDateTime = dateTimeInText,
                    particulars = transactionParticulars,
                    amount = transactionAmount,
                    fromAccountId = toAccountId,
                    toAccountId = fromAccountId,
                    isConsoleMode = isConsoleMode,
                    isDevelopmentMode = isDevelopmentMode
                )
            )
        } else {

            when (transactionType) {

                TransactionTypeEnum.NORMAL, TransactionTypeEnum.TWO_WAY, TransactionTypeEnum.SPECIAL -> {

                    return IsOkModel(

                        isOK = insertTransaction(

                            userId = userId,
                            eventDateTime = dateTimeInText,
                            particulars = transactionParticulars,
                            amount = transactionAmount,
                            fromAccountId = fromAccountId,
                            toAccountId = toAccountId,
                            isConsoleMode = isConsoleMode,
                            isDevelopmentMode = isDevelopmentMode
                        )
                    )
                }

                TransactionTypeEnum.VIA, TransactionTypeEnum.CYCLIC_VIA -> {

                    return IsOkModel(

                        isOK = insertTransaction(

                            userId = userId,
                            eventDateTime = dateTimeInText,
                            particulars = transactionParticulars,
                            amount = transactionAmount,
                            fromAccountId = fromAccountId,
                            toAccountId = viaAccountId,
                            isConsoleMode = isConsoleMode,
                            isDevelopmentMode = isDevelopmentMode
                        )
                    )
                }
            }
        }
    }
}
