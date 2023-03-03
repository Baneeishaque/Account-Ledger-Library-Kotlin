package account.ledger.library.retrofit.data

import account.ledger.library.api.Api
import account.ledger.library.retrofit.ProjectRetrofitClient

open class AppDataSource<T : Any>(val retrofitClient: Api = ProjectRetrofitClient.retrofitClient) :
    CommonDataSource<T>()