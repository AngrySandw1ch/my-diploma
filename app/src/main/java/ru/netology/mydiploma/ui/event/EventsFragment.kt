package ru.netology.mydiploma.ui.event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.mydiploma.R
import ru.netology.mydiploma.adapter.EventAdapter
import ru.netology.mydiploma.adapter.OnEventInteractionListener
import ru.netology.mydiploma.databinding.FragmentEventsBinding
import ru.netology.mydiploma.dto.Event
import ru.netology.mydiploma.viewmodel.AuthViewModel
import ru.netology.mydiploma.viewmodel.EventViewModel

class EventsFragment : Fragment() {

    private val viewModel: EventViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )
    private val authViewModel: AuthViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )
    lateinit var binding: FragmentEventsBinding

    companion object {
        const val EDIT_EVENT_CONTENT_KEY = "event_content"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventsBinding.inflate(inflater, container, false)

        val adapter = EventAdapter(object : OnEventInteractionListener {
            override fun onLike(event: Event) {
                when (event.likedByMe) {
                    true -> viewModel.dislikeEvent(event.id)
                    else -> viewModel.likeEvent(event.id)
                }
            }

            override fun onJoin(event: Event) {
                when (event.participatedByMe) {
                    true -> viewModel.leaveEvent(event.id)
                    else -> viewModel.joinEvent(event.id)
                }

            }

            override fun onRemove(event: Event) {
                viewModel.removeEvent(event.id)
            }

            override fun onEdit(event: Event) {
                viewModel.edit(event)
                findNavController().navigate(R.id.action_eventsFragment_to_editEventFragment, Bundle().apply {
                    putString(EDIT_EVENT_CONTENT_KEY, event.content)
                })
            }
        })

        binding.eventContainer.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events)
        }

        viewModel.dataState.observe(viewLifecycleOwner) {
            with(binding) {
                swipeRefreshEvents.isRefreshing = it.refreshing
                progressEvents.isVisible = it.loading
            }
        }

        authViewModel.data.observe(viewLifecycleOwner) {
            binding.newEvent.isVisible = it.id != 0L
        }

        binding.swipeRefreshEvents.setOnRefreshListener {
            viewModel.refreshEvents()
        }

        binding.newEvent.setOnClickListener {
            findNavController().navigate(R.id.action_eventsFragment_to_newEventFragment)
        }

        return binding.root
    }
}