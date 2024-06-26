package account.ledger.library.utils

import account.ledger.library.api.response.AccountsResponse
import account.ledger.library.api.response.MultipleTransactionResponse
import account.ledger.library.retrofit.data.AccountsDataSource
import account_ledger_library.constants.ConstantsNative
import kotlinx.coroutines.runBlocking
import common.utils.library.utils.ApiUtilsInteractiveCommon

object ApiUtilsInteractive {

    @JvmStatic
    fun getAccountsFull(

        userId: UInt,
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean

    ): Result<AccountsResponse> {

        //TODO : Change return to AccountsResponse instead of Result<AccountsResponse>
        return ApiUtilsInteractiveCommon.getResultFromApiRequestWithOptionalRetries(

            apiCallFunction = fun(): Result<AccountsResponse> {

                return runBlocking {

                    AccountsDataSource().selectUserAccountsFull(userId = userId)
                }
            },
            isConsoleMode = isConsoleMode,
            isDevelopmentMode = isDevelopmentMode
        )
    }

    @JvmStatic
    fun isNoTransactionResponseWithMessage(

        responseStatus: UInt,
        noDataActions: () -> Unit = fun() {}

    ): Boolean {

        return ApiUtilsInteractiveCommon.isNoDataResponseWithMessageAnd1AsIndicator(

            responseStatus = responseStatus,
            noDataActions = noDataActions,
            itemSpecification = ConstantsNative.TRANSACTION_TEXT
        )
    }

    @JvmStatic
    fun isTransactionResponseWithMessage(

        responseStatus: UInt,
        noDataActions: () -> Unit = fun() {}

    ): Boolean = !isNoTransactionResponseWithMessage(

        responseStatus = responseStatus,
        noDataActions = noDataActions
    )

    @JvmStatic
    fun isTransactionResponseWithMessage(

        multipleTransactionResponse: MultipleTransactionResponse,
        noDataActions: () -> Unit = fun() {}

    ): Boolean = isTransactionResponseWithMessage(

        responseStatus = multipleTransactionResponse.status,
        noDataActions = noDataActions
    )
}
