package accountLedgerCli.enums

import account.ledger.library.api.response.AccountResponse

internal data class HandleAccountsApiResponseResult(
    internal val isAccountIdSelected: Boolean,
    internal val selectedAccount: AccountResponse? = null
)