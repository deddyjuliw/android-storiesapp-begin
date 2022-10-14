package com.djw.storiesapp.ui.main

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.djw.storiesapp.R
import com.djw.storiesapp.adapter.StoryListAdapter
import com.djw.storiesapp.data.Result
import com.djw.storiesapp.data.response.ListStoryItem
import com.djw.storiesapp.databinding.ActivityMainBinding
import com.djw.storiesapp.ui.StoryViewModelFactory
import com.djw.storiesapp.ui.signin.SigninActivity
import com.djw.storiesapp.ui.story.AddStoryActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
    }

    private fun setupViewModel(){
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            binding.rvStory.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvStory.layoutManager = LinearLayoutManager(this)
        }

        val factory: StoryViewModelFactory = StoryViewModelFactory.getInstance(this)
        mainViewModel = ViewModelProvider(this, factory).get(
            MainViewModel::class.java
        )

        mainViewModel.isLogin().observe(this){
            if (!it){
                startActivity(Intent(this, SigninActivity::class.java))
                finish()
            }
        }

        mainViewModel.getToken().observe(this) { token ->
            if (token.isNotEmpty()) {
                mainViewModel.getStories(token).observe(this) { result ->
                    if (result != null) {
                        when(result) {
                            is Result.Loading -> {
                                showLoading(true)
                            }
                            is Result.Success -> {
                                showLoading(false)
                                val data = result.data.listStory
                                val storyListAdapter = StoryListAdapter(data as ArrayList<ListStoryItem>)
                                binding.rvStory.adapter = storyListAdapter
                            }
                            is Result.Error -> {
                                showLoading(false)
                                Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.add_story -> {
                startActivity(Intent(this, AddStoryActivity::class.java))
                true
            }
            R.id.logout -> {
                mainViewModel.logout()
                true
            }
            else -> true
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading){
            binding.progressBarLayout.visibility = View.VISIBLE
        } else {
            binding.progressBarLayout.visibility = View.GONE
        }
    }
}