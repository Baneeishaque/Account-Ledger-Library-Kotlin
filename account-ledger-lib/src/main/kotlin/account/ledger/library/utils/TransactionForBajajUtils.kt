package account.ledger.library.utils

import account.ledger.library.enums.TransactionTypeEnum

object TransactionForBajajUtils {

    @JvmStatic
    val bajajTransactionTypes: List<TransactionTypeEnum> =
        TransactionForBajajCoinsUtils.bajajCoinTransactionTypes + TransactionForBajajWalletUtils.bajajWalletTransactionTypes
}
