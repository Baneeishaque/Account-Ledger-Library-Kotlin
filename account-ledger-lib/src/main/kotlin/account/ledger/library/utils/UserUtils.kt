package account.ledger.library.utils

import account.ledger.library.api.response.UserResponse
import account.ledger.library.models.ChooseUserResult
import account_ledger_library.constants.ConstantsNative

object UserUtils {

    @JvmStatic
    fun prepareUsersMap(

        users: List<UserResponse>

    ): LinkedHashMap<UInt, UserResponse> {

        val usersMap = LinkedHashMap<UInt, UserResponse>()
        users.forEach { currentUser: UserResponse -> usersMap[currentUser.id] = currentUser }
//        return usersMap.toSortedMap()
        return usersMap
    }

    @JvmStatic
    fun usersToTextFromLinkedHashMap(

        usersMap: LinkedHashMap<UInt, UserResponse>

    ): String {

        var result = ""
        usersMap.forEach { user: Map.Entry<UInt, UserResponse> -> result += "${ConstantsNative.USER_TEXT.first()}${user.key} - ${user.value.username}\n" }
        return result
    }

    @JvmStatic
    fun handleUserSelection(

        chosenUserId: UInt,
        usersMap: LinkedHashMap<UInt, UserResponse>

    ): ChooseUserResult {

        if (chosenUserId != 0u) {

            return ChooseUserResult(
                isChosen = true,
                chosenUser = usersMap[chosenUserId]!!
            )
        }
        return ChooseUserResult(isChosen = false)
    }
}
