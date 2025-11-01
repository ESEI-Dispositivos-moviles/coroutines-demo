package gal.uvigo.coroutines.network

import gal.uvigo.coroutines.domain.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewsRepository(
    private val api: NewsApi
) {

    suspend fun fetchHeadlines(
        category: String = "technology",
        country: String = "us"
    ): List<Article> = withContext(Dispatchers.IO) {
        val resp = api.topHeadlines(category = category, country = country)
        if (resp.status != "ok") {
            val msg = resp.message ?: "NewsAPI error: ${resp.code ?: "unknown"}"
            throw IllegalStateException(msg)
        }
        resp.articles.orEmpty().mapIndexed { index, dto ->
            val id = dto.source?.id ?: dto.url ?: "article-$index"
            val title = dto.title ?: ""
            val sourceName = dto.source?.name ?: ""
            Article(id, title, sourceName)
        }
    }
}