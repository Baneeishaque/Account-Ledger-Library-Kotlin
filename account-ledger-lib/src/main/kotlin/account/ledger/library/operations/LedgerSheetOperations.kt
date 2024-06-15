package account.ledger.library.operations

import account.ledger.library.api.response.MultipleTransactionResponse
import account.ledger.library.api.response.TransactionResponse
import account.ledger.library.api.response.UserResponse
import account.ledger.library.enums.BalanceSheetRefineLevelEnum
import account.ledger.library.enums.EnvironmentFileEntryEnum
import account.ledger.library.models.BalanceSheetDataRowModel
import account.ledger.library.models.ChooseUserResult
import account.ledger.library.retrofit.data.MultipleTransactionDataSource
import account.ledger.library.utils.UserUtils
import account_ledger_library.constants.ConstantsNative
import common.utils.library.constants.ConstantsCommon
import common.utils.library.models.CommonDataModel
import common.utils.library.models.IsOkModel
import common.utils.library.utils.*
import io.github.cdimascio.dotenv.Dotenv
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import java.time.LocalDate
import kotlin.math.absoluteValue

object LedgerSheetOperations {

    @JvmStatic
    fun balanceSheetOfUser(

        usersMap: LinkedHashMap<UInt, UserResponse>,
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean,
        dotEnv: Dotenv
    ) {
        val chooseUserResult: ChooseUserResult = UserUtils.handleUserSelection(
            chosenUserId = ListUtilsInteractive.getValidIndexFromCollectionWithSelectionPromptAndZeroAsBack(

                map = usersMap,
                itemSpecification = ConstantsNative.USER_TEXT,
                items = UserUtils.usersToTextFromLinkedHashMap(usersMap = usersMap)

            ), usersMap = usersMap
        )
        if (chooseUserResult.isChosen) {

            printBalanceSheetOfUser(

                currentUserName = chooseUserResult.chosenUser!!.username,
                currentUserId = chooseUserResult.chosenUser.id,
                isConsoleMode = isConsoleMode,
                isDevelopmentMode = isDevelopmentMode,
                dotEnv = dotEnv
            )
        }
    }

    @JvmStatic
    fun printSheetOfUser(

        currentUserName: String,
        currentUserId: UInt,
        getDesiredAccountIdsForSheetOfUser: (MultipleTransactionResponse) -> IsOkModel<MutableMap<UInt, String>>,
        sheetTitle: String,
        isNotApiCall: Boolean = true,
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean,
        operationsAfterPrint: (List<BalanceSheetDataRowModel>) -> Unit = fun(_: List<BalanceSheetDataRowModel>) {},
        operationsWithData: (List<BalanceSheetDataRowModel>, Boolean, String, String, (List<BalanceSheetDataRowModel>) -> Unit) -> List<BalanceSheetDataRowModel> = LedgerSheetOperations::printSheetOfUserDefaultOperation

    ): IsOkModel<List<BalanceSheetDataRowModel>> {

        var isOkModel = IsOkModel<List<BalanceSheetDataRowModel>>(isOK = false)

        IsOkUtils.handleIsOkObject(

            isOkModel = generateSheetOfUser(

                currentUserName = currentUserName,
                currentUserId = currentUserId,
                getDesiredAccountIdsForSheetOfUser = getDesiredAccountIdsForSheetOfUser,
                isNotApiCall = isNotApiCall,
                isConsoleMode = isConsoleMode,
                isDevelopmentMode = isDevelopmentMode
            ),
            dataOperation = fun(data: String) {

                isOkModel = IsOkModel(

                    isOK = true,
                    data = operationsWithData.invoke(

                        Json.decodeFromString(

                            deserializer = CommonDataModel.serializer(BalanceSheetDataRowModel.serializer()),
                            string = data

                        ).data!!,
                        isConsoleMode,
                        currentUserName,
                        sheetTitle,
                        operationsAfterPrint
                    )
                )
            },
            errorOperation = fun(error: String) {

                println(error)
                isOkModel.error = error
            }
        )
        return isOkModel
    }

    @JvmStatic
    fun printSheetOfUserDefaultOperation(

        balanceSheetDataRows: List<BalanceSheetDataRowModel>,
        isConsoleMode: Boolean,
        currentUserName: String,
        sheetTitle: String,
        operationsAfterPrint: (List<BalanceSheetDataRowModel>) -> Unit

    ): List<BalanceSheetDataRowModel> {

        if (isConsoleMode) {

            println("\nUser : $currentUserName $sheetTitle Sheet Ledger")
            println(ConstantsCommon.dashedLineSeparator)
            for (balanceSheetDataRow: BalanceSheetDataRowModel in balanceSheetDataRows) {

                println("${balanceSheetDataRow.accountId} : ${balanceSheetDataRow.accountName} : ${balanceSheetDataRow.accountBalance}")
            }
            operationsAfterPrint.invoke(balanceSheetDataRows)

        } else {

            println(

                Json.encodeToString(

                    serializer = CommonDataModel.serializer(BalanceSheetDataRowModel.serializer()),
                    value = CommonDataModel(

                        status = 0,
                        data = balanceSheetDataRows
                    )
                )
            )
        }
        return balanceSheetDataRows
    }

