package account.ledger.library.utils

import account.ledger.library.api.response.UserResponse
import account.ledger.library.constants.Constants
import account.ledger.library.models.UserCredentials

object UserUtils {

    @JvmStatic
    fun prepareUsersMap(users: List<UserResponse>): LinkedHashMap<UInt, UserResponse> {

        val usersMap = LinkedHashMap<UInt, UserResponse>()
        users.forEach { currentUser -> usersMap[currentUser.id] = currentUser }
//        return usersMap.toSortedMap()
        return usersMap
    }

    fun usersToStringFromLinkedHashMap(
        usersMap: LinkedHashMap<UInt, UserResponse>
    ): String {

        var result = ""
        usersMap.forEach { user -> result += "${Constants.userText.first()}${user.key} - ${user.value.username}\n" }
        return result
    }

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