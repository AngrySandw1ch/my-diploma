package ru.netology.mydiploma.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import ru.netology.mydiploma.R
import ru.netology.mydiploma.adapter.OnInteractionListener
import ru.netology.mydiploma.adapter.PostAdapter
import ru.netology.mydiploma.databinding.FragmentFeedBinding
import ru.netology.mydiploma.dto.Post
import ru.netology.mydiploma.viewmodel.PostViewModel

class FeedFragment : Fragment() {
    lateinit var binding: FragmentFeedBinding
    private val viewModel: PostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedBinding.inflate(inflater, container, false)
        val adapter = PostAdapter(object: OnInteractionListener{
            override fun onLike(post: Post) {
                when {
                    post.likedByMe -> viewModel.dislikePostById(post)
                    else -> viewModel.likePostById(post)
                }
            }
            override fun onRemove(post: Post) {
                viewModel.removePostByID(post)
            }
            override fun onEdit(post: Post) {
                //todo implement this fun
            }
        })

        val divider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.HORIZONTAL)

        binding.container.apply {
            this.adapter = adapter
            this.addItemDecoration(divider)
        }

        viewModel.data.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.fab.setOnClickListener {
            //todo here must be navigation to create post fragment
        }
        return binding.root
    }
}