    @JvmStatic
    fun printBalanceSheetOfUser(

        currentUserName: String,
        currentUserId: UInt,
        refineLevel: BalanceSheetRefineLevelEnum = BalanceSheetRefineLevelEnum.WITHOUT_EXPENSE_ACCOUNTS,
        isNotApiCall: Boolean = true,
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean,
        dotEnv: Dotenv

    ): IsOkModel<List<BalanceSheetDataRowModel>> {

        return printSheetOfUser(

            currentUserName = currentUserName,
            currentUserId = currentUserId,
            getDesiredAccountIdsForSheetOfUser = fun(selectUserTransactionsAfterSpecifiedDateResult: MultipleTransactionResponse): IsOkModel<MutableMap<UInt, String>> {

                return getDesiredAccountIdsForBalanceSheetOfUser(

                    refineLevel = refineLevel,
                    selectUserTransactionsAfterSpecifiedDateResult = selectUserTransactionsAfterSpecifiedDateResult,
                    isDevelopmentMode = isDevelopmentMode,
                    dotEnv = dotEnv
                )
            },
            sheetTitle = "Balance",
            isNotApiCall = isNotApiCall,
            isConsoleMode = isConsoleMode,
            isDevelopmentMode = isDevelopmentMode
        )
    }

    @JvmStatic
    fun printSheetOfUserWithFinalBalance(

        currentUserName: String,
        currentUserId: UInt,
        getDesiredAccountIdsForSheetOfUser: (MultipleTransactionResponse) -> IsOkModel<MutableMap<UInt, String>>,
        sheetTitle: String,
        isNotApiCall: Boolean = true,
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean

    ): IsOkModel<List<BalanceSheetDataRowModel>> {

        return printSheetOfUser(

            currentUserName = currentUserName,
            currentUserId = currentUserId,
            getDesiredAccountIdsForSheetOfUser = getDesiredAccountIdsForSheetOfUser,
            sheetTitle = sheetTitle,
            isNotApiCall = isNotApiCall,
            isConsoleMode = isConsoleMode,
            isDevelopmentMode = isDevelopmentMode,
            operationsAfterPrint = LedgerSheetOperations::printFinalBalanceOfBalanceSheetDataRows
        )
    }

    @JvmStatic
    fun printFinalBalanceOfBalanceSheetDataRows(balanceSheetDataRows: List<BalanceSheetDataRowModel>) {

        println(ConstantsCommon.dashedLineSeparator)
        println(calculateFinalBalanceOfBalanceSheetDataRows(balanceSheetDataRows))
    }

    @JvmStatic
    fun calculateFinalBalanceOfBalanceSheetDataRows(balanceSheetDataRows: List<BalanceSheetDataRowModel>): Float {

        var finalBalance = 0F
        balanceSheetDataRows.forEach { balanceSheetDataRow: BalanceSheetDataRowModel ->

            finalBalance += balanceSheetDataRow.accountBalance.absoluteValue
        }
        return finalBalance
    }

    @JvmStatic
    fun printSheetOfUserByAccountIdsFromEnvironment(

        currentUserName: String,
        currentUserId: UInt,
        sheetTitle: String,
        isNotApiCall: Boolean = true,
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean,
        operationsAfterPrint: (List<BalanceSheetDataRowModel>) -> Unit = fun(_: List<BalanceSheetDataRowModel>) {},
        environmentVariable: EnvironmentFileEntryEnum,
        environmentVariablesForAccountsToIgnore: List<EnvironmentFileEntryEnum>,
        operationsWithData: (List<BalanceSheetDataRowModel>, Boolean, String, String, (List<BalanceSheetDataRowModel>) -> Unit) -> List<BalanceSheetDataRowModel>,
        dotEnv: Dotenv

    ): IsOkModel<List<BalanceSheetDataRowModel>> {

        return printSheetOfUser(

            currentUserName = currentUserName,
            currentUserId = currentUserId,
            getDesiredAccountIdsForSheetOfUser = fun(selectUserTransactionsAfterSpecifiedDateResult: MultipleTransactionResponse): IsOkModel<MutableMap<UInt, String>> {

                return getDesiredAccountIdsForSheetOfUserBasedOnEnvironment(

                    environmentVariable = environmentVariable,
                    selectUserTransactionsAfterSpecifiedDateResult = selectUserTransactionsAfterSpecifiedDateResult,
                    isDevelopmentMode = isDevelopmentMode,
                    environmentVariablesForAccountsToIgnore = environmentVariablesForAccountsToIgnore,
                    dotEnv = dotEnv
                )
            },
            sheetTitle = sheetTitle,
            isNotApiCall = isNotApiCall,
            isConsoleMode = isConsoleMode,
            isDevelopmentMode = isDevelopmentMode,
            operationsAfterPrint = operationsAfterPrint,
            operationsWithData = operationsWithData
        )
    }

    @JvmStatic
    fun printSheetOfUserWithFinalBalanceByAccountIdsFromEnvironment(

        currentUserName: String,
        currentUserId: UInt,
        sheetTitle: String,
        isNotApiCall: Boolean = true,
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean,
        environmentVariable: EnvironmentFileEntryEnum,
        environmentVariablesForAccountsToIgnore: List<EnvironmentFileEntryEnum>,
        operationsWithData: (List<BalanceSheetDataRowModel>, Boolean, String, String, (List<BalanceSheetDataRowModel>) -> Unit) -> List<BalanceSheetDataRowModel>,
        dotEnv: Dotenv

    ): IsOkModel<List<BalanceSheetDataRowModel>> = printSheetOfUserByAccountIdsFromEnvironment(

        currentUserName = currentUserName,
        currentUserId = currentUserId,
        sheetTitle = sheetTitle,
        isNotApiCall = isNotApiCall,
        isConsoleMode = isConsoleMode,
        isDevelopmentMode = isDevelopmentMode,
        operationsAfterPrint = LedgerSheetOperations::printFinalBalanceOfBalanceSheetDataRows,
        environmentVariable = environmentVariable,
        environmentVariablesForAccountsToIgnore = environmentVariablesForAccountsToIgnore,
        operationsWithData = operationsWithData,
        dotEnv = dotEnv
    )

