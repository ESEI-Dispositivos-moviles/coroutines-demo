package gal.uvigo.coroutines.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gal.uvigo.coroutines.domain.Article
import gal.uvigo.coroutines.network.NewsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class NewsViewModel(
    private val repo: NewsRepository = NewsRepository()
) : ViewModel() {

    private val _articles = MutableLiveData<List<Article>>(emptyList())
    val articles: LiveData<List<Article>> = _articles

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    fun refresh(category: String = "technology", country: String = "us") {
        // TODO 07. suspend function must be called from a coroutine. We can use launch to start a new coroutine.
        // TODO 08. launch must be called from a CoroutineScope, so we create one tied to the ViewModel lifecycle.
        _loading.value = true
        _error.value = null
        try {
            // TODO 02. Need to make this call non-blocking by using coroutines.
            // TODO 03. We start by making the repository function a suspend function.
            val items = repo.fetchHeadlines(category, country)
            _articles.value = items
        } catch (t: Throwable) {
            _error.value = t.message ?: "Failed to load"
        } finally {
            _loading.value = false
        }
    }

    // TODO 09. We need to cancel the CoroutineScope when the ViewModel is cleared to avoid memory leaks.

}
