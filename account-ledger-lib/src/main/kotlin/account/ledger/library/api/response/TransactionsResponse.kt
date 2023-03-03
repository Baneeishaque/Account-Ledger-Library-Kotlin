package account.ledger.library.api.response

data class TransactionsResponse(

    val status: UInt,
    var transactions: List<TransactionResponse>
)