    @JvmStatic
    fun printExpenseSheetOfUser(

        currentUserName: String,
        currentUserId: UInt,
        isNotApiCall: Boolean = true,
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean,
        dotEnv: Dotenv

    ): IsOkModel<List<BalanceSheetDataRowModel>> {

        return printSheetOfUserWithFinalBalanceByAccountIdsFromEnvironment(

            currentUserName = currentUserName,
            currentUserId = currentUserId,
            sheetTitle = "Expense",
            isNotApiCall = isNotApiCall,
            isConsoleMode = isConsoleMode,
            isDevelopmentMode = isDevelopmentMode,
            environmentVariable = EnvironmentFileEntryEnum.EXPENSE_ACCOUNT_IDS_FOR_SHEET,
            environmentVariablesForAccountsToIgnore = listOf(

                EnvironmentFileEntryEnum.EXPENSE_INCOME_IGNORE_ACCOUNT_IDS_FOR_SHEET,
                EnvironmentFileEntryEnum.INCOME_ACCOUNT_IDS_FOR_SHEET,
                EnvironmentFileEntryEnum.DEBIT_OR_CREDIT_ACCOUNT_IDS_FOR_SHEET,
                EnvironmentFileEntryEnum.ASSET_ACCOUNT_IDS_FOR_SHEET
            ),
            operationsWithData = LedgerSheetOperations::printSheetOfUserDefaultOperation,
            dotEnv = dotEnv
        )
    }

    @JvmStatic
    fun printNotConsiderForIncomeExpenseSheetOfUser(

        currentUserName: String,
        currentUserId: UInt,
        isNotApiCall: Boolean = true,
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean,
        dotEnv: Dotenv

    ): IsOkModel<List<BalanceSheetDataRowModel>> {

        return printSheetOfUserByAccountIdsFromEnvironment(

            currentUserName = currentUserName,
            currentUserId = currentUserId,
            sheetTitle = "Not Consider for Income / Expense",
            isNotApiCall = isNotApiCall,
            isConsoleMode = isConsoleMode,
            isDevelopmentMode = isDevelopmentMode,
            environmentVariable = EnvironmentFileEntryEnum.EXPENSE_INCOME_IGNORE_ACCOUNT_IDS_FOR_SHEET,
            environmentVariablesForAccountsToIgnore = listOf(

                EnvironmentFileEntryEnum.INCOME_ACCOUNT_IDS_FOR_SHEET,
                EnvironmentFileEntryEnum.EXPENSE_ACCOUNT_IDS_FOR_SHEET,
                EnvironmentFileEntryEnum.DEBIT_OR_CREDIT_ACCOUNT_IDS_FOR_SHEET,
                EnvironmentFileEntryEnum.ASSET_ACCOUNT_IDS_FOR_SHEET
            ),
            operationsWithData = LedgerSheetOperations::printSheetOfUserDefaultOperation,
            dotEnv = dotEnv
        )
    }

    @JvmStatic
    fun printNotConsiderForIncomeExpenseOrDebitCreditSheetOfUser(

        currentUserName: String,
        currentUserId: UInt,
        isNotApiCall: Boolean = true,
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean,
        dotEnv: Dotenv

    ): IsOkModel<List<BalanceSheetDataRowModel>> {

        return printSheetOfUserByAccountIdsFromEnvironment(

            currentUserName = currentUserName,
            currentUserId = currentUserId,
            sheetTitle = "Not Consider for Income / Expense / Debit / Credit",
            isNotApiCall = isNotApiCall,
            isConsoleMode = isConsoleMode,
            isDevelopmentMode = isDevelopmentMode,
            environmentVariable = EnvironmentFileEntryEnum.EXPENSE_INCOME_DEBIT_CREDIT_IGNORE_ACCOUNT_IDS_FOR_SHEET,
            environmentVariablesForAccountsToIgnore = listOf(

                EnvironmentFileEntryEnum.INCOME_ACCOUNT_IDS_FOR_SHEET,
                EnvironmentFileEntryEnum.EXPENSE_ACCOUNT_IDS_FOR_SHEET,
                EnvironmentFileEntryEnum.DEBIT_OR_CREDIT_ACCOUNT_IDS_FOR_SHEET,
                EnvironmentFileEntryEnum.ASSET_ACCOUNT_IDS_FOR_SHEET
            ),
            operationsWithData = LedgerSheetOperations::printSheetOfUserDefaultOperation,
            dotEnv = dotEnv
        )
    }

