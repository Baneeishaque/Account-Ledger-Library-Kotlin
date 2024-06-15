package account.ledger.library.utils

import account.ledger.library.models.UserCredentials

object UserUtilsInteractive {

    @JvmStatic
    fun getUserCredentials(): UserCredentials {

        val user = UserCredentials(username = "", passcode = "")
        print("Enter Your Username : ")
        user.username = readlnOrNull().toString()
        print("Enter Your Password : ")
        user.passcode = readlnOrNull().toString()
        return user
    }
}
