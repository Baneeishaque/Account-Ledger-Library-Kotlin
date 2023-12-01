package account.ledger.library.api

import common.utils.library.utils.ApiUtilsCommon

object ProjectApiUtils {

    @JvmStatic
    fun getServerApiMethodAbsoluteUrl(serverApiMethodName: String): String {

        return ApiUtilsCommon.getServerApiMethodAbsoluteUrl(
            serverApiMethodName = serverApiMethodName,
            serverFileExtension = ApiConstants.serverFileExtension
        )
    }

    @JvmStatic
    fun getServerApiMethodFullUrl(serverApiMethodName: String): String {

        return "${ApiConstants.serverApiAddress}${getServerApiMethodAbsoluteUrl(serverApiMethodName = serverApiMethodName)}"
    }

    @JvmStatic
    fun getServerApiMethodSelectUserAccountsFullUrl(): String {

        return getServerApiMethodFullUrl(serverApiMethodName = ApiConstants.selectUserAccountsFullMethod)
    }

    @JvmStatic
    fun getServerApiMethodSelectUserAccountsFullUrlForUser(userId: UInt): String {

        return "${getServerApiMethodSelectUserAccountsFullUrl()}?user_id=$userId"
    }
}
