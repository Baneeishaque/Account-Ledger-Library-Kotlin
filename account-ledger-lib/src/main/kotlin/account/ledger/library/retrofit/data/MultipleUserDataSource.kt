package account.ledger.library.retrofit.data

import account.ledger.library.api.response.MultipleUserResponse
import account.ledger.library.retrofit.ProjectRetrofitClient
import account.ledger.library.retrofit.ResponseHolder
import retrofit2.Response
import java.io.IOException

class MultipleUserDataSource {

    private val retrofitClient = ProjectRetrofitClient.retrofitClient

    suspend fun selectUsers(): ResponseHolder<MultipleUserResponse> {
        return try {

            processApiResponse(retrofitClient.selectUsers())

        } catch (exception: Exception) {

            ResponseHolder.Error(Exception("Exception - ${exception.localizedMessage}"))
        }
    }
}

//    TODO : Rewrite as general function for all responses
private fun processApiResponse(apiResponse: Response<MultipleUserResponse>): ResponseHolder<MultipleUserResponse> {

    if (apiResponse.isSuccessful) {

        val usersApiResponseBody = apiResponse.body()
        return if (usersApiResponseBody != null) {

            ResponseHolder.Success(usersApiResponseBody)

        } else {

            ResponseHolder.Error(Exception("Invalid Response Body - $usersApiResponseBody"))
        }
    }
    return ResponseHolder.Error(IOException("Exception Code - ${apiResponse.code()}, Message - ${apiResponse.message()}"))
}
