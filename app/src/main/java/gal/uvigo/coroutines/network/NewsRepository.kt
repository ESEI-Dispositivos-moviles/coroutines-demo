package gal.uvigo.coroutines.network

import gal.uvigo.coroutines.domain.Article
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewsRepository(
    private val api: NewsApi,
    private val io: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun fetch(): List<Article> = fetchHeadlines()

    suspend fun fetchHeadlines(
        category: String = "technology",
        country: String = "us"
    ): List<Article> = withContext(io) {
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

    // Optional search
    suspend fun search(query: String): List<Article> = withContext(io) {
        val resp = api.search(query = query)
        if (resp.status != "ok") {
            val msg = resp.message ?: "NewsAPI error: ${resp.code ?: "unknown"}"
            throw IllegalStateException(msg)
        }
        resp.articles.orEmpty().mapIndexed { idx, a ->
            Article(
                id = a.url ?: "search-$idx",
                title = a.title ?: "(untitled)",
                source = a.source?.name ?: "Unknown"
            )
        }
    }
}