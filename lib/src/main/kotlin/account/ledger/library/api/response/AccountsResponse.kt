package account.ledger.library.api.response

internal data class AccountsResponse(

    internal val status: UInt,
    internal val accounts: List<AccountResponse>
)
