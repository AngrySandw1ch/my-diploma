package ru.netology.mydiploma.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toFile
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import ru.netology.mydiploma.databinding.FragmentNewPostBinding
import ru.netology.mydiploma.util.AndroidUtils
import ru.netology.mydiploma.util.showSnack
import ru.netology.mydiploma.viewmodel.PostViewModel

private const val MAX_SIZE = 2048

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



        with(binding) {
            newPostContent.requestFocus()

            saveNewPostButton.setOnClickListener {
                viewModel.changeContent(newPostContent.text.toString())
                viewModel.save()
                AndroidUtils.hideKeyboard(requireView())
                findNavController().navigateUp()
            }

            clearAllButton.setOnClickListener {
                newPostContent.text.clear()
                viewModel.changePhoto(null, null)
            }

            pickPhoto.setOnClickListener {
                ImagePicker.with(this@NewPostFragment)
                    .crop()
                    .compress(MAX_SIZE)
                    .provider(ImageProvider.GALLERY)
                    .galleryMimeTypes(
                        arrayOf(
                            "image/png",
                            "image/jpeg"
                        )
                    )
                    .start()
            }


            takePhoto.setOnClickListener {
                ImagePicker.with(this@NewPostFragment)
                    .crop()
                    .compress(MAX_SIZE)
                    .provider(ImageProvider.CAMERA)
                    .start()
            }

        }

        viewModel.photo.observe(viewLifecycleOwner) {
            if (it.uri == null) {
                binding.imageNewPost.visibility = View.GONE
                return@observe
            }
            binding.imageNewPost.apply {
                visibility = View.VISIBLE
                setImageURI(it.uri)
            }
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                val uri: Uri? = data?.data
                viewModel.changePhoto(uri, uri?.toFile())
            }
            ImagePicker.RESULT_ERROR -> {
                showSnack(ImagePicker.getError(data))
            }
        }
    }

}