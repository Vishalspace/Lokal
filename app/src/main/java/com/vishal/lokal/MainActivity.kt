package com.vishal.lokal

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.vishal.lokal.api.NewsService
import com.vishal.lokal.databinding.ActivityMainBinding
import com.vishal.lokal.ui.NewsAdapter
import com.vishal.lokal.utils.Logger
import com.vishal.lokal.utils.addTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var newsService: NewsService
    private lateinit var binding: ActivityMainBinding
    private val compositeDisposable = CompositeDisposable()
    private val adapter = NewsAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).component.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initUi(binding)
    }

    private fun initUi(binding: ActivityMainBinding) {
        binding.newsrecycler.layoutManager = LinearLayoutManager(this)
        binding.newsrecycler.adapter = adapter
        binding.swipeRefreshLayout.setOnRefreshListener {
            callApi()
        }
    }

    override fun onResume() {
        super.onResume()
        callApi()
    }

    private fun callApi(query: String? = null, selectedFilter: String? = null) {
        newsService.get(query, selectedFilter)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { binding.swipeRefreshLayout.isRefreshing = false }
            .doOnError { e ->
                logger.error("Error fetching news: q=${query}", e)
                Toast.makeText(this, "Error fetching news", Toast.LENGTH_SHORT).show()
            }
            .subscribe { result ->
                adapter.submitList(result.articles)
            }.addTo(compositeDisposable)
    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        initSearchMenu(menu.findItem(R.id.action_search))
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.filter_business -> callApi(selectedFilter = "business")
            R.id.filter_tech -> callApi(selectedFilter = "technology")
            R.id.filter_entertainment -> callApi(selectedFilter = "entertainment")
            R.id.filter_general -> callApi(selectedFilter = "general")
            R.id.filter_health -> callApi(selectedFilter = "health")
            R.id.filter_science -> callApi(selectedFilter = "science")
            R.id.filter_sports -> callApi(selectedFilter = "sports")
            else -> callApi(selectedFilter = "general")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initSearchMenu(search: MenuItem) {
        val searchView = search.actionView as SearchView
        searchView.queryHint = "Search"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                callApi(query)
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                callApi(query)
                return false
            }
        })
    }

    companion object {
        private val logger = Logger(MainActivity::class.java.name)
    }

}


