package ru.netology.mydiploma.ui.job

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.mydiploma.R
import ru.netology.mydiploma.databinding.FragmentEditJobBinding
import ru.netology.mydiploma.dto.Job
import ru.netology.mydiploma.dto.User
import ru.netology.mydiploma.ui.user.UserDetailsFragment.Companion.JOB_KEY
import ru.netology.mydiploma.ui.user.UsersFragment
import ru.netology.mydiploma.ui.user.UsersFragment.Companion.USER_ID_KEY
import ru.netology.mydiploma.util.FormatUtils
import ru.netology.mydiploma.util.ViewModelFactory
import ru.netology.mydiploma.util.setDate
import ru.netology.mydiploma.viewmodel.JobViewModel
import java.util.*

class EditJobFragment : Fragment() {

    lateinit var binding: FragmentEditJobBinding
    private var calendar: Calendar? = null
    private val viewModel: JobViewModel by viewModels {
        ViewModelFactory(arguments?.getLong(USER_ID_KEY), requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditJobBinding.inflate(inflater, container, false)

        val job: Job? = arguments?.getParcelable(JOB_KEY)
        calendar = Calendar.getInstance()

        with(binding) {
            job?.let {
                editJobNameText.setText(it.name)
                editJobPositionText.setText(it.position)
                it.link?.let { link ->
                    editJobLinkText.setText(link)
                }
                editChooseStart.text = FormatUtils.formatJustDate(it.start)
                it.finish?.let { finish ->
                    editChooseFinish.text = FormatUtils.formatJustDate(finish)
                }
            }
        }

        binding.acceptJobChanges.setOnClickListener {
            viewModel.changeNameAndPosition(
                binding.editJobNameText.text.toString(),
                binding.editJobPositionText.text.toString()
            )
            val link = binding.editJobLinkText.text.toString()
            if (link.isBlank()) viewModel.changeLink() else viewModel.changeLink(link)

            if (binding.editChooseFinish.text.toString() == getString(R.string.finish)) viewModel.changeFinish()

            viewModel.save()
            viewModel.refreshJobs()
            findNavController().navigateUp()
        }

        binding.buttonDenyChanges.setOnClickListener {
            viewModel.clearEdited()
            findNavController().navigateUp()
        }

        binding.editChooseStart.setOnClickListener {
            setDate(calendar) {
                initStart()
            }
        }

        binding.editChooseFinish.setOnClickListener {
            setDate(calendar) {
                initFinish()
            }
        }

        return binding.root
    }

    override fun onDetach() {
        super.onDetach()
        calendar = null
    }

    private fun initStart() {
        calendar?.timeInMillis?.let {
            binding.editChooseStart.text = FormatUtils.formatJustDate(it)
            viewModel.changeStart(it)
        }
    }

    private fun initFinish() {
        calendar?.timeInMillis?.let {
            binding.editChooseFinish.text = FormatUtils.formatJustDate(it)
            viewModel.changeFinish(it)
        }
    }
}