package gal.uvigo.coroutines.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gal.uvigo.coroutines.domain.Article
import gal.uvigo.coroutines.network.NewsRepository
import gal.uvigo.coroutines.network.NewsService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
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

    // Use an explicit scope so the file compiles even if the lifecycle KTX extension isn't resolved by the analyzer
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    fun refresh(category: String = "technology", country: String = "us") {
        scope.launch {
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

    override fun onCleared() {
        scope.cancel()
        super.onCleared()
    }
}
