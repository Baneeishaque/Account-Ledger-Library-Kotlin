package account.ledger.library.models

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class AccountLedgerGistModel(
    @Required val userName: String,
    @Required val accountLedgerPages: LinkedHashMap<UInt, LinkedHashMap<String, AccountLedgerGistDateLedgerModel>>
)

@Serializable
data class AccountLedgerGistDateLedgerModel(
    var initialBalanceOnDate: Double? = null,
    @Required var transactionsOnDate: MutableList<AccountLedgerGistTransactionModel>,
    var finalBalanceOnDate: Double? = null
)
