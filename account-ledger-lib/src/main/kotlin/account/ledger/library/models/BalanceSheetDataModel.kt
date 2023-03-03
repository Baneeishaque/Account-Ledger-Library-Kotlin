package account.ledger.library.models

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class BalanceSheetDataModel(
    @Required internal val status: Int,
    internal val data: List<BalanceSheetDataRowModel>? = null,
    internal val error: String? = null
)
