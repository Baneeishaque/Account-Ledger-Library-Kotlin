package account.ledger.library.api.response

data class UserResponse(
    val id: UInt,
    internal val password: String,
    internal val status: String,
    val username: String
)