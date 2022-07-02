package ru.netology.mydiploma.ui.post

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import ru.netology.mydiploma.R
import ru.netology.mydiploma.adapter.OnInteractionListener
import ru.netology.mydiploma.adapter.PostAdapter
import ru.netology.mydiploma.databinding.FragmentFeedBinding
import ru.netology.mydiploma.dto.Post
import ru.netology.mydiploma.viewmodel.AuthViewModel
import ru.netology.mydiploma.viewmodel.PostViewModel

const val TEXT_KEY = "edit text"

class FeedFragment : Fragment() {
    lateinit var binding: FragmentFeedBinding
    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    private val authViewModel: AuthViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedBinding.inflate(inflater, container, false)
        val adapter = PostAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                when {
                    post.likedByMe -> viewModel.dislikePostById(post)
                    else -> viewModel.likePostById(post)
                }
            }

            override fun onRemove(post: Post) {
                viewModel.removePostById(post)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                findNavController().navigate(
                    R.id.action_feedFragment_to_editPostFragment,
                    Bundle().apply {
                        putString(TEXT_KEY, post.content)
                    })
            }
        })

        val divider =
            MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.HORIZONTAL)

        binding.container.apply {
            this.adapter = adapter
            this.addItemDecoration(divider)
        }

        viewModel.data.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.dataState.observe(viewLifecycleOwner) {
            with(binding) {
                swipeRefresh.isRefreshing = it.refreshing
                progress.isVisible = it.loading
            }
        }

        authViewModel.data.observe(viewLifecycleOwner) {
            binding.fab.isVisible = it.id != 0L
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshPosts()
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        return binding.root
    }
}