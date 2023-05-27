package account.ledger.library.api.response

data class MultipleUserResponse(

    val status: UInt,
    val users: List<UserResponse>
)
