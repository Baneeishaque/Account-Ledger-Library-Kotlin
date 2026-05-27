package account.ledger.library.retrofit.data

import account.ledger.library.api.response.AccountsResponse
import account.ledger.library.api.response.TransactionManipulationResponse

internal class AccountsDataSource : AppDataSource<AccountsResponse>() {

    internal suspend fun insertAccount(

        fullName: String,
        name: String,
        parentAccountId: UInt,
        accountType: String,
        notes: String,
        commodityType: String,
        commodityValue: String,
        ownerId: UInt,
        taxable: String,
        placeHolder: String

    ): Result<TransactionManipulationResponse> {

        return CommonDataSource<TransactionManipulationResponse>().processApiResponse(

            apiResponse = retrofitClient.insertAccount(

                fullName = fullName,
                name = name,
                parentAccountId = parentAccountId,
                accountType = accountType,
                notes = notes,
                commodityType = commodityType,
                commodityValue = commodityValue,
                ownerId = ownerId,
                taxable = taxable,
                placeHolder = placeHolder
            )
        )
    }


    internal suspend fun selectUserAccounts(

        userId: UInt,
        parentAccountId: UInt = 0u

    ): Result<AccountsResponse> {

        return processApiResponse(

            apiResponse = retrofitClient.selectUserAccounts(

                userId = userId,
                parentAccountId = parentAccountId
            )
        )
    }

    internal suspend fun selectUserAccountsFull(userId: UInt): Result<AccountsResponse> {

        return processApiResponse(

            apiResponse = retrofitClient.selectUserAccountsFull(userId = userId)
        )
    }
}
