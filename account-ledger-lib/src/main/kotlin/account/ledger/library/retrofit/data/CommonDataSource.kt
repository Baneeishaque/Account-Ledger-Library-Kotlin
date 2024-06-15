package account.ledger.library.retrofit.data

import retrofit2.Response

open class CommonDataSource<T : Any> {

    fun processApiResponse(apiResponse: Response<T>): Result<T> {

        if (apiResponse.isSuccessful) {

            val apiResponseBody: T? = apiResponse.body()
            return if (apiResponseBody == null) {

                Result.failure(exception = Exception("Response Body is Null"))

            } else {

                Result.success(value = apiResponseBody)
            }
        }
        return Result.failure(exception = Exception("Exception Code - ${apiResponse.code()}, Message - ${apiResponse.message()}"))
    }
}
