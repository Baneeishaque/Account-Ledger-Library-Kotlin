package account.ledger.library.models

import account.ledger.library.api.response.AccountResponse

class ChooseAccountResult(
//    TODO : migrate to isOK model
    val chosenAccountId: UInt,
    val chosenAccount: AccountResponse? = null
)