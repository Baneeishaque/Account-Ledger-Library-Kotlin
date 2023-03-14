package account.ledger.library.models

import common.utils.library.models.IsOkModel

data class ChooseByIdResult<T>(
    val isOkWithData: IsOkModel<T>,
    val id: UInt? = null
)
