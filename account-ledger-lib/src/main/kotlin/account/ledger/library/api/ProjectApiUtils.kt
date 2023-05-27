package account.ledger.library.api

import common.utils.library.utils.ApiUtilsCommon

internal object ProjectApiUtils {

    internal fun getServerApiMethodAbsoluteUrl(serverApiMethodName: String): String {

        return ApiUtilsCommon.getServerApiMethodAbsoluteUrl(
            serverApiMethodName = serverApiMethodName,
            serverFileExtension = ApiConstants.serverFileExtension
        )
    }
}
