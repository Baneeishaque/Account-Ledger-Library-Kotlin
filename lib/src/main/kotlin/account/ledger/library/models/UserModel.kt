package account.ledger.library.models

import account.ledger.library.constants.UserJsonObjectFields
import account.ledger.library.models.AccountFrequencyModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UserModel(
    @SerialName(UserJsonObjectFields.id)
    val id: UInt,
    @SerialName(UserJsonObjectFields.accountFrequencies)
    var accountFrequencies: List<AccountFrequencyModel>
)