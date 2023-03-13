package account.ledger.library.models

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class AccountLedgerGistModel(
    @Required val userName: String,
    @Required val accountLedgerIds: LinkedHashMap<UInt, LinkedHashMap<String, AccountLedgerGistDateLedgerModel>>
)