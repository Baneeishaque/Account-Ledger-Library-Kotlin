package account.ledger.library.retrofit.data

import account.ledger.library.api.response.TransactionsResponse
import account.ledger.library.retrofit.data.AppDataSource

class TransactionsDataSource : AppDataSource<TransactionsResponse>() {

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

    suspend fun selectTransactions(

        userId: UInt

    ): Result<TransactionsResponse> {

        return handleApiResponse(

            apiResponse = retrofitClient.selectTransactionsV2M(

                userId = userId
            )
        )
    }

    suspend fun selectUserTransactionsAfterSpecifiedDate(

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