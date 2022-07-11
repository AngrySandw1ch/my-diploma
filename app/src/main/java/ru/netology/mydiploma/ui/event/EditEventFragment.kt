package ru.netology.mydiploma.ui.event

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.mydiploma.R
import ru.netology.mydiploma.databinding.FragmentEditEventBinding
import ru.netology.mydiploma.ui.event.EventsFragment.Companion.EDIT_EVENT_CONTENT_KEY
import ru.netology.mydiploma.ui.event.NewEventFragment.Companion.DATE_KEY
import ru.netology.mydiploma.ui.event.NewEventFragment.Companion.TIME_KEY
import ru.netology.mydiploma.util.AndroidUtils
import ru.netology.mydiploma.util.FormatUtils
import ru.netology.mydiploma.viewmodel.EventViewModel
import java.util.*

class EditEventFragment : Fragment() {

    lateinit var binding: FragmentEditEventBinding
    private val viewModel: EventViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )
    private var dateAndTime: Calendar? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditEventBinding.inflate(inflater, container, false)

        dateAndTime = Calendar.getInstance()

        if (savedInstanceState != null) {
            binding.apply {
                editDateButton.text = savedInstanceState.getString(DATE_KEY)
                editTimeButton.text = savedInstanceState.getString(TIME_KEY)
            }
        }

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
                setDate()
            }

            editTimeButton.setOnClickListener {
                setTime()
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

    private fun setDate() {
        dateAndTime?.let { calendar ->
            DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfYear ->
                    calendar.set(year, monthOfYear, dayOfYear)
                    initDate()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setTime() {
        dateAndTime?.let { calendar ->
            TimePickerDialog(
                requireContext(),
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    initTime()
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }
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