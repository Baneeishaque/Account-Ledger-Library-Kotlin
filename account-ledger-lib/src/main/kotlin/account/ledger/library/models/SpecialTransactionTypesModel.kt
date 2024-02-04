package account.ledger.library.models

import account.ledger.library.constants.SpecialTransactionTypesJsonObjectFields
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpecialTransactionTypesModel(

    @SerialName(SpecialTransactionTypesJsonObjectFields.SPECIAL_TRANSACTION_TYPES)
    val specialTransactionTypeModels: List<SpecialTransactionTypeModel>
)