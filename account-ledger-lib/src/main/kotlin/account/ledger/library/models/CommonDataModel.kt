package account.ledger.library.models

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class CommonDataModel<T>(
    @Required val status: Int,
    val data: List<T>? = null,
    val error: String? = null
)
