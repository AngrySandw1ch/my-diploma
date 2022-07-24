package ru.netology.mydiploma.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import ru.netology.mydiploma.R
import ru.netology.mydiploma.adapter.JobAdapter
import ru.netology.mydiploma.adapter.OnJobInteractionListener
import ru.netology.mydiploma.databinding.FragmentUserDetailsBinding
import ru.netology.mydiploma.dto.Job
import ru.netology.mydiploma.dto.User
import ru.netology.mydiploma.ui.user.UsersFragment.Companion.USER_KEY
import ru.netology.mydiploma.viewmodel.AuthViewModel
import ru.netology.mydiploma.viewmodel.JobViewModel

class UserDetailsFragment : Fragment() {

    lateinit var binding: FragmentUserDetailsBinding
    private val jobViewModel: JobViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    companion object {
        const val JOB_KEY = "JOB_KEY"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserDetailsBinding.inflate(inflater, container, false)

        val user: User? = arguments?.getParcelable(USER_KEY)

        user?.let {
            with(binding) {
                userCard.userName.text = it.name
                Glide.with(userCard.avatar)
                    .load(user.avatar)
                    .placeholder(R.drawable.ic_cloud_download_24)
                    .error(R.drawable.ic_person_24)
                    .circleCrop()
                    .timeout(30_000)
                    .into(userCard.avatar)
            }
        }

        val jobAdapter = JobAdapter(object : OnJobInteractionListener{
            override fun onEdit(job: Job) {
                jobViewModel.edit(job)
                findNavController().navigate(R.id.action_userDetailsFragment_to_editJobFragment, Bundle().apply {
                    putParcelable(JOB_KEY, job)
                })
            }

            override fun onRemove(job: Job) {
                jobViewModel.removeJob(job.id)
            }
        })

        binding.jobList.adapter = jobAdapter

        binding.jobSwipeRefresh.setOnRefreshListener {
            jobViewModel.refreshJobs()
        }

        binding.buttonShow.setOnClickListener {
            binding.jobList.isVisible = !binding.jobList.isVisible
        }

        binding.buttonAddJob.setOnClickListener {
            jobViewModel.edit()
            findNavController().navigate(R.id.action_userDetailsFragment_to_newJobFragment)
        }

        jobViewModel.data.observe(viewLifecycleOwner) {
            jobAdapter.submitList(it)
        }

        jobViewModel.dataState.observe(viewLifecycleOwner) {
            binding.progressJob.isVisible = it.loading
            binding.jobSwipeRefresh.isVisible = it.refreshing
        }

        authViewModel.data.observe(viewLifecycleOwner) {
            binding.buttonAddJob.isVisible = it.id != 0L
        }

        return binding.root
    }
}