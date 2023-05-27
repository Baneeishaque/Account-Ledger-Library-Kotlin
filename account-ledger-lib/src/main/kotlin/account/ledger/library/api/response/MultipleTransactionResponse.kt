package account.ledger.library.api.response

data class MultipleTransactionResponse(

    val status: UInt,
    var transactions: List<TransactionResponse>
)
