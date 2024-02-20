package account.ledger.library.utils

import account.ledger.library.api.response.AccountResponse
import account.ledger.library.api.response.MultipleTransactionResponse
import account.ledger.library.api.response.TransactionResponse
import account.ledger.library.models.AccountFrequencyModel
import account.ledger.library.models.ChooseAccountResult
import account.ledger.library.models.FrequencyOfAccountsModel
import account.ledger.library.models.UserModel
import account.ledger.library.operations.ServerOperations
import account_ledger_library.constants.ConstantsNative
import common.utils.library.constants.CommonConstants
import common.utils.library.models.IsOkModel
import common.utils.library.utils.ApiUtilsCommon
import common.utils.library.utils.IsOkUtils
import common.utils.library.utils.JsonFileUtils

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
    fun prepareUserAccountsMap(accounts: List<AccountResponse>): LinkedHashMap<UInt, AccountResponse> {

        val userAccountsMap = LinkedHashMap<UInt, AccountResponse>()
        accounts.forEach { currentAccount -> userAccountsMap[currentAccount.id] = currentAccount }
        return userAccountsMap
    }

    @JvmStatic
    val blankChosenAccount = ChooseAccountResult(chosenAccountId = 0u)

    //TODO : Write List to String, then rewrite userAccountsToStringFromList, usersToStringFromLinkedHashMap, userAccountsToStringFromLinkedHashMap & userAccountsToStringFromListPair

    fun userAccountsToStringFromList(accounts: List<AccountResponse>): String {

        var result = ""
        accounts.forEach { account -> result += "${ConstantsNative.accountText.first()}${account.id} - ${account.fullName}\n" }
        return result
    }

    @JvmStatic
    fun getFrequentlyUsedTop10Accounts(userId: UInt, isDevelopmentMode: Boolean): String {

        return getFrequentlyUsedTopXAccounts(userId = userId, x = 10, isDevelopmentMode = isDevelopmentMode)
    }

    @JvmStatic
    internal fun getFrequentlyUsedTop20Accounts(userId: UInt, isDevelopmentMode: Boolean): String {

        return getFrequentlyUsedTopXAccounts(userId = userId, x = 20, isDevelopmentMode = isDevelopmentMode)
    }

    @JvmStatic
    internal fun getFrequentlyUsedTop30Accounts(userId: UInt, isDevelopmentMode: Boolean): String {

        return getFrequentlyUsedTopXAccounts(userId = userId, x = 30, isDevelopmentMode = isDevelopmentMode)
    }

    @JvmStatic
    fun getFrequentlyUsedTop40Accounts(userId: UInt, isDevelopmentMode: Boolean): String {

        return getFrequentlyUsedTopXAccounts(userId = userId, x = 40, isDevelopmentMode = isDevelopmentMode)
    }

    @JvmStatic
    internal fun getFrequentlyUsedTopXAccounts(userId: UInt, x: Int, isDevelopmentMode: Boolean): String {

        var result = ""

        val readFrequencyOfAccountsFileResult: IsOkModel<FrequencyOfAccountsModel> =
            JsonFileUtils.readJsonFile(
                fileName = ConstantsNative.frequencyOfAccountsFileName,
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

            CommonConstants.dashedLineSeparator

        } else {

            CommonConstants.dashedLineSeparator + "\n" + result + CommonConstants.dashedLineSeparator
        }
    }

    @JvmStatic
    internal fun getAccountFrequenciesForUser(

        frequencyOfAccounts: FrequencyOfAccountsModel,
        userId: UInt

    ): List<AccountFrequencyModel>? {

        //TODO : Allow a range of user ids
        //TODO : Accumulate frequencies of same Accounts
        return frequencyOfAccounts.users.find { user: UserModel -> user.id == userId }?.accountFrequencies
    }

    @JvmStatic
    fun <T> processUserAccountsMap(

        userId: UInt,
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean,
        successActions: (LinkedHashMap<UInt, AccountResponse>) -> T,
        failureActions: () -> Unit = fun() {}

    ) {
        val getUserAccountsMapResult: IsOkModel<LinkedHashMap<UInt, AccountResponse>> =
            HandleResponses.getUserAccountsMap(
                apiResponse = ApiUtils.getAccountsFull(

                    userId = userId,
                    isConsoleMode = isConsoleMode,
                    isDevelopmentMode = isDevelopmentMode
                )
            )

        IsOkUtils.isOkHandler(

            isOkModel = getUserAccountsMapResult,
            data = Unit,
            successActions = fun() {

                successActions.invoke(getUserAccountsMapResult.data!!)
            },
            failureActions = failureActions
        )
    }

    @JvmStatic
    fun getValidAccountById(

        desiredAccount: AccountResponse?,
        userAccountsMap: LinkedHashMap<UInt, AccountResponse>,
        idCorrectionFunction: () -> IsOkModel<UInt>

    ): IsOkModel<AccountResponse> {

        return if (desiredAccount == null) {

            val desiredAccountResult: IsOkModel<UInt> = idCorrectionFunction.invoke()
            if (desiredAccountResult.isOK) {

                getValidAccountById(

                    desiredAccount = userAccountsMap[desiredAccountResult.data],
                    userAccountsMap = userAccountsMap,
                    idCorrectionFunction = idCorrectionFunction
                )
            } else {
                IsOkModel(isOK = false)
            }
        } else {

            IsOkModel(isOK = true, data = desiredAccount)
        }
    }

    @JvmStatic
    fun getAccountBalance(

        userId: UInt,
        desiredAccountId: UInt,
        isDevelopmentMode: Boolean

    ): IsOkModel<Float> {

        var currentBalance = 0.0F
        var isSuccess = true

        ApiUtilsCommon.apiResponseHandler(

            apiResponse = ServerOperations.getUserTransactionsForAnAccount(

                userId = userId,
                accountId = desiredAccountId,
                isDevelopmentMode = isDevelopmentMode
            ),
            apiSuccessActions = fun(multipleTransactionResponse: MultipleTransactionResponse) {

                if (ApiUtils.isNotNoTransactionResponseWithMessage(

                        multipleTransactionResponse = multipleTransactionResponse
                    )
                ) {

                    multipleTransactionResponse.transactions.forEach { currentTransaction: TransactionResponse ->

                        if (currentTransaction.fromAccountId == desiredAccountId) {

                            currentBalance -= currentTransaction.amount

                        } else {

                            currentBalance += currentTransaction.amount
                        }
                    }
                } else {

                    isSuccess = false
                }
            },
            apiFailureActions = fun() {

                isSuccess = false
            }
        )
        return IsOkModel(isOK = isSuccess, data = currentBalance)
    }
}
