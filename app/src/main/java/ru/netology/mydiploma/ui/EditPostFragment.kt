package ru.netology.mydiploma.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.mydiploma.databinding.FragmentEditPostBinding
import ru.netology.mydiploma.util.AndroidUtils
import ru.netology.mydiploma.viewmodel.PostViewModel

class EditPostFragment : Fragment() {

    lateinit var binding: FragmentEditPostBinding
    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditPostBinding.inflate(inflater, container, false)

        binding.postContent.setText(arguments?.getString(TEXT_KEY))
        binding.postContent.requestFocus()


        binding.applyChanges.setOnClickListener {
            viewModel.changeContent(binding.postContent.text.toString())
            viewModel.save()
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }

        binding.denyChanges.setOnClickListener {
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }

        return binding.root
    }
    
}