    @JvmStatic
    fun printNotConsiderForIncomeExpenseDebitCreditOrAssetSheetOfUser(

        currentUserName: String,
        currentUserId: UInt,
        isNotApiCall: Boolean = true,
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean,
        dotEnv: Dotenv

    ): IsOkModel<List<BalanceSheetDataRowModel>> {

        return printSheetOfUserByAccountIdsFromEnvironment(

            currentUserName = currentUserName,
            currentUserId = currentUserId,
            sheetTitle = "Not Consider for Income / Expense / Debit / Credit / Asset",
            isNotApiCall = isNotApiCall,
            isConsoleMode = isConsoleMode,
            isDevelopmentMode = isDevelopmentMode,
            environmentVariable = EnvironmentFileEntryEnum.EXPENSE_INCOME_DEBIT_CREDIT_ASSET_IGNORE_ACCOUNT_IDS_FOR_SHEET,
            environmentVariablesForAccountsToIgnore = listOf(

                EnvironmentFileEntryEnum.INCOME_ACCOUNT_IDS_FOR_SHEET,
                EnvironmentFileEntryEnum.EXPENSE_ACCOUNT_IDS_FOR_SHEET,
                EnvironmentFileEntryEnum.DEBIT_OR_CREDIT_ACCOUNT_IDS_FOR_SHEET,
                EnvironmentFileEntryEnum.ASSET_ACCOUNT_IDS_FOR_SHEET
            ),
            operationsWithData = LedgerSheetOperations::printSheetOfUserDefaultOperation,
            dotEnv = dotEnv
        )
    }

    @JvmStatic
    fun printDebitCreditSheetOfUser(

        currentUserName: String,
        currentUserId: UInt,
        isNotApiCall: Boolean = true,
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean,
        dotEnv: Dotenv

    ): IsOkModel<List<BalanceSheetDataRowModel>> {

        return printSheetOfUserByAccountIdsFromEnvironment(

            currentUserName = currentUserName,
            currentUserId = currentUserId,
            sheetTitle = "Debit / Credit",
            isNotApiCall = isNotApiCall,
            isConsoleMode = isConsoleMode,
            isDevelopmentMode = isDevelopmentMode,
            environmentVariable = EnvironmentFileEntryEnum.DEBIT_OR_CREDIT_ACCOUNT_IDS_FOR_SHEET,
            environmentVariablesForAccountsToIgnore = listOf(

                EnvironmentFileEntryEnum.INCOME_ACCOUNT_IDS_FOR_SHEET,
                EnvironmentFileEntryEnum.EXPENSE_ACCOUNT_IDS_FOR_SHEET,
                EnvironmentFileEntryEnum.ASSET_ACCOUNT_IDS_FOR_SHEET,
                EnvironmentFileEntryEnum.EXPENSE_INCOME_DEBIT_CREDIT_IGNORE_ACCOUNT_IDS_FOR_SHEET
            ),
            operationsWithData = LedgerSheetOperations::printSheetOfUserDefaultOperation,
            dotEnv = dotEnv
        )
    }

    @JvmStatic
    fun printDebitSheetOfUser(

        currentUserName: String,
        currentUserId: UInt,
        isNotApiCall: Boolean = true,
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean,
        dotEnv: Dotenv

    ): IsOkModel<List<BalanceSheetDataRowModel>> {

        return printSheetOfUserWithFinalBalanceByAccountIdsFromEnvironment(

            currentUserName = currentUserName,
            currentUserId = currentUserId,
            sheetTitle = "Debit",
            isNotApiCall = isNotApiCall,
            isConsoleMode = isConsoleMode,
            isDevelopmentMode = isDevelopmentMode,
            environmentVariable = EnvironmentFileEntryEnum.DEBIT_OR_CREDIT_ACCOUNT_IDS_FOR_SHEET,
            environmentVariablesForAccountsToIgnore = listOf(

                EnvironmentFileEntryEnum.INCOME_ACCOUNT_IDS_FOR_SHEET,
                EnvironmentFileEntryEnum.EXPENSE_ACCOUNT_IDS_FOR_SHEET,
                EnvironmentFileEntryEnum.ASSET_ACCOUNT_IDS_FOR_SHEET,
                EnvironmentFileEntryEnum.EXPENSE_INCOME_DEBIT_CREDIT_IGNORE_ACCOUNT_IDS_FOR_SHEET
            ),
            operationsWithData = fun(

                balanceSheetDataRows: List<BalanceSheetDataRowModel>,
                isConsoleMode: Boolean,
                currentUserName: String,
                sheetTitle: String,
                operationsAfterPrint: (List<BalanceSheetDataRowModel>) -> Unit

            ): List<BalanceSheetDataRowModel> {

                return printSheetOfUserDefaultOperation(

                    balanceSheetDataRows = balanceSheetDataRows.filter { balanceSheetDataRow: BalanceSheetDataRowModel ->

                        balanceSheetDataRow.accountBalance > 0
                    },
                    isConsoleMode = isConsoleMode,
                    currentUserName = currentUserName,
                    sheetTitle = sheetTitle,
                    operationsAfterPrint = operationsAfterPrint
                )
            },
            dotEnv = dotEnv
        )
    }

