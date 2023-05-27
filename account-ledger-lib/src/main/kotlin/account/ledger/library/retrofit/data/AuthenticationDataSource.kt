package account.ledger.library.retrofit.data

import account.ledger.library.api.Api
import account.ledger.library.api.response.AuthenticationResponse
import account.ledger.library.retrofit.ProjectRetrofitClient
import account.ledger.library.retrofit.ResponseHolder
import retrofit2.Response
import java.io.IOException

class AuthenticationDataSource {

    private val retrofitClient: Api = ProjectRetrofitClient.retrofitClient

    suspend fun authenticateUser(
        username: String,
        password: String
    ): ResponseHolder<AuthenticationResponse> {
        return try {

            processApiResponse(retrofitClient.authenticateUser(username = username, password = password))

        } catch (exception: Exception) {

            ResponseHolder.Error(Exception("Exception - ${exception.localizedMessage}"))
        }
    }
}

//    TODO : Rewrite as general function for all responses
private fun processApiResponse(apiResponse: Response<AuthenticationResponse>): ResponseHolder<AuthenticationResponse> {

    if (apiResponse.isSuccessful) {

        val loginApiResponseBody = apiResponse.body()
        return if (loginApiResponseBody != null) {

            ResponseHolder.Success(loginApiResponseBody)

        } else {

            ResponseHolder.Error(Exception("Invalid Response Body - null"))
        }
    }
    return ResponseHolder.Error(IOException("Exception Code - ${apiResponse.code()}, Message - ${apiResponse.message()}"))
}
