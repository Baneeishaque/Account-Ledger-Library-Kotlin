package account.ledger.library.models

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class AccountLedgerGistModelForJson(
    @Required val userName: String,
    @Required val accountLedgerPages: MutableList<AccountLedgerGistAccountModel>
)

@Serializable
data class AccountLedgerGistAccountModel(
    @Required val accountId: UInt,
    @Required val accountLedgerDatePages: MutableList<AccountLedgerGistDateLedgerModelForJson>
)

@Serializable
data class AccountLedgerGistDateLedgerModelForJson(
    @Required val accountLedgerPageDate: String,
    var initialBalanceOnDate: Double? = null,
    @Required var transactionsOnDate: MutableList<AccountLedgerGistTransactionModel>,
    var finalBalanceOnDate: Double? = null
)