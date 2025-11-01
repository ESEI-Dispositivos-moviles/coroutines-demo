package gal.uvigo.coroutines.network

// --- DTOs exactly as NewsAPI returns ---
data class NewsApiArticleDto(
    val source: SourceDto?,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?
) {
    data class SourceDto(val id: String?, val name: String?)
}