    @JvmStatic
    fun printCreditSheetOfUser(

        currentUserName: String,
        currentUserId: UInt,
        isNotApiCall: Boolean = true,
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean,
        dotEnv: Dotenv

    ): IsOkModel<List<BalanceSheetDataRowModel>> {

        return printSheetOfUserWithFinalBalanceByAccountIdsFromEnvironment(

            currentUserName = currentUserName,
            currentUserId = currentUserId,
            sheetTitle = "Credit",
            isNotApiCall = isNotApiCall,
            isConsoleMode = isConsoleMode,
            isDevelopmentMode = isDevelopmentMode,
            environmentVariable = EnvironmentFileEntryEnum.DEBIT_OR_CREDIT_ACCOUNT_IDS_FOR_SHEET,
            environmentVariablesForAccountsToIgnore = listOf(

                EnvironmentFileEntryEnum.INCOME_ACCOUNT_IDS_FOR_SHEET,
                EnvironmentFileEntryEnum.EXPENSE_ACCOUNT_IDS_FOR_SHEET,
                EnvironmentFileEntryEnum.ASSET_ACCOUNT_IDS_FOR_SHEET,
                EnvironmentFileEntryEnum.EXPENSE_INCOME_DEBIT_CREDIT_IGNORE_ACCOUNT_IDS_FOR_SHEET
            ),
            operationsWithData = fun(

                balanceSheetDataRows: List<BalanceSheetDataRowModel>,
                isConsoleMode: Boolean,
                currentUserName: String,
                sheetTitle: String,
                operationsAfterPrint: (List<BalanceSheetDataRowModel>) -> Unit

            ): List<BalanceSheetDataRowModel> {

                return printSheetOfUserDefaultOperation(

                    balanceSheetDataRows = balanceSheetDataRows.filter { balanceSheetDataRow: BalanceSheetDataRowModel ->

                        balanceSheetDataRow.accountBalance < 0
                    },
                    isConsoleMode = isConsoleMode,
                    currentUserName = currentUserName,
                    sheetTitle = sheetTitle,
                    operationsAfterPrint = operationsAfterPrint
                )
            },
            dotEnv = dotEnv
        )
    }

    @JvmStatic
    fun printAssetSheetOfUser(

        currentUserName: String,
        currentUserId: UInt,
        isNotApiCall: Boolean = true,
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean,
        dotEnv: Dotenv

    ): IsOkModel<List<BalanceSheetDataRowModel>> {

        return printSheetOfUserWithFinalBalanceByAccountIdsFromEnvironment(

            currentUserName = currentUserName,
            currentUserId = currentUserId,
            sheetTitle = "Asset",
            isNotApiCall = isNotApiCall,
            isConsoleMode = isConsoleMode,
            isDevelopmentMode = isDevelopmentMode,
            environmentVariable = EnvironmentFileEntryEnum.ASSET_ACCOUNT_IDS_FOR_SHEET,
            environmentVariablesForAccountsToIgnore = listOf(

                EnvironmentFileEntryEnum.INCOME_ACCOUNT_IDS_FOR_SHEET,
                EnvironmentFileEntryEnum.EXPENSE_ACCOUNT_IDS_FOR_SHEET,
                EnvironmentFileEntryEnum.DEBIT_OR_CREDIT_ACCOUNT_IDS_FOR_SHEET,
                EnvironmentFileEntryEnum.EXPENSE_INCOME_DEBIT_CREDIT_ASSET_IGNORE_ACCOUNT_IDS_FOR_SHEET
            ),
            operationsWithData = LedgerSheetOperations::printSheetOfUserDefaultOperation,
            dotEnv = dotEnv
        )
    }

    @JvmStatic
    fun printProfitSheetOfUser(

        currentUserName: String,
        currentUserId: UInt,
        isNotApiCall: Boolean = true,
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean,
        dotEnv: Dotenv

    ) {
        printSheetOfUserWithSummarizedBalance(

            currentUserName = currentUserName,
            currentUserId = currentUserId,
            isNotApiCall = isNotApiCall,
            isConsoleMode = isConsoleMode,
            isDevelopmentMode = isDevelopmentMode,
            balanceSheetOkModelFromSubtract = LedgerSheetOperations::printIncomeSheetOfUser,
            balanceSheetOkModelToSubtract = LedgerSheetOperations::printExpenseSheetOfUser,
            dotEnv = dotEnv
        )
    }

    @JvmStatic
    fun printDebitCreditBalanceSheetOfUser(

        currentUserName: String,
        currentUserId: UInt,
        isNotApiCall: Boolean = true,
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean,
        dotEnv: Dotenv

    ) {
        printSheetOfUserWithSummarizedBalance(

            currentUserName = currentUserName,
            currentUserId = currentUserId,
            isNotApiCall = isNotApiCall,
            isConsoleMode = isConsoleMode,
            isDevelopmentMode = isDevelopmentMode,
            balanceSheetOkModelFromSubtract = LedgerSheetOperations::printDebitSheetOfUser,
            balanceSheetOkModelToSubtract = LedgerSheetOperations::printCreditSheetOfUser,
            dotEnv = dotEnv
        )
    }

