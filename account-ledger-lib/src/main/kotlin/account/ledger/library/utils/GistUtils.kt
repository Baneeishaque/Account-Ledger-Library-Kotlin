package account.ledger.library.utils

import account.ledger.library.constants.Constants
import account.ledger.library.models.AccountLedgerGistDateLedgerModel
import account.ledger.library.models.AccountLedgerGistModel
import account.ledger.library.models.AccountLedgerGistTransactionModel
import common.utils.library.utils.DateTimeUtils
import common.utils.library.utils.GistUtils
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.format.DateTimeParseException

object GistUtils {

    @JvmStatic
    fun processGistId(

        userName: String,
        gitHubAccessToken: String,
        gistId: String,
        isDevelopmentMode: Boolean,
        isApiCall: Boolean = true

    ): AccountLedgerGistModel {

        val accountLedgerGist = AccountLedgerGistModel(userName = userName, accountLedgerIds = LinkedHashMap())
        runBlocking {

            GistUtils.getHttpClientForGitHub(

                accessToken = gitHubAccessToken,
                isDevelopmentMode = isDevelopmentMode

            ).use { client ->

                // TODO: inline use of serialization : for file name in gist
                val gistContent: String = GistUtils.getGistContents(

                    client = client,
                    gistId = gistId,
                    isDevelopmentMode = isDevelopmentMode

                ).files.mainTxt.content

                val gistContentLines: List<String> = gistContent.lines()

                var currentAccountId = 0u
                var extractedLedger: LinkedHashMap<UInt, MutableList<String>> = LinkedHashMap()

                gistContentLines.forEach { line: String ->

                    if (line.contains(other = Constants.accountHeaderIdentifier)) {

                        val accountName = line.replace(
                            regex = Constants.accountHeaderIdentifier.toRegex(),
                            replacement = ""
                        ).trim()

                        if (accountName == Constants.walletAccountHeaderIdentifier) {

//                            TODO : set walletAccountId from environment variable
                            currentAccountId = 6u
                        }
//                        TODO : check for custom bank name
                        else if (accountName == Constants.bankAccountHeaderIdentifier) {

//                            TODO : set bankAccountId from environment variable
                            currentAccountId = 11u
                        }

                    } else {

                        if (line.isNotEmpty() &&
                            (!line.contains(other = Constants.accountHeaderUnderlineCharacter)) &&
                            (!line.contains(other = Constants.accountBalanceHolderOpeningBrace))
                        ) {
                            extractedLedger = TextAccountLedgerUtils.addLineToCurrentAccountLedger(
                                ledgerToProcess = extractedLedger,
                                desiredAccountId = currentAccountId,
                                desiredLine = line
                            )
                        }
                    }
                }

                extractedLedger.forEach { (localCurrentAccountId: UInt, currentAccountLedgerLines: List<String>) ->

                    accountLedgerGist.accountLedgerIds[localCurrentAccountId] = LinkedHashMap()

                    var isNextLineFinalBalance = false
                    var previousDate: LocalDate = LocalDate.now()
                    currentAccountLedgerLines.forEach { ledgerLine: String ->

                        if (ledgerLine.first() != Constants.dateUnderlineCharacter) {

                            if (ledgerLine.contains(Constants.finalBalancePrefixCharacter)) {

                                isNextLineFinalBalance = true

                            } else if (isNextLineFinalBalance) {

                                val finalBalance: Double = ledgerLine.trim().toDouble()
                                val transactionDateAsText: String = previousDate.format(DateTimeUtils.normalDatePattern)

                                accountLedgerGist.accountLedgerIds[localCurrentAccountId]!![transactionDateAsText]!!.finalBalanceOnDate =
                                    finalBalance

                            } else {

                                val ledgerLineContents: List<String> = ledgerLine.split(" ", limit = 2)
                                val dateOrAmount: String = ledgerLineContents.first()
                                try {
                                    val transactionDate: LocalDate =
                                        LocalDate.parse(dateOrAmount, DateTimeUtils.normalDatePattern)
                                    // TODO : A/C Ledger Name in extracted ledger
                                    val accountLedgerName = "Wallet"
                                    val initialBalanceOnDate: Double =
                                        ledgerLineContents[1].replace(
                                            regex = accountLedgerName.toRegex(),
                                            replacement = ""
                                        ).trim().toDouble()

                                    val transactionDateAsText: String =
                                        transactionDate.format(DateTimeUtils.normalDatePattern)

                                    accountLedgerGist.accountLedgerIds[localCurrentAccountId]!![transactionDateAsText] =
                                        AccountLedgerGistDateLedgerModel(
                                            initialBalanceOnDate = initialBalanceOnDate,
                                            transactionsOnDate = mutableListOf()
                                        )

                                    previousDate = transactionDate

                                } catch (_: DateTimeParseException) {

                                    val transactionAmount: Double = dateOrAmount.toDouble()
                                    val transactionParticulars: String = ledgerLineContents[1]

                                    val transactionDateAsText: String =
                                        previousDate.format(DateTimeUtils.normalDatePattern)

                                    accountLedgerGist.accountLedgerIds[localCurrentAccountId]!![transactionDateAsText]!!.transactionsOnDate.add(
                                        AccountLedgerGistTransactionModel(
                                            transactionParticulars = transactionParticulars,
                                            transactionAmount = transactionAmount
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                if (isApiCall) {
                    print(
                        Json.encodeToString(
                            serializer = AccountLedgerGistModel.serializer(),
                            value = accountLedgerGist
                        )
                    )
                }
            }
        }
        return accountLedgerGist
    }
}