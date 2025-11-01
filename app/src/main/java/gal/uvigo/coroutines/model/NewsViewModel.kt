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

    // TODO 01. Replace de CoroutineScope with viewModelScope
    // Use the lifecycle-aware viewModelScope provided by androidx.lifecycle:viewmodel-ktx
    fun refresh(category: String = "technology", country: String = "us") {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val items = repo.fetchHeadlines(category, country)
                _articles.value = items
            } catch (t: Throwable) {
                if (t is CancellationException) throw t
                _error.value = t.message ?: "Failed to load"
            } finally {
                _loading.value = false
            }
        }
    }
    // TODO 02. Remove onCleared override. Not needed anymore.
    // No need to cancel viewModelScope manually; it is cancelled when the ViewModel is cleared.
}
