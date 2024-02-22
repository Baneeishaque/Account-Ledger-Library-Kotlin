package account.ledger.library.models

import account.ledger.library.api.response.AccountResponse
import kotlinx.serialization.Serializable

@Serializable
data class TransactionModel(

    val fromAccount: AccountResponse,
    val toAccount: AccountResponse,
    val eventDateTimeInText: String,
    val particulars: String,
    val amount: Float
)