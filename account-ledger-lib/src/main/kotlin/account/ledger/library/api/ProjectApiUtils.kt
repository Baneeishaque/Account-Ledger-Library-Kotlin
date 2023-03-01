package account.ledger.library.api

import common.utils.library.utils.ApiUtils

internal object ProjectApiUtils {

    internal fun getServerApiMethodAbsoluteUrl(serverApiMethodName: String): String {

        return ApiUtils.getServerApiMethodAbsoluteUrl(
            serverApiMethodName = serverApiMethodName,
            serverFileExtension = ApiConstants.serverFileExtension
        )
    }
}
