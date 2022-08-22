package ru.netology.mydiploma.ui.event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.mydiploma.R
import ru.netology.mydiploma.databinding.FragmentEditEventBinding
import ru.netology.mydiploma.ui.event.EventsFragment.Companion.EDIT_EVENT_CONTENT_KEY
import ru.netology.mydiploma.ui.event.NewEventFragment.Companion.DATE_KEY
import ru.netology.mydiploma.ui.event.NewEventFragment.Companion.TIME_KEY
import ru.netology.mydiploma.util.AndroidUtils
import ru.netology.mydiploma.util.FormatUtils
import ru.netology.mydiploma.util.setDate
import ru.netology.mydiploma.util.setTime
import ru.netology.mydiploma.viewmodel.EventViewModel
import java.util.*

@AndroidEntryPoint
class EditEventFragment : Fragment() {

    lateinit var binding: FragmentEditEventBinding
    private val viewModel: EventViewModel by viewModels()
    private var dateAndTime: Calendar? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditEventBinding.inflate(inflater, container, false)

        dateAndTime = Calendar.getInstance()

        binding.editTimeButton.text = savedInstanceState?.getString(DATE_KEY) ?: getString(R.string.date)
        binding.editTimeButton.text = savedInstanceState?.getString(TIME_KEY) ?: getString(R.string.time)

        with(binding) {
            editEventContent.apply {
                arguments?.let { bundle ->
                    this.setText(bundle.getString(EDIT_EVENT_CONTENT_KEY))
                    this.requestFocus()
                }
            }

            cancel.setOnClickListener {
                findNavController().navigateUp()
                viewModel.clearEdited()
                editEventContent.setText("")
                radioGroup.clearCheck()
                editDateButton.text = getString(R.string.date)
                editTimeButton.text = getString(R.string.time)
                AndroidUtils.hideKeyboard(requireView())
            }

            radioGroup.setOnCheckedChangeListener { _, buttonId ->
                when(buttonId) {
                    R.id.edit_online -> viewModel.changeEventType(getString(R.string.online))
                    R.id.edit_offline -> viewModel.changeEventType(getString(R.string.offline))
                }
            }

            editDateButton.setOnClickListener {
                setDate(dateAndTime) {
                    initDate()
                }
            }

            editTimeButton.setOnClickListener {
                setTime(dateAndTime) {
                    initTime()
                }
            }

            acceptChanges.setOnClickListener {
                viewModel.changeContent(editEventContent.text.toString())
                viewModel.save()
                findNavController().navigateUp()
                AndroidUtils.hideKeyboard(requireView())
            }
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(DATE_KEY, binding.editDateButton.text.toString())
        outState.putString(TIME_KEY, binding.editTimeButton.text.toString())
    }

    override fun onDetach() {
        super.onDetach()
        dateAndTime = null
    }

    private fun initTime() {
        dateAndTime?.let {
            binding.editTimeButton.text = FormatUtils.formatJustTime(it.timeInMillis)
            viewModel.changeDatetime(it.timeInMillis)
        }
    }

    private fun initDate() {
        dateAndTime?.let {
            binding.editDateButton.text = FormatUtils.formatJustDate(it.timeInMillis)
            viewModel.changeDatetime(it.timeInMillis)
        }
    }

}