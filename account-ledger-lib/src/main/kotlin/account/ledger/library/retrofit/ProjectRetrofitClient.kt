package account.ledger.library.retrofit

import account.ledger.library.api.Api
import account.ledger.library.api.ApiConstants
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal object ProjectRetrofitClient {

    internal val retrofitClient: Api by lazy {

        Retrofit.Builder()
            .baseUrl(ApiConstants.serverApiAddress)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(Api::class.java)
    }
}
