package account.ledger.library.models

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class BalanceSheetDataRowModel(
    @Required internal val accountId: UInt,
    @Required internal val accountName: String,
    @Required internal val accountBalance: Float
)
