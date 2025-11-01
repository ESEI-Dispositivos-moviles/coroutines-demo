package gal.uvigo.coroutines.network

import gal.uvigo.coroutines.domain.Article
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

class NewsRepository {

    // TODO 04. Need to make this function a suspend function.
    fun fetchHeadlines(category: String, country: String): List<Article> {
        // TODO 05. Replace synchronous Call execution with topHeadlinesCall with suspend function call with topHeadlines.
        // val response: NewsApiResponseDto = NewsService.api.topHeadlines(category = category, country = country)
        val call: Call<NewsApiResponseDto> = NewsService.api.topHeadlinesCall(category = category, country = country)
        val response: Response<NewsApiResponseDto> = call.execute()
        if (response.isSuccessful) {
            val dtoList = response.body()?.articles ?: emptyList()
            // Map DTO to domain model safely
            return dtoList.map { dto ->
                Article(
                    id = dto.url ?: "",
                    title = dto.title ?: "(no title)",
                    source = dto.source?.name ?: "(unknown)"
                )
            }
        } else {
            val err = response.errorBody()?.string()
            throw IOException("HTTP ${response.code()} ${response.message()}${if (err != null) ": $err" else ""}")
        }
    }
}