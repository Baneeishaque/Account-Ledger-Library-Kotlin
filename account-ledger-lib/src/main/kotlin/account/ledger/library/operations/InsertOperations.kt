package account.ledger.library.operations

import account.ledger.library.api.response.AccountResponse
import account.ledger.library.api.response.TransactionManipulationResponse
import account.ledger.library.constants.Constants
import account.ledger.library.models.AccountFrequencyModel
import account.ledger.library.models.FrequencyOfAccountsModel
import account.ledger.library.models.UserModel
import account.ledger.library.retrofit.data.TransactionDataSource
import common.utils.library.models.IsOkModel
import common.utils.library.utils.ApiUtils
import common.utils.library.utils.JsonFileUtils
import common.utils.library.utils.MysqlUtils
import kotlinx.coroutines.runBlocking

object InsertOperations {

    @JvmStatic
    fun insertTransaction(

        userId: UInt,
        eventDateTime: String,
        particulars: String,
        amount: Float,
        fromAccount: AccountResponse,
        toAccount: AccountResponse,
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean,
        eventDateTimeConversionFunction: () -> IsOkModel<String> = {

            MysqlUtils.dateTimeTextConversion(

                dateTimeTextConversionFunction = fun(): IsOkModel<String> {

                    return MysqlUtils.normalDateTimeTextToMySqlDateTimeText(

                        normalDateTimeText = eventDateTime,
                    )
                },
            )
        }

    ): Boolean {

        val eventDateTimeConversionResult: IsOkModel<String> = eventDateTimeConversionFunction.invoke()

        if (eventDateTimeConversionResult.isOK) {

            return manipulateTransaction(

                transactionManipulationApiRequest = fun(): Result<TransactionManipulationResponse> {

                    return runBlocking {

                        TransactionDataSource().insertTransaction(
                            userId = userId,
                            fromAccountId = fromAccount.id,
                            eventDateTimeString = eventDateTimeConversionResult.data!!,
                            particulars = particulars,
                            amount = amount,
                            toAccountId = toAccount.id
                        )
                    }
                },
                transactionManipulationSuccessActions = fun() {

                    val readFrequencyOfAccountsFileResult: IsOkModel<FrequencyOfAccountsModel> =
                        JsonFileUtils.readJsonFile(

                            fileName = Constants.frequencyOfAccountsFileName,
                            isDevelopmentMode = isDevelopmentMode
                        )

                    if (isDevelopmentMode) {

                        println("readFrequencyOfAccountsFileResult : $readFrequencyOfAccountsFileResult")
                    }

                    if (readFrequencyOfAccountsFileResult.isOK) {

                        var frequencyOfAccounts: FrequencyOfAccountsModel = readFrequencyOfAccountsFileResult.data!!
                        val user: UserModel? = frequencyOfAccounts.users.find { user: UserModel -> user.id == userId }
                        if (user != null) {

                            frequencyOfAccounts = updateAccountFrequency(

                                user = user,
                                account = fromAccount,
                                frequencyOfAccounts = frequencyOfAccounts,
                                userId = userId,
                                isDevelopmentMode = isDevelopmentMode
                            )
                            frequencyOfAccounts = updateAccountFrequency(

                                user = user,
                                account = toAccount,
                                frequencyOfAccounts = frequencyOfAccounts,
                                userId = userId,
                                isDevelopmentMode = isDevelopmentMode
                            )

                        } else {
                            frequencyOfAccounts.users = frequencyOfAccounts.users.plusElement(

                                element = getInitialAccountFrequencyForUser(

                                    userId = userId,
                                    fromAccount = fromAccount,
                                    toAccount = toAccount
                                )
                            )
                        }
                        JsonFileUtils.writeJsonFile(

                            fileName = Constants.frequencyOfAccountsFileName,
                            data = frequencyOfAccounts
                        )
                    } else {

                        JsonFileUtils.writeJsonFile(

                            fileName = Constants.frequencyOfAccountsFileName,
                            data = FrequencyOfAccountsModel(

                                users = listOf(
                                    getInitialAccountFrequencyForUser(

                                        userId = userId,
                                        fromAccount = fromAccount,
                                        toAccount = toAccount
                                    )
                                )
                            )
                        )
                    }
                },
                isConsoleMode = isConsoleMode,
                isDevelopmentMode = isDevelopmentMode
            )
        }
        return false
    }

    @JvmStatic
    fun manipulateTransaction(

        transactionManipulationApiRequest: () -> Result<TransactionManipulationResponse>,
        transactionManipulationSuccessActions: () -> Unit = {},
        transactionManipulationFailureActions: (String) -> Unit = {},
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean

    ): Boolean {

        val transactionManipulationApiRequestResult: IsOkModel<TransactionManipulationResponse> =
            ApiUtils.makeApiRequestWithOptionalRetries(

                apiCallFunction = transactionManipulationApiRequest,
                isConsoleMode = isConsoleMode,
                isDevelopmentMode = isDevelopmentMode
            )

        if (transactionManipulationApiRequestResult.isOK) {

            val transactionManipulationResponseResult: TransactionManipulationResponse =
                transactionManipulationApiRequestResult.data!!
            if (transactionManipulationResponseResult.status == 0u) {

                transactionManipulationSuccessActions.invoke()
                return true

            } else {

                transactionManipulationFailureActions.invoke(transactionManipulationResponseResult.error)
            }
        }
        return false
    }

    private fun updateAccountFrequency(

        user: UserModel,
        account: AccountResponse,
        frequencyOfAccounts: FrequencyOfAccountsModel,
        userId: UInt,
        isDevelopmentMode: Boolean

    ): FrequencyOfAccountsModel {

        if (isDevelopmentMode) {

            println("frequencyOfAccounts : $frequencyOfAccounts")
        }

        val accountFrequency: AccountFrequencyModel? =
            user.accountFrequencies.find { accountFrequency: AccountFrequencyModel ->
                accountFrequency.accountID == account.id
            }
        if (isDevelopmentMode) {

            println("accountFrequency : $accountFrequency")
        }
        if (accountFrequency == null) {

            frequencyOfAccounts.users.find { localUser: UserModel -> localUser.id == userId }!!.accountFrequencies =

                frequencyOfAccounts.users.find { localUser: UserModel -> localUser.id == userId }!!.accountFrequencies.plusElement(
                    element = AccountFrequencyModel(
                        accountID = account.id, accountName = account.fullName, countOfRepetition = 1u
                    )
                )

        } else {

            frequencyOfAccounts.users.find { localUser: UserModel -> localUser.id == userId }!!.accountFrequencies.find { localAccountFrequency: AccountFrequencyModel -> localAccountFrequency.accountID == account.id }!!.countOfRepetition++
        }

        if (isDevelopmentMode) {

            println("frequencyOfAccounts : $frequencyOfAccounts")
        }
        return frequencyOfAccounts
    }

    private fun getInitialAccountFrequencyForUser(

        userId: UInt,
        fromAccount: AccountResponse,
        toAccount: AccountResponse

    ) = UserModel(

        id = userId,
        accountFrequencies = listOf(

            AccountFrequencyModel(

                accountID = fromAccount.id,
                accountName = fromAccount.fullName,
                countOfRepetition = 1u

            ), AccountFrequencyModel(

                accountID = toAccount.id,
                accountName = toAccount.fullName,
                countOfRepetition = 1u
            )
        )
    )
}