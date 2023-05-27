package account.ledger.library.api.response

import com.google.gson.annotations.SerializedName

data class TransactionResponse(

    val id: UInt,
    @SerializedName("event_date_time") var eventDateTime: String,
    var particulars: String,
    var amount: Float,
    @SerializedName("insertion_date_time") internal val insertionDateTime: String,
    @SerializedName("from_account_name") var fromAccountName: String,
    @SerializedName("from_account_full_name") var fromAccountFullName: String,
    @SerializedName("from_account_id") var fromAccountId: UInt,
    @SerializedName("to_account_name") var toAccountName: String,
    @SerializedName("to_account_full_name") var toAccountFullName: String,
    @SerializedName("to_account_id") var toAccountId: UInt
)
