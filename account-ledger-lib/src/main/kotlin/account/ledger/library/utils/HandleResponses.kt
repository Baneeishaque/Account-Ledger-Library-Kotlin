package account.ledger.library.utils

import account.ledger.library.api.response.AccountResponse
import account.ledger.library.api.response.AccountsResponse
import common.utils.library.models.IsOkModel

object HandleResponses {

    fun getUserAccountsMap(

        apiResponse: Result<AccountsResponse>,
        isDevelopmentMode: Boolean = false,
        isConsoleMode: Boolean = false

    ): IsOkModel<LinkedHashMap<UInt, AccountResponse>> {

        if (apiResponse.isFailure) {

            if (isConsoleMode) {
                println("Error : ${(apiResponse.exceptionOrNull() as Exception).localizedMessage}")
            }
            return IsOkModel(isOK = false, error = (apiResponse.exceptionOrNull() as Exception).localizedMessage)

        } else {

            val localAccountsResponseWithStatus: AccountsResponse = apiResponse.getOrNull() as AccountsResponse
            return if (localAccountsResponseWithStatus.status == 1u) {

                val errorMessage = "No Accounts..."
                if (isConsoleMode) {
                    println(errorMessage)
                }
                IsOkModel(isOK = false, error = errorMessage)

            } else {

                val userAccountsMap: java.util.LinkedHashMap<UInt, AccountResponse> =
                    AccountUtils.prepareUserAccountsMap(localAccountsResponseWithStatus.accounts)

                if (isDevelopmentMode) {
                    println(userAccountsMap)
                }

                IsOkModel(
                    isOK = true,
                    data = userAccountsMap
                )
            }
        }
    }
}
