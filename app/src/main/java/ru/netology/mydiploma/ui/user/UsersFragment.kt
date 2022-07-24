package ru.netology.mydiploma.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.divider.MaterialDividerItemDecoration
import ru.netology.mydiploma.R
import ru.netology.mydiploma.adapter.UserAdapter
import ru.netology.mydiploma.databinding.FragmentUsersBinding
import ru.netology.mydiploma.viewmodel.UserViewModel

class UsersFragment : Fragment() {

    lateinit var binding: FragmentUsersBinding
    private val viewModel: UserViewModel by viewModels()

    companion object {
        const val USER_KEY = "USER_KEY"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsersBinding.inflate(inflater, container, false)

        val adapter = UserAdapter { user ->
            findNavController().navigate(R.id.action_usersFragment_to_userDetailsFragment, Bundle().apply {
                putParcelable(USER_KEY, user)
            })
        }

        binding.usersContainer.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { users ->
            adapter.submitList(users)
        }

        viewModel.dataState.observe(viewLifecycleOwner) {
            with(binding) {
                swipeRefreshUsers.isRefreshing = it.refreshing
                progressUsers.isVisible = it.loading
            }
        }

        binding.swipeRefreshUsers.setOnRefreshListener {
            viewModel.refreshUsers()
        }

        return binding.root
    }
}