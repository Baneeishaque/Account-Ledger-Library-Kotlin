package account.ledger.library.operations

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader

internal fun readCsv() {

    // @v Input file path from user
    // @cr a Hardcoded file path
    val filePath = "E:\\To_DK\\4356XXXXXXXXX854522-08-2020.xls"
    csvReader().open(filePath) {
        readAllAsSequence().forEach { row ->
            // Do something
            println(row) // [a, b, c]
        }
    }
}
