package gal.uvigo.coroutines.network

data class NewsApiResponseDto(
    val status: String,
    val totalResults: Int?,
    val articles: List<NewsApiArticleDto>?,
    val code: String?,        // only on error
    val message: String?      // only on error
)