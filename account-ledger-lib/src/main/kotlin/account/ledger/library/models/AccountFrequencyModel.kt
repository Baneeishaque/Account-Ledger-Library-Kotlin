package account.ledger.library.models

import account.ledger.library.constants.AccountFrequencyJsonObjectFields
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccountFrequencyModel(
    @SerialName(AccountFrequencyJsonObjectFields.accountID)
    val accountID: UInt,
    @SerialName(AccountFrequencyJsonObjectFields.accountName)
    val accountName: String,
    @SerialName(AccountFrequencyJsonObjectFields.countOfRepetition)
    var countOfRepetition: UInt
)