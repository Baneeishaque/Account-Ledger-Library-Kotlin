package account.ledger.library.api.response

data class AccountsResponse(

    val status: UInt,
    val accounts: List<AccountResponse>
)
