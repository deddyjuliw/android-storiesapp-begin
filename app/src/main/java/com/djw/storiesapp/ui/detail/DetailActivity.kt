package com.djw.storiesapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.djw.storiesapp.R
import com.djw.storiesapp.data.response.ListStoryItem
import com.djw.storiesapp.databinding.ActivityDetailBinding
import com.djw.storiesapp.withDateFormat

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.title = resources.getString(R.string.app_nameDetail)

        val story = intent.getParcelableExtra<ListStoryItem>(EXTRA_STORY)
        binding.apply {
            tvDetailName.text = story?.name
            tvCreated.withDateFormat(story?.createdAt.toString())
            tvDetailDescription.text = story?.description
        }
        Glide.with(this)
            .load(story?.photoUrl)
            .into(binding.ivDetailPhoto)
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}