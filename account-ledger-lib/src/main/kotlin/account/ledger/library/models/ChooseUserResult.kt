package account.ledger.library.models

import account.ledger.library.api.response.UserResponse

data class ChooseUserResult(
    val isChosen: Boolean,
    val chosenUser: UserResponse? = null
)