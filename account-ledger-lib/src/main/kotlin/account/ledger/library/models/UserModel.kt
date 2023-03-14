package account.ledger.library.models

import account.ledger.library.constants.UserJsonObjectFields
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    @SerialName(UserJsonObjectFields.id)
    val id: UInt,
    @SerialName(UserJsonObjectFields.accountFrequencies)
    var accountFrequencies: List<AccountFrequencyModel>
)