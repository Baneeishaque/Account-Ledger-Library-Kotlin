package account.ledger.library.api.response

import com.google.gson.annotations.SerializedName

data class AuthenticationResponse(

    val id: UInt,
    @SerializedName("user_count") val userCount: UInt
)
