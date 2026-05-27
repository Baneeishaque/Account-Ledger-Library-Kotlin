package account.ledger.library.api.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class AccountResponse(

    @Required @SerializedName("account_id") val id: UInt,
    @Required @SerializedName("full_name") val fullName: String,
    @Required val name: String,
    @Required @SerializedName("parent_account_id") val parentAccountId: UInt,
    @Required @SerializedName("account_type") val accountType: String,
    @Required val notes: String?,
    @Required @SerializedName("commodity_type") val commodityType: String,
    @Required @SerializedName("commodity_value") val commodityValue: String,
    @Required @SerializedName("owner_id") internal val ownerId: UInt,
    @Required val taxable: String,
    @Required @SerializedName("place_holder") val placeHolder: String
)
