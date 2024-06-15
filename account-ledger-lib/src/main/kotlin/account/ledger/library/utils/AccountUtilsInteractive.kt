package account.ledger.library.utils

import account.ledger.library.api.response.AccountResponse
import account.ledger.library.api.response.MultipleTransactionResponse
import account.ledger.library.api.response.TransactionResponse
import account.ledger.library.operations.ServerOperations
import common.utils.library.models.IsOkModel
import common.utils.library.utils.ApiUtilsCommon
import common.utils.library.utils.IsOkUtils
import common.utils.library.utils.MysqlUtilsInteractive
import java.time.LocalDateTime

object AccountUtilsInteractive {

    @JvmStatic
    fun <T> processUserAccountsMap(

        userId: UInt,
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean,
        successActions: (LinkedHashMap<UInt, AccountResponse>) -> T,
        failureActions: () -> Unit = fun() {}

    ) {
        val getUserAccountsMapResult: IsOkModel<LinkedHashMap<UInt, AccountResponse>> =
            HandleResponsesInteractiveLibrary.getUserAccountsMap(

                apiResponse = ApiUtilsInteractive.getAccountsFull(

                    userId = userId,
                    isConsoleMode = isConsoleMode,
                    isDevelopmentMode = isDevelopmentMode
                )
            )

        IsOkUtils.isOkHandler(

            isOkModel = getUserAccountsMapResult,
            successActions = fun() {

                successActions.invoke(getUserAccountsMapResult.data!!)
            },
            failureActions = failureActions
        )
    }

    @JvmStatic
    fun getAccountBalance(

        userId: UInt,
        desiredAccountId: UInt,
        isDevelopmentMode: Boolean,
        upToDateTime: LocalDateTime? = null

    ): IsOkModel<Float> {

        var currentBalance = 0.0F
        var isSuccess = true
        var error: String? = null

        ApiUtilsCommon.apiResponseHandler(

            apiResponse = ServerOperations.getUserTransactionsForAnAccount(

                userId = userId,
                accountId = desiredAccountId,
                isDevelopmentMode = isDevelopmentMode
            ),
            apiSuccessActions = fun(multipleTransactionResponse: MultipleTransactionResponse) {

                if (ApiUtilsInteractive.isTransactionResponseWithMessage(

                        multipleTransactionResponse = multipleTransactionResponse
                    )
                ) {
                    if (upToDateTime != null) {

                        multipleTransactionResponse.transactions =
                            multipleTransactionResponse.transactions.filter { currentTransaction: TransactionResponse ->

                                val dateConversionResult: IsOkModel<LocalDateTime> =
                                    MysqlUtilsInteractive.mySqlDateTimeTextToDateTimeWithMessage(

                                        mySqlDateTimeText = currentTransaction.eventDateTime
                                    )
                                if (dateConversionResult.isOK) {

                                    dateConversionResult.data!! < upToDateTime

                                } else {

                                    isSuccess = false
                                    error = dateConversionResult.error
                                    false
                                }
                            }
                    }
                    if (isSuccess) {

                        multipleTransactionResponse.transactions.forEach { currentTransaction: TransactionResponse ->

                            if (currentTransaction.fromAccountId == desiredAccountId) {

                                currentBalance -= currentTransaction.amount

                            } else {

                                currentBalance += currentTransaction.amount
                            }
                        }
                    }
                } else {

                    isSuccess = false
                }
            },
            apiFailureActions = fun() {

                isSuccess = false
            }
        )
        return IsOkModel(

            isOK = isSuccess,
            data = currentBalance,
            error = error,
            errorSpecifier = desiredAccountId.toString()
        )
    }
}
