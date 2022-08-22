package ru.netology.mydiploma.ui.event

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.mydiploma.R
import ru.netology.mydiploma.databinding.FragmentNewEventBinding
import ru.netology.mydiploma.ui.post.MAX_SIZE
import ru.netology.mydiploma.util.*
import ru.netology.mydiploma.viewmodel.EventViewModel
import java.util.*

@AndroidEntryPoint
class NewEventFragment : Fragment() {

    lateinit var binding: FragmentNewEventBinding
    private var dateAndTime: Calendar? = null
    private val viewModel: EventViewModel by viewModels()

    companion object {
        const val DATE_KEY = "date"
        const val TIME_KEY = "time"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewEventBinding.inflate(inflater, container, false)
        dateAndTime = Calendar.getInstance()

        binding.newEventContent.requestFocus()

        binding.chooseDate.text = savedInstanceState?.getString(DATE_KEY) ?: getString(R.string.date)
        binding.chooseTime.text = savedInstanceState?.getString(TIME_KEY) ?: getString(R.string.time)


        val pickPhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                ImagePicker.RESULT_ERROR -> {
                   showSnack(ImagePicker.getError(it.data))
                }
                Activity.RESULT_OK -> {
                    val uri: Uri? = it.data?.data
                    viewModel.changePhoto(uri, uri?.toFile())
                }
            }
        }

        binding.pickPhoto.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(MAX_SIZE)
                .provider(ImageProvider.GALLERY)
                .galleryMimeTypes(
                    arrayOf(
                        "image/png",
                        "image/jpeg"
                    )
                    )
                .createIntent {
                    pickPhotoLauncher.launch(it)
                }
        }

        binding.takePhoto.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(MAX_SIZE)
                .provider(ImageProvider.CAMERA)
                .createIntent {
                    pickPhotoLauncher.launch(it)
                }
        }

        binding.buttonCreateEvent.setOnClickListener {
            viewModel.changeContent(binding.newEventContent.text.toString())
            viewModel.save()
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }

        binding.buttonClearAll.setOnClickListener {
            binding.apply {
                newEventContent.text.clear()
                radioGroup.clearCheck()
                chooseTime.text = getString(R.string.time)
                chooseDate.text = getString(R.string.date)
                imageNewEvent.setImageURI(null)
            }
            viewModel.clearEdited()
            viewModel.changePhoto(null, null)
            AndroidUtils.hideKeyboard(requireView())
        }

        binding.chooseDate.setOnClickListener {
            setDate(dateAndTime) {
                initDate()
            }
        }

        binding.chooseTime.setOnClickListener {
            setTime(dateAndTime) {
                initTime()
            }
        }

        binding.radioGroup.setOnCheckedChangeListener { _, buttonId ->
            when(buttonId) {
                R.id.online -> viewModel.changeEventType(getString(R.string.online))
                R.id.offline -> viewModel.changeEventType(getString(R.string.offline))
            }
        }


        viewModel.photo.observe(viewLifecycleOwner) { photoModel ->
            if (photoModel.uri == null) {
                binding.imageNewEvent.visibility = View.GONE
                return@observe
            }
            binding.imageNewEvent.apply {
                visibility = View.VISIBLE
                setImageURI(photoModel.uri)
            }
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(DATE_KEY, binding.chooseDate.text.toString())
        outState.putString(TIME_KEY, binding.chooseTime.text.toString())
    }

    override fun onDetach() {
        super.onDetach()
        dateAndTime = null
    }

    private fun initDate() {
        dateAndTime?.timeInMillis?.let {
            binding.chooseDate.text = FormatUtils.formatJustDate(it)
            viewModel.changeDatetime(it)
        }
    }

    private fun initTime() {
        dateAndTime?.timeInMillis?.let {
            binding.chooseTime.text = FormatUtils.formatJustTime(it)
            viewModel.changeDatetime(it)
        }
    }
}