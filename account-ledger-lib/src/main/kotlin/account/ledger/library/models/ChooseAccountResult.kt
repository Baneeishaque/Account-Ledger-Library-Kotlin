package account.ledger.library.models

import account.ledger.library.api.response.AccountResponse

internal class ChooseAccountResult(
//    TODO : migrate to isOK model
    internal val chosenAccountId: UInt,
    internal val chosenAccount: AccountResponse? = null
)