    @JvmStatic
    fun printSheetOfUserWithSummarizedBalance(

        currentUserName: String,
        currentUserId: UInt,
        isNotApiCall: Boolean = true,
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean,
        balanceSheetOkModelFromSubtract: (String, UInt, Boolean, Boolean, Boolean, Dotenv) -> IsOkModel<List<BalanceSheetDataRowModel>>,
        balanceSheetOkModelToSubtract: (String, UInt, Boolean, Boolean, Boolean, Dotenv) -> IsOkModel<List<BalanceSheetDataRowModel>>,
        dotEnv: Dotenv
    ) {
        val balanceSheetOkModelFromSubtractResult: IsOkModel<List<BalanceSheetDataRowModel>> =
            balanceSheetOkModelFromSubtract.invoke(

                currentUserName,
                currentUserId,
                isNotApiCall,
                isConsoleMode,
                isDevelopmentMode,
                dotEnv
            )
        val balanceSheetOkModelToSubtractResult: IsOkModel<List<BalanceSheetDataRowModel>> =
            balanceSheetOkModelToSubtract.invoke(

                currentUserName,
                currentUserId,
                isNotApiCall,
                isConsoleMode,
                isDevelopmentMode,
                dotEnv
            )
        if (balanceSheetOkModelFromSubtractResult.isOK && balanceSheetOkModelToSubtractResult.isOK) {

            println(ConstantsCommon.DOUBLE_DASHED_LINE_SEPARATOR)

            val fromBalance = calculateFinalBalanceOfBalanceSheetDataRows(balanceSheetOkModelFromSubtractResult.data!!)
            val toBalance = calculateFinalBalanceOfBalanceSheetDataRows(balanceSheetOkModelToSubtractResult.data!!)

            println("$fromBalance - $toBalance = ${fromBalance - toBalance}")
        }
    }

    @JvmStatic
    fun printIncomeSheetOfUser(

        currentUserName: String,
        currentUserId: UInt,
        isNotApiCall: Boolean = true,
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean,
        dotEnv: Dotenv

    ): IsOkModel<List<BalanceSheetDataRowModel>> {

        return printSheetOfUserWithFinalBalanceByAccountIdsFromEnvironment(

            currentUserName = currentUserName,
            currentUserId = currentUserId,
            sheetTitle = "Income",
            isNotApiCall = isNotApiCall,
            isConsoleMode = isConsoleMode,
            isDevelopmentMode = isDevelopmentMode,
            environmentVariable = EnvironmentFileEntryEnum.INCOME_ACCOUNT_IDS_FOR_SHEET,
            environmentVariablesForAccountsToIgnore = listOf(

                EnvironmentFileEntryEnum.EXPENSE_INCOME_IGNORE_ACCOUNT_IDS_FOR_SHEET,
                EnvironmentFileEntryEnum.EXPENSE_ACCOUNT_IDS_FOR_SHEET,
                EnvironmentFileEntryEnum.ASSET_ACCOUNT_IDS_FOR_SHEET,
                EnvironmentFileEntryEnum.DEBIT_OR_CREDIT_ACCOUNT_IDS_FOR_SHEET
            ),
            operationsWithData = LedgerSheetOperations::printSheetOfUserDefaultOperation,
            dotEnv = dotEnv
        )
    }

