package account.ledger.library.models

import account.ledger.library.constants.FrequencyOfAccountsJsonObjectFields
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FrequencyOfAccountsModel(
    @SerialName(FrequencyOfAccountsJsonObjectFields.USERS)
    var users: List<UserModel>
)
