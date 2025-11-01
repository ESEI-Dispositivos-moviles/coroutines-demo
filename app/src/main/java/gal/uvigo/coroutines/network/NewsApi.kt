package gal.uvigo.coroutines.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    // Example: technology headlines in the US
    @GET("v2/top-headlines")
    suspend fun topHeadlines(
        @Query("category") category: String = "technology",
        @Query("country") country: String = "us",
        @Query("pageSize") pageSize: Int = 20
    ): NewsApiResponseDto

    // Synchronous variant for demos / blocking calls
    @GET("v2/top-headlines")
    fun topHeadlinesCall(
        @Query("category") category: String = "technology",
        @Query("country") country: String = "us",
        @Query("pageSize") pageSize: Int = 20
    ): Call<NewsApiResponseDto>

    // Optional: search endpoint
    @GET("v2/everything")
    suspend fun search(
        @Query("q") query: String,
        @Query("pageSize") pageSize: Int = 20,
        @Query("sortBy") sortBy: String = "publishedAt",
    ): NewsApiResponseDto
}