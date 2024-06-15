package account.ledger.library.utils

import account.ledger.library.api.response.AccountResponse
import account.ledger.library.enums.BajajDiscountTypeEnum
import account_ledger_library.enums.BajajRewardTypeEnum
import account.ledger.library.enums.EnvironmentFileEntryEnum
import account.ledger.library.enums.TransactionTypeEnum
import account.ledger.library.models.TransactionModel
import account_ledger_library.constants.ConstantsNative
import common.utils.library.constants.ConstantsCommon
import common.utils.library.models.EnvironmentFileEntryStrictModel
import common.utils.library.models.IsOkModel
import common.utils.library.models.SuccessBasedOnIsOkModel
import common.utils.library.utils.*
import io.github.cdimascio.dotenv.Dotenv
import java.time.LocalDateTime

object TransactionForBajajUtils {

    @JvmStatic
    val bajajTransactionTypes: List<TransactionTypeEnum> =
        TransactionForBajajCoinsUtils.bajajCoinTransactionTypes + TransactionForBajajCashbackUtils.bajajCashbackTransactionTypes

    @JvmStatic
    val bajajTransactionTypesForUpToDiscountType: List<TransactionTypeEnum> =
        bajajTransactionTypes.filter { transactionTypeEnum: TransactionTypeEnum ->

            transactionTypeEnum.name.contains(other = "_UP_TO")
        }

