package gal.uvigo.coroutines.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textview.MaterialTextView
import gal.uvigo.coroutines.R
import gal.uvigo.coroutines.domain.Article
import gal.uvigo.coroutines.model.NewsViewModel

class NewsFragment : Fragment(R.layout.fragment_news) {

    private val vm: NewsViewModel by viewModels()
    private lateinit var adapter: ArticlesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar)
        val recycler = view.findViewById<RecyclerView>(R.id.recycler)
        val progress = view.findViewById<CircularProgressIndicator>(R.id.progress)
        val errorText = view.findViewById<MaterialTextView>(R.id.errorText)

        // Toolbar setup
        toolbar.title = getString(R.string.app_name)
        toolbar.inflateMenu(R.menu.menu_news) // create menu with a single Refresh item
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_refresh -> { vm.refresh(category = "technology", country = "us"); true }
                else -> false
            }
        }

        // Recycler setup
        adapter = ArticlesAdapter()
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        vm.articles.observe(viewLifecycleOwner) { list: List<Article> ->
            adapter.submitList(list)
            errorText.visibility = if (list.isEmpty() && vm.error.value != null) View.VISIBLE else View.GONE
        }

        vm.loading.observe(viewLifecycleOwner) { loading ->
            progress.visibility = if (loading) View.VISIBLE else View.GONE
        }

        vm.error.observe(viewLifecycleOwner) { err ->
            errorText.text = err ?: ""
            errorText.visibility = if (err != null) View.VISIBLE else View.GONE
        }

        // Trigger initial load
        vm.refresh()
    }
}
