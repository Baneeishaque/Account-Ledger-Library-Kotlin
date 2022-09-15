package account.ledger.library.api.response

internal data class UserResponse(
    internal val id: UInt,
    internal val password: String,
    internal val status: String,
    internal val username: String
)