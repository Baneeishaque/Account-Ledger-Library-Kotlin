package accountLedgerCli.models

import account.ledger.library.api.response.UserResponse

internal data class ChooseUserResult(internal val isChosen: Boolean, internal val chosenUser: UserResponse? = null)