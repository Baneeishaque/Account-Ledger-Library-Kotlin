package account.ledger.library.models

data class ChooseSpecialTransactionTypeResultModel(
    
    val isSpecialTransactionTypeSelected: Boolean,
    val selectedSpecialTransactionType: SpecialTransactionTypeModel? = null
)