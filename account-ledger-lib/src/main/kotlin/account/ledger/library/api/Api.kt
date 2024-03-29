package account.ledger.library.api

import account.ledger.library.api.response.AccountsResponse
import account.ledger.library.api.response.AuthenticationResponse
import account.ledger.library.api.response.TransactionManipulationResponse
import account.ledger.library.api.response.MultipleTransactionResponse
import account.ledger.library.api.response.MultipleUserResponse
import retrofit2.Response
import retrofit2.http.*

interface Api {

    @GET("${ApiConstants.selectUserMethod}.${ApiConstants.serverFileExtension}")
    suspend fun authenticateUser(
        @Query("username") username: String,
        @Query("password") password: String
    ): Response<AuthenticationResponse>

    @GET("${ApiConstants.selectUsersMethod}.${ApiConstants.serverFileExtension}")
    suspend fun selectUsers(): Response<MultipleUserResponse>

    @GET("${ApiConstants.selectUserAccountsV2Method}.${ApiConstants.serverFileExtension}")
    suspend fun selectUserAccounts(
        @Query("user_id") userId: UInt,
        @Query("parent_account_id") parentAccountId: UInt
    ): Response<AccountsResponse>

    @GET("${ApiConstants.selectUserAccountsFullMethod}.${ApiConstants.serverFileExtension}")
    suspend fun selectUserAccountsFull(@Query("user_id") userId: UInt): Response<AccountsResponse>

    @GET("${ApiConstants.selectUserTransactionsV2MMethod}.${ApiConstants.serverFileExtension}")
    suspend fun selectUserTransactionsV2M(
        @Query("user_id") userId: UInt,
        @Query("account_id") accountId: UInt
    ): Response<MultipleTransactionResponse>

    @GET("${ApiConstants.selectTransactionsV2MMethod}.${ApiConstants.serverFileExtension}")
    suspend fun selectTransactionsV2M(
        @Query("user_id") userId: UInt
    ): Response<MultipleTransactionResponse>

    @FormUrlEncoded
    @POST("${ApiConstants.insertTransactionMethod}.${ApiConstants.serverFileExtension}")
    suspend fun insertTransaction(
        @Field("event_date_time") eventDateTimeString: String,
        @Field("user_id") userId: UInt,
        @Field("particulars") particulars: String,
        @Field("amount") amount: Float,
        @Field("from_account_id") fromAccountId: UInt,
        @Field("to_account_id") toAccountId: UInt
    ): Response<TransactionManipulationResponse>

    @FormUrlEncoded
    @POST("${ApiConstants.updateTransactionMethod}.${ApiConstants.serverFileExtension}")
    suspend fun updateTransaction(
        @Field("event_date_time") eventDateTimeString: String,
        @Field("particulars") particulars: String,
        @Field("amount") amount: Float,
        @Field("from_account_id") fromAccountId: UInt,
        @Field("to_account_id") toAccountId: UInt,
        @Field("id") transactionId: UInt
    ): Response<TransactionManipulationResponse>

    @FormUrlEncoded
    @POST("${ApiConstants.deleteTransactionMethod}.${ApiConstants.serverFileExtension}")
    suspend fun deleteTransaction(
        @Field("id") transactionId: UInt
    ): Response<TransactionManipulationResponse>

    @GET("${ApiConstants.selectUserTransactionsAfterSpecifiedDateMethod}.${ApiConstants.serverFileExtension}")
    suspend fun selectUserTransactionsAfterSpecifiedDate(
        @Query("user_id") userId: UInt,
        @Query("specified_date") specifiedDate: String
    ): Response<MultipleTransactionResponse>
}
