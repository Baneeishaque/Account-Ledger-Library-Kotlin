package account.ledger.library.models

import account.ledger.library.constants.AccountFrequencyJsonObjectFields
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccountFrequencyModel(
    @SerialName(AccountFrequencyJsonObjectFields.ACCOUNT_ID)
    val accountID: UInt,
    @SerialName(AccountFrequencyJsonObjectFields.ACCOUNT_NAME)
    val accountName: String,
    @SerialName(AccountFrequencyJsonObjectFields.COUNT_OF_REPETITION)
    var countOfRepetition: UInt
)
