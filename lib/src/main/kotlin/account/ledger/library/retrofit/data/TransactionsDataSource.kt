package accountLedgerCli.retrofit.data

import account.ledger.library.api.response.TransactionsResponse

internal class TransactionsDataSource : AppDataSource<TransactionsResponse>() {

    internal suspend fun selectUserTransactions(

        userId: UInt,
        accountId: UInt

    ): Result<TransactionsResponse> {

        return handleApiResponse(

            apiResponse = retrofitClient.selectUserTransactionsV2M(

                userId = userId,
                accountId = accountId
            )
        )
    }

    internal suspend fun selectTransactions(

        userId: UInt

    ): Result<TransactionsResponse> {

        return handleApiResponse(

            apiResponse = retrofitClient.selectTransactionsV2M(

                userId = userId
            )
        )
    }

    internal suspend fun selectUserTransactionsAfterSpecifiedDate(

        userId: UInt,
        specifiedDate: String

    ): Result<TransactionsResponse> {

        return handleApiResponse(

            apiResponse = retrofitClient.selectUserTransactionsAfterSpecifiedDate(

                userId = userId,
                specifiedDate = specifiedDate
            )
        )
    }
}