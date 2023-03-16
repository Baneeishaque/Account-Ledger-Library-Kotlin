package account.ledger.library.operations

import account.ledger.library.api.response.TransactionManipulationResponse
import common.utils.library.models.IsOkModel
import common.utils.library.utils.ApiUtils

object InsertOperations {

    @JvmStatic
    fun manipulateTransaction(

        transactionManipulationApiRequest: () -> Result<TransactionManipulationResponse>,
        transactionManipulationSuccessActions: () -> Unit,
        transactionManipulationFailureActions: (String) -> Unit,
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean

    ): Boolean {

        val transactionManipulationApiRequestResult: IsOkModel<TransactionManipulationResponse> =
            ApiUtils.makeApiRequestWithOptionalRetries(

                apiCallFunction = transactionManipulationApiRequest,
                isConsoleMode = isConsoleMode,
                isDevelopmentMode = isDevelopmentMode
            )

        if (transactionManipulationApiRequestResult.isOK) {

            val transactionManipulationResponseResult: TransactionManipulationResponse =
                transactionManipulationApiRequestResult.data!!
            if (transactionManipulationResponseResult.status == 0u) {

                transactionManipulationSuccessActions.invoke()
                return true

            } else {

                transactionManipulationFailureActions.invoke(transactionManipulationResponseResult.error)
            }
        }
        return false
    }
}