    private fun generateSheetOfUser(

        currentUserName: String,
        currentUserId: UInt,
        getDesiredAccountIdsForSheetOfUser: (MultipleTransactionResponse) -> IsOkModel<MutableMap<UInt, String>>,
        isNotApiCall: Boolean = true,
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean

    ): IsOkModel<String> {

        if (isConsoleMode && isDevelopmentMode) {

            println("currentUser : $currentUserName")
        }

        val multipleTransactionDataSource = MultipleTransactionDataSource()

        if (isNotApiCall && isDevelopmentMode) {

            println("Contacting Server...")
        }

        val apiResponse: Result<MultipleTransactionResponse>

        val userInitialTransactionDateFromUsernameResult: IsOkModel<LocalDate> =
            DataOperations.getUserInitialTransactionDateFromUsername(username = currentUserName)

        if (userInitialTransactionDateFromUsernameResult.isOK) {

            //TODO : Only applicable for user_first_entry_date usernames
            val specifiedDate: IsOkModel<String> = MysqlUtils.normalDateTextToMySqlDateText(

                //TODO : migrate to DateTimeUtils
                normalDateText = DataOperations.getUserInitialTransactionDateFromUsername(username = currentUserName).data!!
                    .minusDays(
                        /* daysToSubtract = */ 1
                    ).format(DateTimeUtils.normalDatePattern)
            )
            if (specifiedDate.isOK) {

                //TODO : migrate to ApiUtilsCommon
                runBlocking {

                    apiResponse = multipleTransactionDataSource.selectUserTransactionsAfterSpecifiedDate(

                        userId = currentUserId,
                        specifiedDate = specifiedDate.data!!
                    )
                }

                // println("Response : $apiResponse2")
                if (apiResponse.isFailure) {

                    if (isNotApiCall) {

                        println("Error : ${(apiResponse.exceptionOrNull() as Exception).localizedMessage}")
                        do {

                            print("Retry (Y/N) ? : ")
                            when (readln()) {

                                "Y", "" -> {

                                    return generateSheetOfUser(

                                        currentUserName = currentUserName,
                                        currentUserId = currentUserId,
                                        getDesiredAccountIdsForSheetOfUser = getDesiredAccountIdsForSheetOfUser,
                                        isConsoleMode = isConsoleMode,
                                        isDevelopmentMode = isDevelopmentMode
                                    )
                                }

                                "N" -> {

                                    return IsOkModel(

                                        isOK = false,
                                        error = ConstantsCommon.USER_CANCELED_MESSAGE
                                    )
                                }

                                else -> ErrorUtilsInteractive.printInvalidOptionMessage()
                            }
                        } while (true)

                    } else {

                        return IsOkModel(

                            isOK = false,
                            error = Json.encodeToString(

                                serializer = CommonDataModel.serializer(Unit.serializer()),
                                value = CommonDataModel(

                                    status = 1,
                                    error = "Error : ${(apiResponse.exceptionOrNull() as Exception).localizedMessage}"
                                )
                            )
                        )
                    }
                } else {

                    val selectUserTransactionsAfterSpecifiedDateResult: MultipleTransactionResponse =
                        apiResponse.getOrNull()!!
                    // TODO : migrate to ApiUtils
                    if (selectUserTransactionsAfterSpecifiedDateResult.status == 1u) {

                        return IsOkModel(

                            isOK = false,
                            error = Json.encodeToString(

                                serializer = CommonDataModel.serializer(Unit.serializer()),
                                value = CommonDataModel(

                                    status = 2,
                                    error = "No Transactions"
                                )
                            )
                        )
                    } else {

                        val getDesiredAccountIdsForSheetOfUserResult = getDesiredAccountIdsForSheetOfUser(
                            selectUserTransactionsAfterSpecifiedDateResult
                        )
                        if (getDesiredAccountIdsForSheetOfUserResult.isOK) {

                            val accounts: MutableMap<UInt, String> = getDesiredAccountIdsForSheetOfUserResult.data!!

                            val sheetDataRows: MutableList<BalanceSheetDataRowModel> = mutableListOf()
                            for (account: MutableMap.MutableEntry<UInt, String> in accounts) {

                                //TODO : migrate to ApiUtilsCommon
                                val apiResponse2: Result<MultipleTransactionResponse> =
                                    ServerOperations.getUserTransactionsForAnAccount(

                                        userId = currentUserId,
                                        accountId = account.key,
                                        isNotFromBalanceSheet = false,
                                        isDevelopmentMode = isDevelopmentMode
                                    )
                                if (apiResponse2.isFailure) {

                                    if (isNotApiCall) {

                                        println("Error : ${(apiResponse2.exceptionOrNull() as Exception).localizedMessage}")
//                            do {
//                                print("Retry (Y/N) ? : ")
//                                val input: String = readLine()!!
//                                when (input) {
//                                    "Y", "" -> {
//                                    }
//
//                                    "N" -> {}
//                                    else -> invalidOptionMessage()
//                                }
//                            } while (input != "N")
                                    } else {

                                        return IsOkModel(

                                            isOK = false,
                                            error = Json.encodeToString(

                                                serializer = CommonDataModel.serializer(Unit.serializer()),
                                                value = CommonDataModel(

                                                    status = 1,
                                                    error = "Error : ${(apiResponse2.exceptionOrNull() as Exception).localizedMessage}"
                                                )
                                            )
                                        )
                                    }
                                } else {

                                    val userMultipleTransactionResponseResult: MultipleTransactionResponse =
                                        apiResponse2.getOrNull()!!
                                    if (userMultipleTransactionResponseResult.status == 0u) {

                                        var currentBalance = 0.0F
                                        userMultipleTransactionResponseResult.transactions.forEach { currentTransaction: TransactionResponse ->

                                            if (currentTransaction.fromAccountId == account.key) {

                                                currentBalance -= currentTransaction.amount

                                            } else {

                                                currentBalance += currentTransaction.amount
                                            }
                                        }
                                        if (currentBalance != 0.0F) {

                                            sheetDataRows.add(

                                                element = BalanceSheetDataRowModel(

                                                    accountId = account.key,
                                                    accountName = account.value,
                                                    accountBalance = currentBalance
                                                )
                                            )
                                        }
                                    } else {

                                        return IsOkModel(

                                            isOK = false,
                                            error = Json.encodeToString(

                                                serializer = CommonDataModel.serializer(Unit.serializer()),
                                                value = CommonDataModel(

                                                    status = 1,
                                                    error = "Server Execution Error, Execution Status is ${userMultipleTransactionResponseResult.status}"
                                                )
                                            )
                                        )
                                    }
                                }
                            }

                            //TODO : print Formatted Sheet on Console
                            return IsOkModel(

                                isOK = true,
                                data = Json.encodeToString(

                                    serializer = CommonDataModel.serializer(BalanceSheetDataRowModel.serializer()),
                                    value = CommonDataModel(

                                        status = 0,
                                        data = sheetDataRows.sortedBy { balanceSheetDataRow: BalanceSheetDataRowModel ->

                                            balanceSheetDataRow.accountId
                                        }
                                    )
                                )
                            )
                        } else {

                            return IsOkModel(

                                isOK = false,
                                error = getDesiredAccountIdsForSheetOfUserResult.error
                            )
                        }

                    }
                }
            } else {

                return IsOkModel(

                    isOK = false,
                    error = Json.encodeToString(

                        serializer = CommonDataModel.serializer(Unit.serializer()),
                        value = CommonDataModel(

                            status = 1,
                            error = "Error : ${specifiedDate.error!!}"
                        )
                    )
                )
            }
        } else {
            return IsOkModel(

                isOK = false,
                error = Json.encodeToString(

                    serializer = CommonDataModel.serializer(Unit.serializer()),
                    value = CommonDataModel(

                        status = 1,
                        error = ConstantsNative.DATE_FROM_USERNAME_ERROR
                    )
                )
            )
        }
    }

