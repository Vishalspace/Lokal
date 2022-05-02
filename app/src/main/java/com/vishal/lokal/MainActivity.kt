package com.vishal.lokal

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.vishal.lokal.api.NewsApi
import com.vishal.lokal.databinding.ActivityMainBinding
import com.vishal.lokal.ui.NewsAdapter
import com.vishal.lokal.utils.addTo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var api: NewsApi
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
        startAutoRefresh()
    }

    private fun startAutoRefresh() {
        Observable.interval(0, 4, TimeUnit.SECONDS, Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnEach {
                if (!binding.swipeRefreshLayout.isRefreshing) callApi()
            }
            .subscribe()
            .addTo(compositeDisposable)
    }

    private fun callApi() {
        api.getQuotes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { binding.swipeRefreshLayout.isRefreshing = false }
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
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }


}


