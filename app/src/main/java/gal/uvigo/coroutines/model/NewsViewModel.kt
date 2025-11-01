package gal.uvigo.coroutines.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gal.uvigo.coroutines.domain.Article
import gal.uvigo.coroutines.network.NewsRepository
import gal.uvigo.coroutines.network.NewsService
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class NewsViewModel(
    private val repo: NewsRepository = NewsRepository(NewsService.api)
) : ViewModel() {

    private val _articles = MutableLiveData<List<Article>>(emptyList())
    val articles: LiveData<List<Article>> = _articles

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    // GOAL. We need to carry out parallel network requests
    fun refresh(category: String = "technology", country: String = "us") {
        // TODO 01. Define categories to fetch in parallel: category1 and category2
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                // TODO 02. Launch multiple requests in parallel using async/await
                // TODO 02.01 Use coroutine async to fetch different categories: category1 and category2
                // TODO 02.02 Await both results
                val items = repo.fetchHeadlines(category, country)
                // TODO 03. Combine results from multiple requests
                _articles.value = items
            } catch (t: Throwable) {
                if (t is CancellationException) throw t
                _error.value = t.message ?: "Failed to load"
            } finally {
                _loading.value = false
            }
        }
    }
}
