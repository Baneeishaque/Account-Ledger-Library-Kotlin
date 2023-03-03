package account.ledger.library.api.response

data class UsersResponse(

    val status: UInt,
    val users: List<UserResponse>
)