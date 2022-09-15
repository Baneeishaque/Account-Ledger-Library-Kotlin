package account.ledger.library.api.response

internal data class TransactionManipulationResponse(

    internal val status: UInt,
    internal val error: String
)