    private fun getDesiredAccountIdsForBalanceSheetOfUser(

        refineLevel: BalanceSheetRefineLevelEnum,
        selectUserTransactionsAfterSpecifiedDateResult: MultipleTransactionResponse,
        isDevelopmentMode: Boolean,
        dotEnv: Dotenv

    ): IsOkModel<MutableMap<UInt, String>> {

        //TODO : Check duplication of account ids

        var accountsToExclude: List<String> = emptyList()

        when (refineLevel) {

            BalanceSheetRefineLevelEnum.WITHOUT_OPEN_BALANCES -> {

                // TODO : Change to new api methods
                // TODO : Change to EnvironmentFileEntryEnum
                accountsToExclude = (dotEnv["OPEN_BALANCE_ACCOUNT_IDS"] ?: "0").split(',')
            }

            BalanceSheetRefineLevelEnum.WITHOUT_MISC_INCOMES -> {

                accountsToExclude = (dotEnv["OPEN_BALANCE_ACCOUNT_IDS"]
                    ?: "0").split(',') + (dotEnv["MISC_INCOME_ACCOUNT_IDS"]
                    ?: "0").split(',')
            }

            BalanceSheetRefineLevelEnum.WITHOUT_INVESTMENT_RETURNS -> {

                accountsToExclude =
                    (dotEnv["OPEN_BALANCE_ACCOUNT_IDS"]
                        ?: "0").split(',') + (dotEnv["MISC_INCOME_ACCOUNT_IDS"]
                        ?: "0").split(',') + (dotEnv["INVESTMENT_RETURNS_ACCOUNT_IDS"]
                        ?: "0").split(',')
            }

            BalanceSheetRefineLevelEnum.WITHOUT_FAMILY_ACCOUNTS -> {

                accountsToExclude =
                    (dotEnv["OPEN_BALANCE_ACCOUNT_IDS"]
                        ?: "0").split(',') + (dotEnv["MISC_INCOME_ACCOUNT_IDS"]
                        ?: "0").split(',') + (dotEnv["INVESTMENT_RETURNS_ACCOUNT_IDS"]
                        ?: "0").split(',') + (dotEnv["FAMILY_ACCOUNT_IDS"]
                        ?: "0").split(',')
            }

            BalanceSheetRefineLevelEnum.WITHOUT_EXPENSE_ACCOUNTS -> {

                accountsToExclude =
                    (dotEnv["OPEN_BALANCE_ACCOUNT_IDS"]
                        ?: "0").split(',') + (dotEnv["MISC_INCOME_ACCOUNT_IDS"]
                        ?: "0").split(',') + (dotEnv["INVESTMENT_RETURNS_ACCOUNT_IDS"]
                        ?: "0").split(',') + (dotEnv["FAMILY_ACCOUNT_IDS"]
                        ?: "0").split(',') + (dotEnv["EXPENSE_ACCOUNT_IDS"]
                        ?: "0").split(',')
            }

            BalanceSheetRefineLevelEnum.ALL -> {}
        }
        val accounts: MutableMap<UInt, String> = mutableMapOf()
        selectUserTransactionsAfterSpecifiedDateResult.transactions.forEach { transaction: TransactionResponse ->

            if (!accountsToExclude.contains(transaction.fromAccountId.toString())) {

                accounts.putIfAbsent(transaction.fromAccountId, transaction.fromAccountFullName)
            }

            if (!accountsToExclude.contains(transaction.toAccountId.toString())) {

                accounts.putIfAbsent(transaction.toAccountId, transaction.toAccountFullName)
            }
        }

        if (isDevelopmentMode) {

            println("Affected A/Cs : $accounts")
        }

        return IsOkModel(

            isOK = true,
            data = accounts
        )
    }

    private fun getDesiredAccountIdsForSheetOfUserBasedOnEnvironment(

        environmentVariable: EnvironmentFileEntryEnum,
        selectUserTransactionsAfterSpecifiedDateResult: MultipleTransactionResponse,
        isDevelopmentMode: Boolean,
        environmentVariablesForAccountsToIgnore: List<EnvironmentFileEntryEnum>,
        dotEnv: Dotenv

    ): IsOkModel<MutableMap<UInt, String>> {

        //TODO : Check duplication of account ids

        val accountsToInclude: List<String> = (dotEnv[environmentVariable.name] ?: "0").split(',')

        val accountsToIgnore: List<String> = environmentVariablesForAccountsToIgnore
            .flatMap { environmentVariableForAccountToIgnore ->
                dotEnv[environmentVariableForAccountToIgnore.name]?.split(',') ?: emptyList()
            }

        val accounts: MutableMap<UInt, String> = mutableMapOf()
        selectUserTransactionsAfterSpecifiedDateResult.transactions.forEach { transaction: TransactionResponse ->

            fun validateAccountDetails(accountId: UInt, accountName: String): IsOkModel<MutableMap<UInt, String>>? {

                return if (accountsToInclude.contains(accountId.toString())) {

                    accounts.putIfAbsent(accountId, accountName)
                    null

                } else if (accountsToIgnore.contains(accountId.toString())) {

                    null

                } else {

                    IsOkModel(
                        isOK = false,
                        error = "Account $accountId : $accountName not available in configuration"
                    )
                }
            }

            val validateAccountResult =
                validateAccountDetails(transaction.fromAccountId, transaction.fromAccountFullName)
                    ?: validateAccountDetails(transaction.toAccountId, transaction.toAccountFullName)
                    ?: IsOkModel(isOK = true)

            if (IsOkUtils.isNotOk(validateAccountResult)) {

                return validateAccountResult
            }
        }

        if (isDevelopmentMode) {

            println("Affected Expense A/Cs : $accounts")
        }

        return IsOkModel(

            isOK = true,
            data = accounts
        )
    }
}
