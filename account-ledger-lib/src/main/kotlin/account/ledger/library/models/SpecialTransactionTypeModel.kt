package account.ledger.library.models

import account.ledger.library.constants.SpecialTransactionTypeJsonObjectFields
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpecialTransactionTypeModel(

    @SerialName(SpecialTransactionTypeJsonObjectFields.INDICATOR)
    val indicator: String,
    @SerialName(SpecialTransactionTypeJsonObjectFields.PARTICULARS_PATTERNS)
    val particularsPatterns: List<String>,
    @SerialName(SpecialTransactionTypeJsonObjectFields.RELATED_ACCOUNTS)
    val relatedAccounts: List<UInt>,
    @SerialName(SpecialTransactionTypeJsonObjectFields.IS_REPEATING_TRANSACTION)
    val isRepeatingTransaction: Boolean,
    @SerialName(SpecialTransactionTypeJsonObjectFields.IS_COUNTING_BETTER)
    val isCountingBetter: Boolean,
    @SerialName(SpecialTransactionTypeJsonObjectFields.PATTERN_QUESTIONS)
    val patternQuestions: List<PatternQuestionModel>,
    @SerialName(SpecialTransactionTypeJsonObjectFields.RESULTANT_TEXT)
    val resultantText: String
)