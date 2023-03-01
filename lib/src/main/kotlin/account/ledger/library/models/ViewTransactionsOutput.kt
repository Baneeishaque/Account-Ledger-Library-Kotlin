package account.ledger.library.models

import account.ledger.library.models.InsertTransactionResult

internal data class ViewTransactionsOutput(

    internal val output: String,
    internal val addTransactionResult: InsertTransactionResult
)
