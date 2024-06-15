package account.ledger.library.utils

import account.ledger.library.api.response.AccountResponse
import account.ledger.library.enums.AccountsListSortMode
import account.ledger.library.models.AccountFrequencyModel
import account.ledger.library.models.ChooseAccountResult
import account.ledger.library.models.FrequencyOfAccountsModel
import account.ledger.library.models.UserModel
import account_ledger_library.constants.ConstantsNative
import common.utils.library.constants.ConstantsCommon
import common.utils.library.models.FailureWithoutExplanationBasedOnIsOkModel
import common.utils.library.models.IsOkModel
import common.utils.library.models.SuccessBasedOnIsOkModel
import common.utils.library.utils.JsonFileUtilsInteractive

object AccountUtils {

    @JvmStatic
    val blankAccount = AccountResponse(

        id = 0u,
        fullName = "",
        name = "",
        parentAccountId = 0u,
        accountType = "",
        notes = "",
        commodityType = "",
        commodityValue = "",
        ownerId = 0u,
        taxable = "",
        placeHolder = ""
    )


    @JvmStatic
    fun prepareUserAccountsMap(

        accounts: List<AccountResponse>

    ): LinkedHashMap<UInt, AccountResponse> {

        val userAccountsMap = LinkedHashMap<UInt, AccountResponse>()
        accounts.forEach { currentAccount: AccountResponse ->

            userAccountsMap[currentAccount.id] = currentAccount
        }
        return userAccountsMap
    }

    @JvmStatic
    val blankChosenAccount = ChooseAccountResult(chosenAccountId = 0u)

    //TODO : Write List to String, then rewrite userAccountsToStringFromList, usersToStringFromLinkedHashMap, userAccountsToStringFromLinkedHashMap & userAccountsToStringFromListPair

    fun userAccountsToStringFromList(

        accounts: List<AccountResponse>,
        sortMode: AccountsListSortMode = AccountsListSortMode.BASED_ON_ID

    ): String {

        var result = ""
        var localAccounts: List<AccountResponse> = accounts

        when (sortMode) {

            AccountsListSortMode.BASED_ON_ID -> {

                localAccounts = accounts.sortedBy { account: AccountResponse ->

                    account.id
                }
            }

            AccountsListSortMode.BASED_ON_FULL_NAME -> {

                localAccounts = accounts.sortedBy { account: AccountResponse ->

                    account.fullName
                }
            }

            AccountsListSortMode.BASED_ON_NAME -> {

                localAccounts = accounts.sortedBy { account: AccountResponse ->

                    account.name
                }
            }

            AccountsListSortMode.BASED_ON_PARENT_ID -> {

                localAccounts = accounts.sortedBy { account: AccountResponse ->

                    account.parentAccountId
                }
            }
        }
        localAccounts.forEach { account: AccountResponse ->

            result = concatenateAccountToText(

                result = result,
                account = account
            )
        }
        return result
    }

    private fun concatenateAccountToText(

        result: String,
        account: AccountResponse

    ): String = result + "${ConstantsNative.ACCOUNT_TEXT.first()}${account.id} - ${account.fullName}\n"

    @JvmStatic
    fun getFrequentlyUsedTop10Accounts(

        userId: UInt,
        isDevelopmentMode: Boolean

    ): String = getFrequentlyUsedTopXAccounts(

        userId = userId,
        x = 10,
        isDevelopmentMode = isDevelopmentMode
    )

    @JvmStatic
    internal fun getFrequentlyUsedTop20Accounts(

        userId: UInt,
        isDevelopmentMode: Boolean

    ): String = getFrequentlyUsedTopXAccounts(

        userId = userId,
        x = 20,
        isDevelopmentMode = isDevelopmentMode
    )

    @JvmStatic
    internal fun getFrequentlyUsedTop30Accounts(

        userId: UInt,
        isDevelopmentMode: Boolean

    ): String = getFrequentlyUsedTopXAccounts(

        userId = userId,
        x = 30,
        isDevelopmentMode = isDevelopmentMode
    )

    @JvmStatic
    fun getFrequentlyUsedTop40Accounts(

        userId: UInt,
        isDevelopmentMode: Boolean

    ): String = getFrequentlyUsedTopXAccounts(

        userId = userId,
        x = 40,
        isDevelopmentMode = isDevelopmentMode
    )

    @JvmStatic
    internal fun getFrequentlyUsedTopXAccounts(

        userId: UInt,
        x: Int,
        isDevelopmentMode: Boolean

    ): String {

        var result = ""

        val readFrequencyOfAccountsFileResult: IsOkModel<FrequencyOfAccountsModel> =
            JsonFileUtilsInteractive.readJsonFile(

                fileName = ConstantsNative.FREQUENCY_OF_ACCOUNTS_FILE_NAME,
                isDevelopmentMode = isDevelopmentMode
            )
        if (readFrequencyOfAccountsFileResult.isOK) {

            getAccountFrequenciesForUser(

                frequencyOfAccounts = readFrequencyOfAccountsFileResult.data!!,
                userId = userId

            )?.sortedByDescending { accountFrequency: AccountFrequencyModel ->

                accountFrequency.countOfRepetition

            }?.take(n = x)
                ?.sortedBy { accountFrequency: AccountFrequencyModel ->

                    accountFrequency.accountName

                }?.forEach { accountFrequency: AccountFrequencyModel ->

                    result += "${accountFrequency.accountID} : ${accountFrequency.accountName} [${accountFrequency.countOfRepetition}]\n"
                }
        }
        if (isDevelopmentMode) {

            println("result = $result")
        }
        return if (result.isEmpty()) {

            ConstantsCommon.dashedLineSeparator

        } else {

            ConstantsCommon.dashedLineSeparator + "\n" + result + ConstantsCommon.dashedLineSeparator
        }
    }

    @JvmStatic
    internal fun getAccountFrequenciesForUser(

        frequencyOfAccounts: FrequencyOfAccountsModel,
        userId: UInt

    ): List<AccountFrequencyModel>? = frequencyOfAccounts.users.find { user: UserModel ->

        //TODO : Allow a range of user ids
        //TODO : Accumulate frequencies of same Accounts
        user.id == userId

    }?.accountFrequencies

    @JvmStatic
    fun getValidAccountById(

        desiredAccount: AccountResponse?,
        userAccountsMap: LinkedHashMap<UInt, AccountResponse>,
        idCorrectionFunction: () -> IsOkModel<UInt>

    ): IsOkModel<AccountResponse> = if (desiredAccount == null) {

        val desiredAccountResult: IsOkModel<UInt> = idCorrectionFunction.invoke()
        if (desiredAccountResult.isOK) {

            getValidAccountById(

                desiredAccount = userAccountsMap[desiredAccountResult.data],
                userAccountsMap = userAccountsMap,
                idCorrectionFunction = idCorrectionFunction
            )
        } else {
            FailureWithoutExplanationBasedOnIsOkModel()
        }
    } else {

        SuccessBasedOnIsOkModel(ownData = desiredAccount)
    }
}
