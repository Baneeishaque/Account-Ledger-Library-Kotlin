package accountLedgerCli.retrofit.data

import account.ledger.library.api.Api
import accountLedgerCli.retrofit.ProjectRetrofitClient

internal open class AppDataSource<T : Any>(val retrofitClient: Api = ProjectRetrofitClient.retrofitClient) :
    CommonDataSource<T>()