package ru.netology.mydiploma.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ru.netology.mydiploma.R
import ru.netology.mydiploma.databinding.FragmentNewPostBinding
import ru.netology.mydiploma.viewmodel.PostViewModel

class NewPostFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    private lateinit var binding: FragmentNewPostBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentNewPostBinding.inflate(inflater, container, false)

        return binding.root
    }

}