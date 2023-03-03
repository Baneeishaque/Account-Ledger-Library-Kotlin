package account.ledger.library.models

import account.ledger.library.models.InsertTransactionResult

data class ViewTransactionsOutput(

    val output: String,
    val addTransactionResult: InsertTransactionResult
)
