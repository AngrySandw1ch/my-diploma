package ru.netology.mydiploma.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.material.divider.MaterialDividerItemDecoration
import ru.netology.mydiploma.R
import ru.netology.mydiploma.adapter.EventAdapter
import ru.netology.mydiploma.adapter.OnEventInteractionListener
import ru.netology.mydiploma.databinding.CardEventBinding
import ru.netology.mydiploma.databinding.FragmentEventsBinding
import ru.netology.mydiploma.dto.Event
import ru.netology.mydiploma.viewmodel.EventViewModel

class EventsFragment : Fragment() {

    val viewModel: EventViewModel by viewModels()
    lateinit var binding: FragmentEventsBinding

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
        })
        val divider = MaterialDividerItemDecoration(
            requireContext(),
            MaterialDividerItemDecoration.HORIZONTAL
        ).apply {
            dividerInsetStart = 16
            dividerInsetEnd = 16
        }

        binding.eventContainer.apply {
            this.adapter = adapter
            this.addItemDecoration(divider)
        }

        viewModel.data.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events)
        }

        viewModel.dataState.observe(viewLifecycleOwner) {
            with(binding) {
                swipeRefreshEvents.isRefreshing = it.refreshing
                progressEvents.isVisible = it.loading
            }
        }

        binding.swipeRefreshEvents.setOnRefreshListener {
            viewModel.refreshEvents()
        }

        return binding.root
    }

}