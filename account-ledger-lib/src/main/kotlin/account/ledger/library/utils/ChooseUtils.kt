package account.ledger.library.utils

import account.ledger.library.models.ChooseByIdResult
import common.utils.library.models.IsOkModel
import java.util.*
import common.utils.library.utils.ApiUtilsCommon

object ChooseUtils {

    @JvmStatic
    fun <T> chooseById(

        itemSpecification: String,
        apiCallFunction: () -> Result<T>,
        prefixForPrompt: String = "",
        isConsoleMode: Boolean,
        isDevelopmentMode: Boolean

    ): ChooseByIdResult<T> {

        var idInput: UInt
        val reader = Scanner(System.`in`)

        while (true) {

            print("Enter $prefixForPrompt$itemSpecification ID or 0 to Back : ")

            try {

                idInput = reader.nextInt().toUInt()
                if (idInput == 0u) return ChooseByIdResult(isOkWithData = IsOkModel(isOK = false))

                return ChooseByIdResult(
                    isOkWithData = ApiUtilsCommon.makeApiRequestWithOptionalRetries(

                        apiCallFunction = apiCallFunction,
                        isConsoleMode = isConsoleMode,
                        isDevelopmentMode = isDevelopmentMode
                    ),
                    id = idInput
                )

            } catch (exception: InputMismatchException) {

                println("Invalid $itemSpecification ID...")
            }
        }
    }
}
