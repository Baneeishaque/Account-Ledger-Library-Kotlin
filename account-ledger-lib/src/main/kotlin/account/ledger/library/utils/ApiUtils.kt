package account.ledger.library.utils

import account.ledger.library.api.response.AccountsResponse
import account.ledger.library.retrofit.data.AccountsDataSource
import account_ledger_library.constants.Constants
import kotlinx.coroutines.runBlocking
import common.utils.library.utils.ApiUtils as CommonApiUtils

object ApiUtils {

    @JvmStatic
    fun getAccountsFull(
        userId: UInt,
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean
    ): Result<AccountsResponse> {

        //TODO : Change return to AccountsResponse instead of Result<AccountsResponse>
        return CommonApiUtils.getResultFromApiRequestWithOptionalRetries(

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
        noDataBeforeMessageActions: () -> Unit = fun() {}

    ): Boolean {

        return CommonApiUtils.isNoDataResponseWithMessageIncludingBeforeMessageActionsAnd1AsIndicator(

            responseStatus = responseStatus,
            noDataMessageBeforeActions = noDataBeforeMessageActions,
            itemSpecification = Constants.transactionText
        )
    }

    fun isNotNoTransactionResponseWithMessage(

        responseStatus: UInt,
        noDataBeforeMessageActions: () -> Unit = fun() {}

    ): Boolean = !isNoTransactionResponseWithMessage(

        responseStatus = responseStatus,
        noDataBeforeMessageActions = noDataBeforeMessageActions
    )
}
