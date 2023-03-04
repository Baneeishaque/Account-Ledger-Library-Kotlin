package account.ledger.library.utils;

object TextAccountLedgerUtils {

    @JvmStatic
    fun addLineToCurrentAccountLedger(
        ledgerToProcess: LinkedHashMap<UInt, MutableList<String>>,
        desiredAccountId: UInt,
        desiredLine: String
    ) {
        val currentAccountLedgerLines: MutableList<String> =
            ledgerToProcess.getOrDefault(key = desiredAccountId, defaultValue = mutableListOf())
        currentAccountLedgerLines.add(element = desiredLine)
        ledgerToProcess[desiredAccountId] = currentAccountLedgerLines
    }
}
