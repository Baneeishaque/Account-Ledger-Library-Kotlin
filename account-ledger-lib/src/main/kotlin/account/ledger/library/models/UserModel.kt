package account.ledger.library.models

import account.ledger.library.constants.UserJsonObjectFields
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    @SerialName(UserJsonObjectFields.ID)
    val id: UInt,
    @SerialName(UserJsonObjectFields.ACCOUNT_FREQUENCIES)
    var accountFrequencies: List<AccountFrequencyModel>
)
