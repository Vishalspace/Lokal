package com.vishal.lokal

import android.os.Bundle
import android.view.Menu
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

    private fun callApi(query: String? = null) {
        newsService.get(query)
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
        val search = menu.findItem(R.id.action_search)
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
        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        private val logger = Logger(MainActivity::class.java.name)
    }

}


