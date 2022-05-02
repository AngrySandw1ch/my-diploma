package ru.netology.mydiploma.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ru.netology.mydiploma.R
import ru.netology.mydiploma.adapter.UserAdapter
import ru.netology.mydiploma.databinding.FragmentUsersBinding
import ru.netology.mydiploma.viewmodel.UserViewModel

class UsersFragment : Fragment() {

    lateinit var binding: FragmentUsersBinding
    private val viewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsersBinding.inflate(inflater, container, false)
        val adapter = UserAdapter()
        binding.usersContainer.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { users ->
            adapter.submitList(users)
        }

        return binding.root
    }
}