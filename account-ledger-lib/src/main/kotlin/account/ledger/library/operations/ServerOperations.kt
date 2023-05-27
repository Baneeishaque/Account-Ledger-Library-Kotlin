package account.ledger.library.operations

import account.ledger.library.api.response.AccountsResponse
import account.ledger.library.api.response.TransactionsResponse
import account.ledger.library.retrofit.data.AccountsDataSource
import account.ledger.library.retrofit.data.TransactionsDataSource
import common.utils.library.utils.ApiUtilsCommon
import kotlinx.coroutines.runBlocking

fun getAccounts(

    userId: UInt,
    parentAccountId: UInt = 0u,
    isConsoleMode: Boolean,
    isDevelopmentMode: Boolean

): Result<AccountsResponse> {

    return ApiUtilsCommon.getResultFromApiRequestWithOptionalRetries(apiCallFunction = fun(): Result<AccountsResponse> {

        return runBlocking {

            AccountsDataSource().selectUserAccounts(

                userId = userId,
                parentAccountId = parentAccountId
            )
        }
    }, isConsoleMode = isConsoleMode, isDevelopmentMode = isDevelopmentMode)
}

fun getUserTransactionsForAnAccount(

    userId: UInt,
    accountId: UInt,
    isNotFromBalanceSheet: Boolean = true,
    isDevelopmentMode: Boolean

): Result<TransactionsResponse> {

    return ApiUtilsCommon.getResultFromApiRequestWithOptionalRetries(

        apiCallFunction = fun(): Result<TransactionsResponse> {

            return runBlocking {

                TransactionsDataSource().selectUserTransactions(

                    userId = userId,
                    accountId = accountId
                )
            }
        },
        isConsoleMode = isNotFromBalanceSheet,
        isDevelopmentMode = isDevelopmentMode
    )
}
