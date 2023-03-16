package account.ledger.library.operations

import account.ledger.library.api.response.AccountResponse
import account.ledger.library.api.response.TransactionManipulationResponse
import account.ledger.library.models.AccountFrequencyModel
import account.ledger.library.models.FrequencyOfAccountsModel
import account.ledger.library.models.UserModel
import account.ledger.library.retrofit.data.TransactionDataSource
import common.utils.library.models.IsOkModel
import common.utils.library.utils.ApiUtils
import common.utils.library.utils.MysqlUtils
import kotlinx.coroutines.runBlocking

object InsertOperations {

    @JvmStatic
    fun insertTransaction(

        userId: UInt,
        eventDateTime: String,
        particulars: String,
        amount: Float,
        fromAccountId: UInt,
        toAccountId: UInt,
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
        },
        transactionManipulationSuccessActions: () -> Unit = {}

    ): Boolean {

        val eventDateTimeConversionResult: IsOkModel<String> = eventDateTimeConversionFunction.invoke()

        if (eventDateTimeConversionResult.isOK) {

            return manipulateTransaction(

                transactionManipulationApiRequest = fun(): Result<TransactionManipulationResponse> {

                    return runBlocking {

                        TransactionDataSource().insertTransaction(
                            userId = userId,
                            fromAccountId = fromAccountId,
                            eventDateTimeString = eventDateTimeConversionResult.data!!,
                            particulars = particulars,
                            amount = amount,
                            toAccountId = toAccountId
                        )
                    }
                },
                transactionManipulationSuccessActions = transactionManipulationSuccessActions,
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

    fun updateAccountFrequency(

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

    fun getInitialAccountFrequencyForUser(

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