package gal.uvigo.coroutines.network

import gal.uvigo.coroutines.domain.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewsRepository(){

    // TODO 04. Need to make this function a suspend function.
    suspend fun fetchHeadlines(category: String, country: String): List<Article> =
        // TODO 06. Need to move the network call off the main thread to avoid blocking it.
        // We use withContext (Dispatchers.IO) { ... } to switch to the IO dispatcher for the duration of the network call.
        withContext(Dispatchers.IO) {
            // TODO 05. Replace synchronous Call execution with topHeadlinesCall with suspend function call with topHeadlines.
            val resp = NewsService.api.topHeadlines(category = category, country = country)
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