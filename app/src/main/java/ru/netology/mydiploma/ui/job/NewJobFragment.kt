package ru.netology.mydiploma.ui.job

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.mydiploma.R
import ru.netology.mydiploma.databinding.FragmentNewJobBinding
import ru.netology.mydiploma.ui.user.UsersFragment.Companion.USER_ID_KEY
import ru.netology.mydiploma.util.*
import ru.netology.mydiploma.viewmodel.JobViewModel
import java.util.*

@AndroidEntryPoint
class NewJobFragment : Fragment() {
    lateinit var binding: FragmentNewJobBinding
    private val viewModel: JobViewModel by viewModels()
    private var calendar: Calendar? = null

    companion object {
        const val START_KEY = "START_KEY"
        const val FINISH_KEY = "FINISH_KEY"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val userId: Long? = arguments?.getLong(USER_ID_KEY)

        binding = FragmentNewJobBinding.inflate(inflater, container, false)

        calendar = Calendar.getInstance()

        with(binding) {
            chooseStart.text = savedInstanceState?.getString(START_KEY) ?: getString(R.string.start)
            chooseFinish.text = savedInstanceState?.getString(FINISH_KEY) ?: getString(R.string.finish)
        }

        binding.jobNameText.requestFocus()


        binding.chooseStart.setOnClickListener {
            setDate(calendar) {
                initStart()
            }
        }

        binding.chooseFinish.setOnClickListener {
            setDate(calendar) {
                initFinish()
            }
        }

        binding.jobNameText.setOnEditorActionListener { view, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    AndroidUtils.hideKeyboard(requireView())
                    binding.positionTextInputLayout.requestFocus()
                    true
                }
                else -> false
            }
        }

        binding.createJob.setOnClickListener {

            if (binding.chooseStart.text.toString() == getString(R.string.start))  {
                showToast(getString(R.string.warning_message))
                return@setOnClickListener
            }

            viewModel.changeNameAndPosition(
                binding.jobNameText.text.toString(),
                binding.jobPositionText.text.toString()
            )

            val link = binding.jobLinkText.text.toString()
            if (link.isBlank()) viewModel.changeLink() else viewModel.changeLink(link)

            if (binding.chooseFinish.text.toString() == getString(R.string.finish)) viewModel.changeFinish()

            userId?.let {
                viewModel.save(it)
            }

            findNavController().navigateUp()
        }

        binding.close.setOnClickListener {
            viewModel.clearEdited()
            with(binding) {
                jobNameText.text?.clear()
                jobPositionText.text?.clear()
                jobLinkText.text?.clear()
                chooseFinish.text = getString(R.string.finish)
                chooseStart.text = getString(R.string.start)
            }
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onDetach() {
        super.onDetach()
        calendar = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(START_KEY, binding.chooseStart.text.toString())
        outState.putString(FINISH_KEY, binding.chooseFinish.text.toString())
    }

    private fun initStart() {
        calendar?.timeInMillis?.let {
            binding.chooseStart.text = FormatUtils.formatJustDate(it)
            viewModel.changeStart(it)
        }
    }

    private fun initFinish() {
        calendar?.timeInMillis?.let {
            binding.chooseFinish.text = FormatUtils.formatJustDate(it)
            viewModel.changeFinish(it)
        }
    }
}