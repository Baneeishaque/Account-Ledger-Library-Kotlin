package account.ledger.library.retrofit.data

import account.ledger.library.api.response.TransactionManipulationResponse

class TransactionDataSource : AppDataSource<TransactionManipulationResponse>() {

    suspend fun insertTransaction(

        userId: UInt,
        eventDateTimeString: String,
        particulars: String,
        amount: Float,
        fromAccountId: UInt,
        toAccountId: UInt

    ): Result<TransactionManipulationResponse> {

        return handleApiResponse(
            apiResponse = retrofitClient.insertTransaction(
                userId = userId,
                eventDateTimeString = eventDateTimeString,
                particulars = particulars,
                amount = amount,
                fromAccountId = fromAccountId,
                toAccountId = toAccountId
            )
        )
    }

    suspend fun updateTransaction(

        transactionId: UInt,
        eventDateTimeString: String,
        particulars: String,
        amount: Float,
        fromAccountId: UInt,
        toAccountId: UInt

    ): Result<TransactionManipulationResponse> {

        return handleApiResponse(
            apiResponse = retrofitClient.updateTransaction(
                transactionId = transactionId,
                eventDateTimeString = eventDateTimeString,
                particulars = particulars,
                amount = amount,
                fromAccountId = fromAccountId,
                toAccountId = toAccountId
            )
        )

    }

    suspend fun deleteTransaction(transactionId: UInt): Result<TransactionManipulationResponse> {

        return handleApiResponse(
            apiResponse = retrofitClient.deleteTransaction(
                transactionId = transactionId
            )
        )
    }
}