    @JvmStatic
    fun prepareTransactionsBase(

        isSourceTransactionPresent: Boolean,
        sourceAccount: AccountResponse,
        secondPartyAccount: AccountResponse,
        rewardIncomeAccount: AccountResponse,
        rewardCollectionAccount: AccountResponse,
        amountToSpendForReward: UInt,
        perTransactionAmountForReward: UInt,
        totalNumberOfTransactionsForRewards: UInt,
        listOfRewardingTransactionIndexes: List<UInt>,
        listOfRewards: List<UInt>,
        eventDateTimeInText: String,
        calculateTransactionAmount: (UInt) -> Float,
        generateTransactionParticularsSuffix: (UInt) -> String,
        rewardSpecifier: BajajRewardTypeEnum,
        discountType: BajajDiscountTypeEnum,
        upToValue: UInt?

    ): List<TransactionModel> {

        val transactions: MutableList<TransactionModel> = mutableListOf()
        var localEventDateTimeInText: String = eventDateTimeInText

        val rewardsSum: UInt = listOfRewards.sum()
        val listOfRewardingTransactionIndexesInText: String =
            listOfRewardingTransactionIndexes.joinToString(separator = ", ")
        val listOfRewardsInText: String = listOfRewards.joinToString(separator = ", ")

        val rewardDescription =
            "${discountType.name} ${upToValue ?: rewardsSum} ${rewardSpecifier.value} on ${perTransactionAmountForReward}x$totalNumberOfTransactionsForRewards, Receive on $listOfRewardingTransactionIndexesInText Transactions"

        if (isSourceTransactionPresent) {

            transactions.add(

                element = TransactionModel(

                    fromAccount = secondPartyAccount,
                    toAccount = sourceAccount,
                    amount = amountToSpendForReward.toFloat(),
                    particulars = "To Spend for Bajaj Finserv UPI Transactions to get rewards - $rewardDescription",
                    eventDateTimeInText = localEventDateTimeInText
                )
            )
        }

        // Prepare the next totalNumberOfTransactionsForRewards transactions
        for (i: Int in 0 until totalNumberOfTransactionsForRewards.toInt()) {

            localEventDateTimeInText =
                DateTimeUtils.add5MinutesToNormalDateTimeInText(dateTimeInText = localEventDateTimeInText)

            transactions.add(
                element = TransactionModel(

                    fromAccount = sourceAccount,
                    toAccount = secondPartyAccount,
                    amount = perTransactionAmountForReward.toFloat(),
                    particulars = "To ${secondPartyAccount.name} for Bajaj Finserv UPI Reward - $rewardDescription - ${i + 1}th",
                    eventDateTimeInText = localEventDateTimeInText
                )
            )

            // Check the current transaction index is one of the rewarding transactions.
            if (listOfRewardingTransactionIndexes.contains((i + 1).toUInt())) {

                localEventDateTimeInText = DateTimeUtils.add5MinutesToNormalDateTimeInText(localEventDateTimeInText)
                val reward: UInt =
                    listOfRewards[listOfRewardingTransactionIndexes.indexOf((i + 1).toUInt())]

                transactions.add(

                    element = TransactionModel(

                        fromAccount = rewardIncomeAccount,
                        toAccount = rewardCollectionAccount,
                        amount = calculateTransactionAmount.invoke(reward),
                        particulars = "Bajaj Finserv UPI Reward: ${sourceAccount.name} -> ${secondPartyAccount.name} => ${perTransactionAmountForReward}x$totalNumberOfTransactionsForRewards times, ${discountType.name} ${upToValue ?: rewardsSum} ${rewardSpecifier.value} as $listOfRewardsInText on $listOfRewardingTransactionIndexesInText transactions${
                            generateTransactionParticularsSuffix.invoke(
                                reward
                            )
                        }",
                        eventDateTimeInText = localEventDateTimeInText
                    )
                )
            }
        }
        return transactions
    }

    fun generateTransactionsForRewardBase(

        isFundingTransactionPresent: Boolean,
        sourceAccount: AccountResponse,
        secondPartyAccount: AccountResponse,
        eventDateTimeInText: String,
        dotEnv: Dotenv,
        userId: UInt,
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean,
        isBalanceCheckByPassed: Boolean,
        rewardIncomeAccountIdEnvironmentVariable: EnvironmentFileEntryStrictModel<EnvironmentFileEntryEnum>,
        rewardCollectionAccountIdEnvironmentVariable: EnvironmentFileEntryStrictModel<EnvironmentFileEntryEnum>,
        rewardType: BajajRewardTypeEnum,
        discountType: BajajDiscountTypeEnum,
        upToValue: UInt?

    ): IsOkModel<List<TransactionModel>> {

        val result = IsOkModel<List<TransactionModel>>(isOK = false)

        var confirmDataResult: IsOkModel<UInt> =
            EnvironmentFileOperationsInteractive.confirmWholeNumberEnvironmentVariableDataOrBackWithPrompt(

                dotEnv = dotEnv,
                environmentVariableName = rewardIncomeAccountIdEnvironmentVariable.entry.name,
                dataSpecification = rewardIncomeAccountIdEnvironmentVariable.formalName
            )

        if (confirmDataResult.isOK) {

            AccountUtilsInteractive.processUserAccountsMap(

                userId = userId,
                isConsoleMode = isConsoleMode,
                isDevelopmentMode = isDevelopmentMode,
                successActions = fun(userAccountsMap: LinkedHashMap<UInt, AccountResponse>) {

                    val getValidRewardIncomeAccountResult: IsOkModel<AccountResponse> =
                        AccountUtils.getValidAccountById(

                            desiredAccount = userAccountsMap[confirmDataResult.data!!],
                            userAccountsMap = userAccountsMap,
                            idCorrectionFunction = fun(): IsOkModel<UInt> {

                                return InputUtilsInteractive.getValidUnsignedIntOrBackWithPrompt(

                                    dataSpecification = rewardIncomeAccountIdEnvironmentVariable.formalName,
                                )
                            }
                        )
                    if (getValidRewardIncomeAccountResult.isOK) {

                        confirmDataResult =
                            EnvironmentFileOperationsInteractive.confirmWholeNumberEnvironmentVariableDataOrBackWithPrompt(

                                dotEnv = dotEnv,
                                environmentVariableName = rewardCollectionAccountIdEnvironmentVariable.entry.name,
                                dataSpecification = rewardCollectionAccountIdEnvironmentVariable.formalName
                            )

                        if (confirmDataResult.isOK) {

                            val getValidRewardCollectionAccountResult: IsOkModel<AccountResponse> =
                                AccountUtils.getValidAccountById(

                                    desiredAccount = userAccountsMap[confirmDataResult.data!!],
                                    userAccountsMap = userAccountsMap,
                                    idCorrectionFunction = fun(): IsOkModel<UInt> {

                                        return InputUtilsInteractive.getValidUnsignedIntOrBackWithPrompt(

                                            dataSpecification = rewardCollectionAccountIdEnvironmentVariable.formalName,
                                        )
                                    }
                                )

                            if (getValidRewardCollectionAccountResult.isOK) {

                                var bajajCoinConversionRate: UInt? = null
                                if (rewardType == BajajRewardTypeEnum.COINS) {

                                    confirmDataResult =
                                        EnvironmentFileOperationsInteractive.confirmWholeNumberEnvironmentVariableDataOrBackWithPrompt(

                                            dotEnv = dotEnv,
                                            environmentVariableName = EnvironmentFileEntryEnum.BAJAJ_COINS_CONVERSION_RATE.name,
                                            dataSpecification = ConstantsNative.BAJAJ_COINS_CONVERSION_RATE_TEXT
                                        )
                                    if (confirmDataResult.isOK) {

                                        bajajCoinConversionRate = confirmDataResult.data!!
                                    }
                                }

                                if (confirmDataResult.isOK) {

                                    val amountToSpendForRewardText =
                                        "Amount To Spend for Bajaj ${rewardType.value} Rewarding Transactions"

                                    val amountToSpendForRewardResult: IsOkModel<UInt> =
                                        if (isFundingTransactionPresent) {

                                            InputUtilsInteractive.getValidUnsignedIntOrBackWithPrompt(

                                                dataSpecification = amountToSpendForRewardText,
                                            )
                                        } else {

                                            SuccessBasedOnIsOkModel(

                                                ownData = 0u
                                            )
                                        }

                                    if (amountToSpendForRewardResult.isOK) {

                                        val getDataResult: IsOkModel<List<UInt>> =
                                            IsOkUtils.checkListOfOkModels(

                                                isOkModels = listOf(

                                                    InputUtilsInteractive.getValidUnsignedIntOrBackWithPrompt(

                                                        dataSpecification = "Amount of each Transaction for Bajaj ${rewardType.value} Rewards",
                                                    ),
                                                    InputUtilsInteractive.getValidUnsignedIntOrBackWithPrompt(

                                                        dataSpecification = "Count of Bajaj ${rewardType.value} Rewarding Transactions",
                                                    )
                                                )
                                            )
                                        if (getDataResult.isOK) {

                                            val countOfRewardingTransactions: UInt = getDataResult.data!!.last()
                                            val getDataLists: IsOkModel<List<List<UInt>>> =
                                                IsOkUtils.checkListOfOkModels(

                                                    isOkModels = listOf(

                                                        InputUtilsInteractive.getMultipleValidUnsignedIntOrBackWithPrompt(

                                                            dataSpecification = "List of Bajaj ${rewardType.value} Rewarding Transactions",
                                                            count = countOfRewardingTransactions
                                                        ),
                                                        InputUtilsInteractive.getMultipleValidUnsignedIntOrBackWithPrompt(

                                                            dataSpecification = "List of ${rewardType.value} Rewards",
                                                            count = countOfRewardingTransactions
                                                        )
                                                    )
                                                )

                                            if (getDataLists.isOK) {

                                                val listOfRewardingTransactionIndexes: List<UInt> =
                                                    getDataLists.data!!.first()
                                                val totalNumberOfTransactionsForReward: UInt =
                                                    listOfRewardingTransactionIndexes.last()

                                                val fundingPartyAccountBalance: IsOkModel<Float> =
                                                    if (isFundingTransactionPresent) {
                                                        AccountUtilsInteractive.getAccountBalance(

                                                            userId = userId,
                                                            desiredAccountId = secondPartyAccount.id,
                                                            isDevelopmentMode = isDevelopmentMode
                                                        )
                                                    } else {

                                                        SuccessBasedOnIsOkModel(

                                                            ownData = 0F
                                                        )
                                                    }

                                                if (fundingPartyAccountBalance.isOK) {

                                                    val eventDateTime: IsOkModel<LocalDateTime> =
                                                        DateTimeUtils.normalDateTimeInTextToDateTime(

                                                            normalDateTimeInText = eventDateTimeInText
                                                        )

                                                    if (eventDateTime.isOK) {

                                                        val getAccountBalancesResult: IsOkModel<List<Float>> =
                                                            IsOkUtils.checkListOfOkModels(

                                                                isOkModels = listOf(

                                                                    AccountUtilsInteractive.getAccountBalance(

                                                                        userId = userId,
                                                                        desiredAccountId = sourceAccount.id,
                                                                        isDevelopmentMode = isDevelopmentMode
                                                                    ),
                                                                    AccountUtilsInteractive.getAccountBalance(

                                                                        userId = userId,
                                                                        desiredAccountId = getValidRewardCollectionAccountResult.data!!.id,
                                                                        isDevelopmentMode = isDevelopmentMode
                                                                    )
                                                                )
                                                            )

                                                        if (getAccountBalancesResult.isOK) {

                                                            val balanceOfRewardCollectionAccount: Float =
                                                                getAccountBalancesResult.data!!.last()

                                                            // Check if a/c 1 has at least amountToSpendForRewards.
                                                            val amountToSpendForReward: UInt =
                                                                amountToSpendForRewardResult.data!!

                                                            if (isFundingTransactionPresent && (fundingPartyAccountBalance.data!! < amountToSpendForReward.toFloat())) {

                                                                result.error =
                                                                    "Insufficient ${ConstantsNative.BALANCE_TEXT} in ${secondPartyAccount.name}. " +
                                                                            "Please ensure ${secondPartyAccount.name} has at least a ${ConstantsNative.BALANCE_TEXT} of $amountToSpendForReward."

                                                            } else {

                                                                // Check (totalNumberOfTransactionsForRewards * perTransactionAmountForReward) > amountToSpendForRewards.
                                                                // Check a/c 2 has ((totalNumberOfTransactionsForRewards * perTransactionAmountForReward) - amountToSpendRewards) amount as balance.
                                                                val perTransactionAmountForReward: UInt =
                                                                    getDataResult.data!!.first()

                                                                val totalAmountOfRewardTransactions: UInt =
                                                                    totalNumberOfTransactionsForReward * perTransactionAmountForReward
                                                                val effectiveTotalAmountOfRewardTransactions: Float =
                                                                    totalAmountOfRewardTransactions.toFloat() - amountToSpendForReward.toFloat()

                                                                val secondPartyAccountBalance: Float =
                                                                    getAccountBalancesResult.data!!.first()

                                                                if (isBalanceCheckByPassed || (secondPartyAccountBalance > effectiveTotalAmountOfRewardTransactions)) {

                                                                    // Prepare transactions
                                                                    val listOfRewards: List<UInt> =
                                                                        getDataLists.data!!.last()

                                                                    var transactions: List<TransactionModel> =
                                                                        emptyList()
                                                                    when (rewardType) {

                                                                        BajajRewardTypeEnum.CASHBACK -> {

                                                                            transactions =
                                                                                TransactionForBajajCashbackUtils.prepareTransactionsForCashback(

                                                                                    isSourceTransactionPresent = isFundingTransactionPresent,
                                                                                    sourceAccount = sourceAccount,
                                                                                    secondPartyAccount = secondPartyAccount,
                                                                                    rewardIncomeAccount = getValidRewardIncomeAccountResult.data!!,
                                                                                    rewardCollectionAccount = getValidRewardCollectionAccountResult.data!!,
                                                                                    amountToSpendForReward = amountToSpendForReward,
                                                                                    perTransactionAmountForReward = perTransactionAmountForReward,
                                                                                    totalNumberOfTransactionsForReward = totalNumberOfTransactionsForReward,
                                                                                    listOfRewardingTransactionIndexes = listOfRewardingTransactionIndexes,
                                                                                    listOfRewards = listOfRewards,
                                                                                    eventDateTimeInText = eventDateTimeInText,
                                                                                    discountType = discountType,
                                                                                    upToValue = upToValue
                                                                                )
                                                                        }

                                                                        BajajRewardTypeEnum.COINS -> {

                                                                            transactions =
                                                                                TransactionForBajajCoinsUtils.prepareTransactionsForCoins(

                                                                                    isSourceTransactionPresent = isFundingTransactionPresent,
                                                                                    sourceAccount = sourceAccount,
                                                                                    secondPartyAccount = secondPartyAccount,
                                                                                    rewardIncomeAccount = getValidRewardIncomeAccountResult.data!!,
                                                                                    rewardCollectionAccount = getValidRewardCollectionAccountResult.data!!,
                                                                                    amountToSpendForReward = amountToSpendForReward,
                                                                                    perTransactionAmountForReward = perTransactionAmountForReward,
                                                                                    totalNumberOfTransactionsForRewards = totalNumberOfTransactionsForReward,
                                                                                    listOfRewardingTransactionIndexes = listOfRewardingTransactionIndexes,
                                                                                    listOfRewards = listOfRewards,
                                                                                    coinsCollectionAccountBalance = balanceOfRewardCollectionAccount,
                                                                                    eventDateTimeInText = eventDateTimeInText,
                                                                                    coinConversionRate = bajajCoinConversionRate!!,
                                                                                    discountType = discountType,
                                                                                    upToValue = upToValue
                                                                                )
                                                                        }
                                                                    }

                                                                    if (isDevelopmentMode) {

                                                                        println("transactions = $transactions")
                                                                    }
                                                                    result.isOK = true
                                                                    result.data = transactions

                                                                } else {

                                                                    result.error =
                                                                        "Insufficient ${ConstantsNative.BALANCE_TEXT} in ${sourceAccount.name}. " +
                                                                                "Please ensure ${sourceAccount.name} has at least a ${ConstantsNative.BALANCE_TEXT} of ${effectiveTotalAmountOfRewardTransactions}."
                                                                }
                                                            }
                                                        } else {

                                                            if (getAccountBalancesResult.errorSpecifier != null) {

                                                                result.error =
                                                                    "${ConstantsNative.ACCOUNT_TEXT} ${ConstantsNative.BALANCE_CALCULATION_ERROR_TEXT} for ${ConstantsNative.ACCOUNT_TEXT} ID: ${getAccountBalancesResult.errorSpecifier}"

                                                            } else {

                                                                "${ConstantsNative.ACCOUNT_TEXT} ${ConstantsNative.BALANCE_CALCULATION_ERROR_TEXT}"
                                                            }
                                                        }
                                                    } else {

                                                        result.error =
                                                            DateTimeUtils.constructDateErrorMessage(message = eventDateTime.error!!)
                                                    }
                                                } else {

                                                    result.error =
                                                        "${ConstantsNative.ACCOUNT_TEXT} ${ConstantsNative.BALANCE_CALCULATION_ERROR_TEXT} for ${ConstantsNative.ACCOUNT_TEXT}: ${secondPartyAccount.name} [${secondPartyAccount.id}]"
                                                }
                                            } else {

                                                result.error =
                                                    ConstantsCommon.USER_CANCELED_MESSAGE
                                            }
                                        } else {

                                            result.error =
                                                ConstantsCommon.USER_CANCELED_MESSAGE
                                        }
                                    } else {

                                        result.error =
                                            ErrorUtils.generateInvalidInputMessage(inputSpecifier = amountToSpendForRewardText)
                                    }
                                } else {

                                    result.error =
                                        ErrorUtils.generateDataConfirmationErrorMessage(

                                            dataSpecifier = ConstantsNative.BAJAJ_COINS_CONVERSION_RATE_TEXT
                                        )
                                }
                            } else {

                                result.error =
                                    ErrorUtils.generateInvalidInputMessage(inputSpecifier = rewardCollectionAccountIdEnvironmentVariable.formalName)
                            }
                        } else {

                            result.error =
                                ErrorUtils.generateDataConfirmationErrorMessage(dataSpecifier = rewardCollectionAccountIdEnvironmentVariable.formalName)
                        }
                    } else {

                        result.error =
                            ErrorUtils.generateInvalidInputMessage(inputSpecifier = rewardIncomeAccountIdEnvironmentVariable.formalName)
                    }
                }
            )
        } else {

            result.error =
                ErrorUtils.generateDataConfirmationErrorMessage(dataSpecifier = rewardIncomeAccountIdEnvironmentVariable.formalName)
        }
        return result
    }
}
