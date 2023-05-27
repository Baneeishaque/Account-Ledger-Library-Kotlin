package account.ledger.library.retrofit.data

import account.ledger.library.api.response.MultipleTransactionResponse

class MultipleTransactionDataSource : AppDataSource<MultipleTransactionResponse>() {

    internal suspend fun selectUserTransactions(

        userId: UInt,
        accountId: UInt

    ): Result<MultipleTransactionResponse> {

        return handleApiResponse(

            apiResponse = retrofitClient.selectUserTransactionsV2M(

                userId = userId,
                accountId = accountId
            )
        )
    }

    suspend fun selectTransactions(

        userId: UInt

    ): Result<MultipleTransactionResponse> {

        return handleApiResponse(

            apiResponse = retrofitClient.selectTransactionsV2M(

                userId = userId
            )
        )
    }

    suspend fun selectUserTransactionsAfterSpecifiedDate(

        userId: UInt,
        specifiedDate: String

    ): Result<MultipleTransactionResponse> {

        return handleApiResponse(

            apiResponse = retrofitClient.selectUserTransactionsAfterSpecifiedDate(

                userId = userId,
                specifiedDate = specifiedDate
            )
        )
    }
}
