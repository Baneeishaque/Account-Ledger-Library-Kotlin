package account.ledger.library.models

import account.ledger.library.api.response.TransactionResponse

data class ChooseTransactionResultModel(

    val isTransactionSelected: Boolean,
    val selectedTransaction: TransactionResponse? = null
)