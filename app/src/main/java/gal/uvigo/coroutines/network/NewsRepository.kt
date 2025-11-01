package gal.uvigo.coroutines.network

import gal.uvigo.coroutines.domain.Article
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewsRepository(
    private val api: NewsApi,
    private val io: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun fetch(): List<Article> = withContext(io) {
        // Optional: delay(800) to simulate latency in class
        api.getArticles().map { Article(it.id, it.title) }
    }
}