package account.ledger.library.models

import kotlinx.serialization.Serializable

@Serializable
data class TransactionModelForLedger(

    val id: String,
    val eventDateTime: String,
    val fromAccountFullName: String,
    val fromAccountId: UInt,
    val toAccountFullName: String,
    val particulars: String,
    val amount: Float
)
