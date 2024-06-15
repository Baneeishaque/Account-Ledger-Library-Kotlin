package account.ledger.library.constants

import account.ledger.library.enums.EnvironmentFileEntryEnum
import account_ledger_library.constants.ConstantsNative
import common.utils.library.constants.ConstantsCommon
import common.utils.library.models.EnvironmentFileEntryStrictModel

object EnvironmentalFileEntries {

    val walletAccountId: EnvironmentFileEntryStrictModel<EnvironmentFileEntryEnum> = EnvironmentFileEntryStrictModel(

        entry = EnvironmentFileEntryEnum.WALLET_ACCOUNT_ID,
        formalName = "Wallet ${ConstantsNative.ACCOUNT_ID_FORMAL_NAME}"
    )

    val frequent1AccountId: EnvironmentFileEntryStrictModel<EnvironmentFileEntryEnum> = EnvironmentFileEntryStrictModel(

        entry = EnvironmentFileEntryEnum.FREQUENT_1_ACCOUNT_ID,
        formalName = "${ConstantsCommon.FREQUENT_TEXT} 1 ${ConstantsNative.ACCOUNT_ID_FORMAL_NAME}"
    )

    val frequent2AccountId: EnvironmentFileEntryStrictModel<EnvironmentFileEntryEnum> = EnvironmentFileEntryStrictModel(

        entry = EnvironmentFileEntryEnum.FREQUENT_2_ACCOUNT_ID,
        formalName = "${ConstantsCommon.FREQUENT_TEXT} 2 ${ConstantsNative.ACCOUNT_ID_FORMAL_NAME}"
    )

    val frequent3AccountId: EnvironmentFileEntryStrictModel<EnvironmentFileEntryEnum> = EnvironmentFileEntryStrictModel(

        entry = EnvironmentFileEntryEnum.FREQUENT_3_ACCOUNT_ID,
        formalName = "${ConstantsCommon.FREQUENT_TEXT} 3 ${ConstantsNative.ACCOUNT_ID_FORMAL_NAME}"
    )

    val bankAccountId: EnvironmentFileEntryStrictModel<EnvironmentFileEntryEnum> = EnvironmentFileEntryStrictModel(

        entry = EnvironmentFileEntryEnum.BANK_ACCOUNT_ID,
        formalName = "Bank ${ConstantsNative.ACCOUNT_ID_FORMAL_NAME}"
    )
}
