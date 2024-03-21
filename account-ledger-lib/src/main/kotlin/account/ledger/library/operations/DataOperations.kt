package account.ledger.library.operations

import common.utils.library.models.IsOkModel
import common.utils.library.utils.DateTimeUtils
import java.time.LocalDate

object DataOperations {

    @JvmStatic
    fun getUserInitialTransactionDateFromUsername(username: String): IsOkModel<LocalDate> {

        return DateTimeUtils.parseDateWithPatterns(

            dateString = username.removePrefix("banee_ishaque_k_"),
            patterns = listOf("dd_MM_yyyy", "dd_MM_yyyy_HH_mm")
        )
    }
}