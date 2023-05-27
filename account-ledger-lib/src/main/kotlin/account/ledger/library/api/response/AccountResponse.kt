package account.ledger.library.api.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class AccountResponse(

    @Required @SerializedName("account_id") val id: UInt,
    @Required @SerializedName("full_name") val fullName: String,
    @Required val name: String,
    @Required @SerializedName("parent_account_id") internal val parentAccountId: UInt,
    @Required @SerializedName("account_type") internal val accountType: String,
    @Required internal val notes: String?,
    @Required @SerializedName("commodity_type") internal val commodityType: String,
    @Required @SerializedName("commodity_value") internal val commodityValue: String,
    @Required @SerializedName("owner_id") internal val ownerId: UInt,
    @Required internal val taxable: String,
    @Required @SerializedName("place_holder") internal val placeHolder: String
)
