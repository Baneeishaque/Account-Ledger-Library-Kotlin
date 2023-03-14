package account.ledger.library.models

data class ViewTransactionsOutput(

    val output: String,
    val addTransactionResult: InsertTransactionResult
)
