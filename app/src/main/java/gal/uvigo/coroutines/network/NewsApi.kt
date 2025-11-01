package gal.uvigo.coroutines.network

import retrofit2.http.GET

interface NewsApi {
    @GET("articles.json")
    suspend fun getArticles(): List<ArticleDto>
}