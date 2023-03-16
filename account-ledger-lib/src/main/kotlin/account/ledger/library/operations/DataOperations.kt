package account.ledger.library.operations

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

fun getUserInitialTransactionDateFromUsername(username: String): LocalDate {

    return LocalDate.parse(
        // TODO : Use environment variable for user prefix
        username.removePrefix("banee_ishaque_k_"),
        DateTimeFormatter.ofPattern("dd_MM_yyyy", Locale.getDefault())
    )
}