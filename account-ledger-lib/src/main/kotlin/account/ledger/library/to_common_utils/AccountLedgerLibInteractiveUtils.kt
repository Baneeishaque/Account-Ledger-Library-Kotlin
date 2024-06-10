package account.ledger.library.to_common_utils

import common_utils_library.constants.ConstantsCommonNative

object AccountLedgerLibInteractiveUtils {

    @JvmStatic
    fun generateDataConfirmationErrorMessage(dataSpecifier: String): String {

        return "${ConstantsCommonNative.DATA_CONFIRMATION_ERROR_TEXT} for $dataSpecifier"
    }
}
