package account.ledger.library.operations

import account.ledger.library.api.response.AccountsResponse
import account.ledger.library.api.response.MultipleTransactionResponse
import account.ledger.library.retrofit.data.AccountsDataSource
import account.ledger.library.retrofit.data.MultipleTransactionDataSource
import common.utils.library.utils.ApiUtilsCommon
import common.utils.library.utils.ApiUtilsInteractiveCommon
import kotlinx.coroutines.runBlocking

object ServerOperations {

    @JvmStatic
    fun getAccounts(

        userId: UInt,
        parentAccountId: UInt = 0u,
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean

    ): Result<AccountsResponse> {

        return ApiUtilsInteractiveCommon.getResultFromApiRequestWithOptionalRetries(apiCallFunction = fun(): Result<AccountsResponse> {

            return runBlocking {

                AccountsDataSource().selectUserAccounts(

                    userId = userId,
                    parentAccountId = parentAccountId
                )
            }
        }, isConsoleMode = isConsoleMode, isDevelopmentMode = isDevelopmentMode)
    }

    @JvmStatic
    fun getUserTransactionsForAnAccount(

        userId: UInt,
        accountId: UInt,
        isNotFromBalanceSheet: Boolean = true,
        isDevelopmentMode: Boolean

    ): Result<MultipleTransactionResponse> {

        return ApiUtilsInteractiveCommon.getResultFromApiRequestWithOptionalRetries(

            apiCallFunction = fun(): Result<MultipleTransactionResponse> {

                return runBlocking {

                    MultipleTransactionDataSource().selectUserTransactions(

                        userId = userId,
                        accountId = accountId
                    )
                }
            },
            isConsoleMode = isNotFromBalanceSheet,
            isDevelopmentMode = isDevelopmentMode
        )
    }
}
