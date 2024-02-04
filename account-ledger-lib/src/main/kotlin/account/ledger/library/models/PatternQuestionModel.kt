package account.ledger.library.models

import account.ledger.library.constants.PatternQuestionsJsonArrayMemberFields
import common.utils.library.enums.PatternQuestionAnswerTypesEnum
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PatternQuestionModel(

    @SerialName(PatternQuestionsJsonArrayMemberFields.QUESTION)
    val question: String,
    @SerialName(PatternQuestionsJsonArrayMemberFields.ANSWER_TYPE)
    val answerType: PatternQuestionAnswerTypesEnum,
    @SerialName(PatternQuestionsJsonArrayMemberFields.POSITION_VALUES)
    val positionValues: List<String> = listOf(),
    @SerialName(PatternQuestionsJsonArrayMemberFields.POSITION_VALUE_TYPE)
    val positionValueType: String = "notApplicable",
    @SerialName(PatternQuestionsJsonArrayMemberFields.IS_SIZE_INDICATOR)
    val isSizeIndicator: Boolean = false
)
