package account.ledger.library.api.response

data class TransactionResponse(

    val id: UInt,
    var event_date_time: String,
    var particulars: String,
    var amount: Float,
    internal val insertion_date_time: String,
    var from_account_name: String,
    var from_account_full_name: String,
    var from_account_id: UInt,
    var to_account_name: String,
    var to_account_full_name: String,
    var to_account_id: UInt
)
