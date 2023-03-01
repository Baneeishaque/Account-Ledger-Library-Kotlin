package account.ledger.library.cli

import account.ledger.library.api.response.AccountsResponse
import account.ledger.library.api.response.TransactionsResponse
import account.ledger.library.retrofit.data.AccountsDataSource
import account.ledger.library.retrofit.data.TransactionsDataSource
import common.utils.library.utils.ApiUtils as CommonApiUtils
import kotlinx.coroutines.runBlocking

internal fun getAccounts(

    userId: UInt,
    parentAccountId: UInt = 0u,
    isConsoleMode: Boolean,
    isDevelopmentMode: Boolean

): Result<AccountsResponse> {

    return CommonApiUtils.getResultFromApiRequestWithOptionalRetries(apiCallFunction = fun(): Result<AccountsResponse> {

        return runBlocking {

            AccountsDataSource().selectUserAccounts(

                userId = userId,
                parentAccountId = parentAccountId
            )
        }
    }, isConsoleMode = isConsoleMode, isDevelopmentMode = isDevelopmentMode)
}

internal fun getUserTransactionsForAnAccount(

    userId: UInt,
    accountId: UInt,
    isNotFromBalanceSheet: Boolean = true,
    isDevelopmentMode: Boolean

): Result<TransactionsResponse> {

    return CommonApiUtils.getResultFromApiRequestWithOptionalRetries(

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
