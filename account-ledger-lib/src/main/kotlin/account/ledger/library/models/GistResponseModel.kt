package account.ledger.library.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Root(
    val files: Files
)

@Serializable
data class Files(
    // TODO: use environmnent variable for filename
    @SerialName("main.txt")
    val mainTxt: MainTxt
)

@Serializable
data class MainTxt(
    val content: String
)