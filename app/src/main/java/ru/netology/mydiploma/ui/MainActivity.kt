package ru.netology.mydiploma.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.mydiploma.R
import ru.netology.mydiploma.adapter.OnInteractionListener
import ru.netology.mydiploma.adapter.PostAdapter
import ru.netology.mydiploma.databinding.ActivityMainBinding
import ru.netology.mydiploma.dto.Post
import ru.netology.mydiploma.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val viewModel:PostViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostAdapter(object: OnInteractionListener {
            override fun onLike(post: Post) {
                when {
                    post.likedByMe -> viewModel.likePostById(post)
                    else -> viewModel.dislikePostById(post)
                }
            }

            override fun onRemove(post: Post) {
                viewModel.removePostByID(post)
            }

            override fun onEdit(post: Post) {
            }
        })

        binding.recycler.adapter = adapter

        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }
    }
}