package account.ledger.library.models

import account.ledger.library.api.response.AccountResponse

data class InsertTransactionResult(

    var isSuccess: Boolean,
    var dateTimeInText: String,
    var transactionParticulars: String,
    var transactionAmount: Float,
    var fromAccount: AccountResponse,
    var viaAccount: AccountResponse,
    var toAccount: AccountResponse
)
