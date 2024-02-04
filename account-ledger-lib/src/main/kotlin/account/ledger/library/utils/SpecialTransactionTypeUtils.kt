package account.ledger.library.utils

import account.ledger.library.models.SpecialTransactionTypeModel
import account_ledger_library.constants.ConstantsNative

object SpecialTransactionTypeUtils {

    fun specialTransactionTypesToTextFromList(specialTransactionTypes: List<SpecialTransactionTypeModel>): String {

        var result = ""
        specialTransactionTypes.forEachIndexed { index: Int, specialTransactionType: SpecialTransactionTypeModel -> result += "${ConstantsNative.SPECIAL_TRANSACTION_TYPE_TEXT.first()}${index+1} - ${specialTransactionType.indicator}\n" }
        return result
    }
}