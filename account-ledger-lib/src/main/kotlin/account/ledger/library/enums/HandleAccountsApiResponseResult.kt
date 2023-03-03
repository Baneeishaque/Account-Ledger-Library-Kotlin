package account.ledger.library.enums

import account.ledger.library.api.response.AccountResponse

data class HandleAccountsApiResponseResult(
    val isAccountIdSelected: Boolean,
    val selectedAccount: AccountResponse? = null
)