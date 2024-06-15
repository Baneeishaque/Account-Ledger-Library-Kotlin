package account.ledger.library.utils

import account.ledger.library.api.response.AccountResponse
import account.ledger.library.api.response.AccountsResponse
import account_ledger_library.constants.ConstantsNative
import common.utils.library.models.FailureBasedOnIsOkModel
import common.utils.library.models.IsOkModel
import common.utils.library.models.SuccessBasedOnIsOkModel

object HandleResponsesInteractiveLibrary {

    @JvmStatic
    fun getUserAccountsMap(

        apiResponse: Result<AccountsResponse>,
        isDevelopmentMode: Boolean = false,
        isConsoleMode: Boolean = false

    ): IsOkModel<LinkedHashMap<UInt, AccountResponse>> {

        if (apiResponse.isFailure) {

            val localizedExceptionMessage: String = (apiResponse.exceptionOrNull() as Exception).localizedMessage
            if (isConsoleMode) {

                println("Error : $localizedExceptionMessage")
            }
            return FailureBasedOnIsOkModel(

                ownError = localizedExceptionMessage
            )

        } else {

            val localAccountsResponseWithStatus: AccountsResponse = apiResponse.getOrNull() as AccountsResponse
            return if (localAccountsResponseWithStatus.status == 1u) {

                val errorMessage = "No ${ConstantsNative.ACCOUNT_TEXT}s..."
                if (isConsoleMode) {

                    println(errorMessage)
                }
                FailureBasedOnIsOkModel(ownError = errorMessage)

            } else {

                val userAccountsMap: java.util.LinkedHashMap<UInt, AccountResponse> =
                    AccountUtils.prepareUserAccountsMap(localAccountsResponseWithStatus.accounts)

                if (isDevelopmentMode) {
                    println("userAccountsMap = $userAccountsMap")
                }

                SuccessBasedOnIsOkModel(

                    ownData = userAccountsMap
                )
            }
        }
    }

    @JvmStatic
    fun getUserAccountsMapForSerializer(

        apiResponse: Result<AccountsResponse>,
        isDevelopmentMode: Boolean = false,
        isConsoleMode: Boolean = false

    ): IsOkModel<List<AccountResponse>> {

        val getUserAccountsMapResult: IsOkModel<LinkedHashMap<UInt, AccountResponse>> =
            getUserAccountsMap(

                apiResponse = apiResponse,
                isDevelopmentMode = isDevelopmentMode,
                isConsoleMode = isConsoleMode
            )

        return IsOkModel(

            isOK = getUserAccountsMapResult.isOK,
            data = getUserAccountsMapResult.data?.values?.let { accountResponses: MutableCollection<AccountResponse> ->

                accountResponses.toList()
            },
            error = getUserAccountsMapResult.error
        )
    }
}
