package account.ledger.library.api.response

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class AccountsResponse(

    @Required val status: UInt,
    @Required val accounts: List<AccountResponse>
)
