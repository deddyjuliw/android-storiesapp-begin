package com.djw.storiesapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.djw.storiesapp.data.response.ListStoryItem
import com.djw.storiesapp.databinding.ItemStoryBinding
import com.djw.storiesapp.ui.detail.DetailActivity
import com.djw.storiesapp.withDateFormat

class StoryListAdapter(private val listStory: ArrayList<ListStoryItem>) : RecyclerView.Adapter<StoryListAdapter.ListViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listStory[position])
    }

    override fun getItemCount(): Int = listStory.size

    class ListViewHolder(private var binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(story: ListStoryItem) {
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .into(binding.ivItemPhoto)
            binding.apply {
                tvItemName.text = story.name
                tvRvCreate.withDateFormat(story.createdAt.toString())
                tvRvDesc.text = story.description
            }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_STORY, story)
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.ivItemPhoto, "photo"),
                        Pair(binding.tvItemName, "name"),
                        Pair(binding.tvRvCreate, "create"),
                        Pair(binding.tvRvDesc, "desc")
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }
}